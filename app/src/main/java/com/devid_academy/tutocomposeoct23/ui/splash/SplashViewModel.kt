package com.devid_academy.tutocomposeoct23.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.Screen
import com.devid_academy.tutocomposeoct23.network.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val user: User
) : ViewModel() {

    private var _goToLogin = MutableSharedFlow<String>()
    val goToLogin = _goToLogin.asSharedFlow()

    fun getDirectionLoginAfterDelay(){
        viewModelScope.launch {
            delay(1500)
            _goToLogin.emit(if(user.getUserIdFromPreferences() != 0L)Screen.Main.route else Screen.Login.route)
        }
    }
}