package com.devid_academy.tutocomposeoct23.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.network.ApiInterface
import com.devid_academy.tutocomposeoct23.network.LoginDto
import com.devid_academy.tutocomposeoct23.network.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class LoginViewModel @Inject constructor(

    private val apiInterface: ApiInterface,
    private val user: User

) : ViewModel() {

    private val _successFlow = MutableSharedFlow<String>()
    val successFlow = _successFlow.asSharedFlow()

    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow = _errorFlow.asSharedFlow()

    fun loginUser(
        login: String,
        password: String
    ) {
        viewModelScope.launch {
            if (login.isNotEmpty() && password.isNotEmpty()) {
                try {
                    val response = apiInterface.loginUser(login, password)
                    when (response?.code()) {
                        200 -> {
                            val responseBody = response.body()
                            val token = responseBody?.token.orEmpty()
                            val userId = responseBody?.id ?: 0L
                            user.saveToken(token)
                            user.saveUserId(userId)
                            _successFlow.emit(token)
                        }
                        304 -> _errorFlow.emit("OK mais le token est inchangé (problème de sécurité)")
                        400 -> _errorFlow.emit("Problème de paramètre")
                        401 -> _errorFlow.emit("Utilisateur inconnu (mauvais login/mot de passe)")
                        503 -> _errorFlow.emit("Erreur MySQL")
                        else -> _errorFlow.emit("Une erreur est survenue, veuillez réessayer plus tard.")
                    }
                } catch (e: Exception) {
                    _errorFlow.emit("Une erreur est survenue, veuillez réessayer plus tard.")
                }
            } else {
                _errorFlow.emit("Veuillez remplir tous les champs.")
            }
        }
    }
}
