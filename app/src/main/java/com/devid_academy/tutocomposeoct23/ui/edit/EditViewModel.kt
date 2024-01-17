package com.devid_academy.tutocomposeoct23.ui.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.network.ApiInterface
import com.devid_academy.tutocomposeoct23.network.ArticleDto
import com.devid_academy.tutocomposeoct23.network.NewArticleDto
import com.devid_academy.tutocomposeoct23.network.UpdateArticleDto
import com.devid_academy.tutocomposeoct23.network.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel  @Inject constructor(

    private val apiService: ApiInterface,
    private val user: User

) : ViewModel() {

    private val _dataStateFlow = MutableStateFlow<ArticleDto?>(null)
    val dataStateFlow = _dataStateFlow.asStateFlow()

    fun getArticleFromJson(articleDto: String) {

        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val articleData = moshi.adapter(ArticleDto::class.java).fromJson(articleDto)

        _dataStateFlow.value = articleData

    }


    private val _articleEditionResult = MutableSharedFlow<String>(
        replay = 1,
        extraBufferCapacity = 1
    )
    val articleEditionResult = _articleEditionResult.asSharedFlow()

    fun editArticle(
        articleId: Long,
        title: String,
        content: String,
        picture: String,
        selectedCategory: Int
    ) {
        val token = user.getTokenFromPreferences()


        viewModelScope.launch {
            try {
                if (title.isEmpty() || content.isEmpty() || picture.isEmpty()) {
                    _articleEditionResult.emit("Veuillez remplir tous les champs")
                    return@launch
                }
                val updateArticleDto =
                    UpdateArticleDto(articleId, title, content, picture, selectedCategory)
                val response =
                    token?.let { apiService.updateArticle(articleId, it, updateArticleDto) }
                if (response != null && response.isSuccessful) {
                    when (response.code()) {
                        201 -> _articleEditionResult.emit("Article mis à jour avec succès")
                        303 -> _articleEditionResult.emit("Les IDs sont différents")
                        304 -> _articleEditionResult.emit("Article non mis à jour")
                        400 -> _articleEditionResult.emit("Problème de paramètre dans la requête")
                        401 -> _articleEditionResult.emit("Modification non autorisée (mauvais token)")
                        503 -> _articleEditionResult.emit("Erreur du serveur (erreur MySQL)")
                    }
                } else {
                    _articleEditionResult.emit("Erreur de communication avec le serveur")
                }
            } catch (e: Exception) {
                _articleEditionResult.emit("Erreur réseau ou serveur : ${e.message}")
                Log.e("EditViewModel", "Erreur dans editArticle", e)
            }
        }
    }
}

