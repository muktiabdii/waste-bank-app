package com.example.wastebank.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wastebank.domain.usecase.UserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(private val userProfileUseCase: UserProfileUseCase) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _gender = MutableStateFlow("")
    val gender: StateFlow<String> = _gender

    private val _point = MutableStateFlow(0)
    val userPoint: StateFlow<Int> = _point

    fun getUserProfile() {
        viewModelScope.launch {
            userProfileUseCase.getUserProfile { name, email, phoneNumber, gender, point ->
                _name.value = name ?: ""
                _email.value = email ?: ""
                _phoneNumber.value = phoneNumber ?: ""
                _gender.value = gender ?: ""
                _point.value = point ?: 0
            }
        }
    }


    fun editUserProfile() {
        viewModelScope.launch {
            userProfileUseCase.editUserProfile(
                name.value, phoneNumber.value, email.value, password.value, gender.value, onResult = { success, message ->
                    if (success) {
                        // Handle success
                    } else {
                        // Handle error
                    }
                }
            )
        }
    }


    fun getUserPoint() {
        viewModelScope.launch {
            userProfileUseCase.getUserPoint { point ->
                _point.value = point ?: 0
            }
        }
    }


    fun getUserName() {
        viewModelScope.launch {
            userProfileUseCase.getUserName { name ->
                _name.value = name ?: ""
            }
        }
    }


    fun deleteAccount(onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            userProfileUseCase.deleteAccount(onResult)
        }
    }


    class Factory(private val userProfileUseCase: UserProfileUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
                return UserProfileViewModel(userProfileUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
