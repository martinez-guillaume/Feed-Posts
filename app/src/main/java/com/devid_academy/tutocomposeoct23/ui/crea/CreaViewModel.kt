package com.devid_academy.tutocomposeoct23.ui.crea

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.network.ApiInterface
import com.devid_academy.tutocomposeoct23.network.NewArticleDto
import com.devid_academy.tutocomposeoct23.network.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreaViewModel @Inject constructor(

    private val apiService: ApiInterface,
    private val user: User

) : ViewModel() {

    private val _articleCreationResult = MutableSharedFlow<String>()
    val articleCreationResult = _articleCreationResult.asSharedFlow()

    fun createArticle(
        title: String,
        content: String,
        picture: String,
        selectedCategory: Int
    ) {

        val token = user.getTokenFromPreferences()
        val idUser = user.getUserIdFromPreferences()


        viewModelScope.launch {
            try {
                if (title.isEmpty() || content.isEmpty()) {
                    _articleCreationResult.emit("Veuillez remplir tous les champs")
                    return@launch
                }
                if(title.length > 80){
                    _articleCreationResult.emit("Le titre ne peut dépasser 80 charactères")
                    return@launch
                }

                val response = token?.let { apiService.createArticle(it, newArticleDto = NewArticleDto(idUser,title,content,picture,selectedCategory) ) }
                when (response?.code()) {
                    201 -> _articleCreationResult.emit("Article créé avec succès")
                    304 -> _articleCreationResult.emit("Article non créé")
                    400 -> _articleCreationResult.emit("Problème de paramètre dans la requête")
                    401 -> _articleCreationResult.emit("Création non autorisée (mauvais token)")
                    503 -> _articleCreationResult.emit("Erreur du serveur (erreur MySQL)")
                    else -> _articleCreationResult.emit("Erreur inconnue: ${response?.code()}")
                }
            } catch (e: Exception) {
                _articleCreationResult.emit("Erreur réseau ou serveur : ${e.message}")
                Log.e("MainViewModel", "Erreur dans mainArticle", e)
            }
        }
    }
}