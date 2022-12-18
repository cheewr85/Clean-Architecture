package com.example.deliveryapp.screen.main.my

import android.app.Activity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.FragmentMyBinding
import com.example.deliveryapp.extensions.load
import com.example.deliveryapp.model.restaurant.order.OrderModel
import com.example.deliveryapp.screen.base.BaseFragment
import com.example.deliveryapp.util.provider.ResourcesProvider
import com.example.deliveryapp.widget.adapter.ModelRecyclerAdapter
import com.example.deliveryapp.widget.adapter.listener.AdapterListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyFragment: BaseFragment<MyViewModel, FragmentMyBinding>() {

    override val viewModel by viewModel<MyViewModel>()

    override fun getViewBinding(): FragmentMyBinding = FragmentMyBinding.inflate(layoutInflater)

    // Google 로그인을 위한 변수 선언
    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private val gsc by lazy { GoogleSignIn.getClient(requireActivity(), gso) }

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    // 로그인 처리를 위한 인텐트 생성 및 처리, 결과값을 얻어옴
    private val loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                task.getResult(ApiException::class.java)?.let { account ->
                    viewModel.saveToken(account.idToken ?: throw Exception())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private val resourcesProvider by inject<ResourcesProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<OrderModel, MyViewModel>(listOf(), viewModel, resourcesProvider, object : AdapterListener {})
    }

    override fun initViews() = with(binding) {
        loginButton.setOnClickListener {
            signInGoogle()
        }
        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            viewModel.signOut()
        }
        recyclerView.adapter = adapter
    }

    private fun signInGoogle() {
        val signInIntent = gsc.signInIntent
        loginLauncher.launch(signInIntent)
    }

    override fun observeData() = viewModel.myStateLiveData.observe(viewLifecycleOwner) {
        when (it) {
            is MyState.Loading -> handleLoadingState()
            is MyState.Success -> handleSuccessState(it)
            is MyState.Login -> handleLoginState(it)
            is MyState.Error -> handleErrorState(it)
            else -> Unit
        }
    }

    private fun handleLoadingState() {
        binding.loginRequiredGroup.isGone = true
        binding.progressBar.isVisible = true
    }

    private fun handleSuccessState(state: MyState.Success) = with(binding) {
        progressBar.isGone = true
        when (state) {
            is MyState.Success.Registered -> {
                handleRegisteredState(state)
            }
            is MyState.Success.NotRegistered -> {
                profileGroup.isGone = true
                loginRequiredGroup.isVisible = true
            }
        }
    }

    private fun handleRegisteredState(state: MyState.Success.Registered) = with(binding){
        profileGroup.isVisible = true
        loginRequiredGroup.isGone = true
        profileImageView.load(state.profileImageUri.toString(), 60f)
        userNameTextView.text = state.userName
        adapter.submitList(state.orderList)
    }

    private fun handleLoginState(state: MyState.Login) {
        binding.progressBar.isVisible = true
        val credential = GoogleAuthProvider.getCredential(state.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    viewModel.setUserInfo(user)
                } else {
                    firebaseAuth.signOut()
                    viewModel.setUserInfo(null)
                }
            }
    }

    private fun handleErrorState(state: MyState.Error) {

    }

    // Fragment를 보여주기 위해 객체로 생성
    companion object {

        fun newInstance() = MyFragment()

        const val TAG = "MyFragment"
    }
}