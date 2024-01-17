package com.devid_academy.tutocomposeoct23.ui.register


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.network.ApiInterface
import com.devid_academy.tutocomposeoct23.network.RegisterDto
import com.devid_academy.tutocomposeoct23.network.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val apiInterface: ApiInterface,
    private val user: User
) : ViewModel() {

    private val _successFlow = MutableSharedFlow<String>()
    val successFlow = _successFlow.asSharedFlow()

    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow = _errorFlow.asSharedFlow()


    fun registerUser(
        login: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            if (login.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    try {
                        val response = apiInterface.registerUser(RegisterDto(login, password))
                        when (response?.code()) {
                            200 -> {
                                val responseBody = response.body()
                                val token = responseBody?.token.orEmpty()
                                val userId = responseBody?.id ?: 0L
                                user.saveToken(token)
                                user.saveUserId(userId)
                                _successFlow.emit("success")
                            }
                            303 -> _errorFlow.emit("Login déjà utilisé")
                            304 -> _errorFlow.emit("Nouveau compte non créé")
                            400 -> _errorFlow.emit("Problème de paramètre")
                            503 -> _errorFlow.emit("Erreur MySQL")
                            else -> _errorFlow.emit("Une erreur est survenue, veuillez réessayer plus tard.")
                        }
                    } catch (e: Exception) {
                        _errorFlow.emit("Une erreur est survenue, veuillez réessayer plus tard.")
                    }
                } else {
                    _errorFlow.emit("Les mots de passe ne correspondent pas.")
                }
            } else {
                _errorFlow.emit("Veuillez remplir tous les champs.")
            }
        }
    }
}