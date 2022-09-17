package com.example.shoppingapp.presentation.profile

import com.example.shoppingapp.databinding.FragmentProfileBinding
import com.example.shoppingapp.presentation.BaseFragment
import org.koin.android.ext.android.inject

internal class ProfileFragment: BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    // BottomNav 선택시 구분하기 위한 태그
    companion object {
        const val TAG = "ProfileFragment"
    }

    override val viewModel by inject<ProfileViewModel>()

    override fun getViewBinding(): FragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater)

    override fun observeData() {
        TODO("Not yet implemented")
    }
}