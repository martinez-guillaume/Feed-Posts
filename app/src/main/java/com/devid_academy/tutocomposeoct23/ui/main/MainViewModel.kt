package com.devid_academy.tutocomposeoct23.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.tutocomposeoct23.network.ApiInterface
import com.devid_academy.tutocomposeoct23.network.ArticleDto
import com.devid_academy.tutocomposeoct23.network.User
import com.devid_academy.tutocomposeoct23.network.apiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

    private val apiInterface: ApiInterface,
    private val user: User

) : ViewModel() {

    private val _navigateToLogin = MutableSharedFlow<Boolean>()
    val navigateToLogin = _navigateToLogin.asSharedFlow()

    private val _selectedCategoryIdStateFlow = MutableStateFlow(0)
    val selectedCategoryIdStateFlow: StateFlow<Int> = _selectedCategoryIdStateFlow.asStateFlow()

    private val _articlesListStateFlow = MutableStateFlow<List<ArticleDto>>(emptyList())
    val articlesListStateFlow: StateFlow<List<ArticleDto>> = _articlesListStateFlow.asStateFlow()

    private val _articleListFilteredStateFlow = MutableStateFlow<List<ArticleDto>>(emptyList())
    val articleListFilteredStateFlow: StateFlow<List<ArticleDto>> = _articleListFilteredStateFlow.asStateFlow()

    val currentUserId: Long
        get() = user.getUserIdFromPreferences()

    fun logoutUser() {
        viewModelScope.launch {
            try {
                user.clearToken()
                _navigateToLogin.emit(true)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Une erreur est survenue lors de la déconnexion.", e)
            }
        }
    }


    fun loadArticles() {
        viewModelScope.launch {
            try {
                val token = user.getTokenFromPreferences()
                if (!token.isNullOrEmpty()) {
                    val response = apiInterface.getArticles(token)
                    if (response?.isSuccessful == true) {
                        _articlesListStateFlow.value = response.body() ?: emptyList()
                        updateFilteredArticles(_selectedCategoryIdStateFlow.value)

                    } else {
                        Log.e(
                            "MainViewModel",
                            "Failed to load articles: ${response?.errorBody()?.string()}"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Exception in loadArticles: ${e.message}", e)
            }
        }
    }


    fun updateFilteredArticles(categoryId: Int) {
        _selectedCategoryIdStateFlow.value = categoryId
        _articleListFilteredStateFlow.value =
            if (categoryId == 0) {
                _articlesListStateFlow.value
            } else {
                _articlesListStateFlow.value.filter { it.categorie == categoryId }
            }
    }


    private val _currentArticleJson = MutableStateFlow<String?>(null)
    val currentArticleJson = _currentArticleJson.asStateFlow()

    fun getJsonFromArticle(
        articleDto: ArticleDto
    ) {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val articleDataJson = moshi.adapter(ArticleDto::class.java).toJson(articleDto)
        _currentArticleJson.value = articleDataJson
        Log.d("MainViewModel", "Article to edit: $articleDataJson")
    }


    private val _articleDeletionResult = MutableStateFlow<String?>(null)
    val articleDeletionResult: StateFlow<String?> = _articleDeletionResult

    suspend fun deleteArticle(
        articleId: Long,
        token: String
    ) {
        try {
            val response = apiService.deleteArticle(articleId, token)
            if (response != null && response.isSuccessful) {
                when (response.code()) {
                    201 -> _articleDeletionResult.value = "Article supprimé avec succès"
                    304 -> _articleDeletionResult.value = "Article non supprimé"
                    400 -> _articleDeletionResult.value = "Problème de paramètre dans la requête"
                    401 -> _articleDeletionResult.value = "Suppression non autorisée (mauvais token)"
                    503 -> _articleDeletionResult.value = "Erreur du serveur (erreur MySQL)"
                    else -> _articleDeletionResult.value = "Réponse inattendue du serveur"
                }
                loadArticles()
            } else {
                _articleDeletionResult.value = "Erreur de communication avec le serveur"
                loadArticles()
            }
        } catch (e: Exception) {
            _articleDeletionResult.value = "Erreur réseau ou serveur : ${e.message}"
            Log.e("DeleteViewModel", "Erreur dans deleteArticle", e)
            loadArticles()
        }
    }

    fun getToken(): String {
        return user.getTokenFromPreferences() ?: ""
    }


}