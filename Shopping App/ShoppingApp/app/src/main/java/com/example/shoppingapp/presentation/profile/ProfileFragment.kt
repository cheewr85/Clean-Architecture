package com.example.shoppingapp.presentation.profile

import android.app.Activity
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.FragmentProfileBinding
import com.example.shoppingapp.extension.loadCenterCrop
import com.example.shoppingapp.extension.toast
import com.example.shoppingapp.presentation.BaseFragment
import com.example.shoppingapp.presentation.detail.ProductDetailActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.android.inject
import kotlin.Exception

internal class ProfileFragment: BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    // BottomNav 선택시 구분하기 위한 태그
    companion object {
        const val TAG = "ProfileFragment"
    }

    override val viewModel by inject<ProfileViewModel>()

    override fun getViewBinding(): FragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater)

    // 구글 사인 버튼에 대해 빌더 패턴으로 만듬
    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    // 구글 로그인을 가져옴
    private val gsc by lazy { GoogleSignIn.getClient(requireActivity(), gso) }

    // Firebase Auth를 통해 로그인을 하기위해 가져옴
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    // 런쳐 구현 & ActivityForResult와 같은 역할을 함 콜백을 받아서 처리
    private val loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // 로그인을 성공적으로 받았을 시
        if (result.resultCode == Activity.RESULT_OK) {
            // result 데이터를 받아옴(로그인 정보)
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // 결과륿 받고 예외처리도 함
                task.getResult(ApiException::class.java)?.let { account ->
                    Log.e(TAG, "firebaseAuthWithGoogle: ${account.id}")
                    viewModel.saveToken(account.idToken ?: throw Exception())
                } ?: throw Exception()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // LiveData 상태에 따라 UI 변화 반영
    override fun observeData() = viewModel.profileStateLiveData.observe(this) {
        when (it) {
            is ProfileState.Uninitialized -> initViews()
            is ProfileState.Loading -> handleLoadingState()
            is ProfileState.Login -> handleLoginState(it)
            is ProfileState.Success -> handleSuccessState(it)
            is ProfileState.Error -> handleErrorState()
        }
    }

    private fun initViews() = with(binding) {
        loginButton.setOnClickListener {
            signInGoogle()
        }
        logoutButton.setOnClickListener {

        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.isVisible = true
        loginRequiredGroup.isGone = true
    }

    private fun signInGoogle() {
        // Intent를 호출해서 gsc의 인텐트를 가져옴, 그리고 런처를 실행해서 정보를 받아옴
        val signInIntent = gsc.signInIntent
        loginLauncher.launch(signInIntent)
    }

    private fun handleSuccessState(state: ProfileState.Success) = with(binding) {
        progressBar.isGone = true
        // register 상태에 따라 나뉨
        when (state) {
            is ProfileState.Success.Registered -> {
                handleRegisteredState(state)
            }
            is ProfileState.Success.NotRegistered -> {
                profileGroup.isGone = true
                loginRequiredGroup.isVisible = true
            }
        }
    }

    private fun handleLoginState(state: ProfileState.Login) = with(binding) {
        // 토큰을 기준으로 아이디 정보를 가져옴
        val credential = GoogleAuthProvider.getCredential(state.idToken, null)
        // credential을 바탕으로 콜백을 받을 수 있음
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    // 유저 정보 처리를 함
                    viewModel.setUserInfo(user)
                } else {
                    viewModel.setUserInfo(null)
                }
            }
    }

    private fun handleRegisteredState(state: ProfileState.Success.Registered) = with(binding) {
        profileGroup.isVisible = true
        loginRequiredGroup.isGone = true
        profileImageView.loadCenterCrop(state.profileImageUri.toString(), 60f)
        userNameTextView.text = state.userName

        if (state.productList.isEmpty()) {
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
        }
    }

    private fun handleErrorState() {
        requireContext().toast("에러가 발생했습니다.")
    }
}