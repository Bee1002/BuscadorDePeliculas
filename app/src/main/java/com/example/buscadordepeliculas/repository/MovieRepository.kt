package com.example.buscadordepeliculas.repository

import com.example.buscadordepeliculas.api.MovieApiService
import com.example.buscadordepeliculas.data.MovieDetail
import com.example.buscadordepeliculas.data.MovieResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieRepository {
    private val apiKey = "9dee3091"
    private val baseUrl = "https://www.omdbapi.com/"
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    private val apiService = retrofit.create(MovieApiService::class.java)
    
    suspend fun searchMovies(query: String): MovieResponse {
        return apiService.searchMovies(apiKey, query)
    }
    
    suspend fun getMovieDetails(imdbId: String): MovieDetail {
        return apiService.getMovieDetails(apiKey, imdbId)
    }
} 