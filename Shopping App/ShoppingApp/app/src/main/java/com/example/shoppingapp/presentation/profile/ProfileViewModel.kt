package com.example.shoppingapp.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.preference.PreferenceManager
import com.example.shoppingapp.presentation.BaseViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class ProfileViewModel(
    private val preferenceManager: PreferenceManager
): BaseViewModel() {

    private var _profileStateLiveData = MutableLiveData<ProfileState>(ProfileState.Uninitialized)
    val profileStateLiveData: LiveData<ProfileState> = _profileStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        setState(ProfileState.Loading)
        // id token값에 따라서 State를 다르게 함
        preferenceManager.getIdToken()?.let {
            setState(
                ProfileState.Login(it)
            )
        } ?: kotlin.run {
            ProfileState.Success.NotRegistered
        }
    }

    private fun setState(state: ProfileState) {
        _profileStateLiveData.postValue(state)
    }

    // 파이어베이스 로그인 시 토큰을 저장하는 함수
    fun saveToken(idToken: String) = viewModelScope.launch {
        // preferences 작업을 IO 쓰레드로 처리하는게 좋음
        withContext(Dispatchers.IO) {
            preferenceManager.putIdToken(idToken)
            // 로딩 상태로 해줌
            fetchData()
        }
    }

    fun setUserInfo(firebaseUser: FirebaseUser?) = viewModelScope.launch {
        firebaseUser?.let { user ->
            setState(
                ProfileState.Success.Registered(
                    user.displayName ?: "익명",
                    user.photoUrl,
                    listOf()
                )
            )
        } ?: kotlin.run {
            setState(
                ProfileState.Success.NotRegistered
            )
        }
    }
}