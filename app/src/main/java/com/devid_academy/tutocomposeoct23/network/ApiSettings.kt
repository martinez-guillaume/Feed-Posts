package com.devid_academy.tutocomposeoct23.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

object ApiRoutes {
    const val BASE_URL = "https://dev.dev-id.fr/formation/api_pro/"
    const val REGISTER_USER = "articles/user/"
    const val LOGIN_USER = "articles/user/"
    const val GET_ARTICLES = "articles/"
    const val GET_ARTICLE = "articles/{id}"
    const val POST_ARTICLE = "articles/{id}"
    const val DELETE_ARTICLE = "articles/{id}"
    const val PUT_ARTICLE = "articles/"
}

private fun getClient(): Retrofit {
    val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    val moshi = Moshi.Builder().apply {
        add(KotlinJsonAdapterFactory())
    }.build()

    return Retrofit.Builder()
        .baseUrl(ApiRoutes.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()
}
val apiService: ApiInterface = getClient().create(ApiInterface::class.java)


interface ApiInterface {
    @PUT(ApiRoutes.REGISTER_USER)
    suspend fun registerUser(@Body registerDto: RegisterDto): Response<ApiResponse>?

    @FormUrlEncoded
    @POST(ApiRoutes.LOGIN_USER)
    suspend fun loginUser(
        @Field("login") login: String,
        @Field("mdp") password: String
    ): Response<ApiResponse>?

    @GET(ApiRoutes.GET_ARTICLES)
    suspend fun getArticles(
        @Header("token") token: String
    ): Response<List<ArticleDto>>?

    @GET(ApiRoutes.GET_ARTICLE)
    suspend fun getArticle(
        @Path("id") articleId: Long,
        @Header("token") token: String
    ): Response<ArticleDto>?

    @POST(ApiRoutes.POST_ARTICLE)
    suspend fun updateArticle(
        @Path("id") articleId: Long,
        @Header("token") token: String,
        @Body updateArticleDto: UpdateArticleDto
    ): Response<Unit>?

    @DELETE(ApiRoutes.DELETE_ARTICLE)
    suspend fun deleteArticle(
        @Path("id") articleId: Long,
        @Header("token") token: String
    ): Response<Unit>?

    @PUT(ApiRoutes.PUT_ARTICLE)
    suspend fun createArticle(
        @Header("token") token: String,
        @Body newArticleDto: NewArticleDto
    ): Response<Unit>?
}



