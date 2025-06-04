package com.example.buscadordepeliculas.api

import com.example.buscadordepeliculas.data.MovieDetail
import com.example.buscadordepeliculas.data.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET(".")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") query: String,
        @Query("type") type: String = "movie"
    ): MovieResponse

    @GET(".")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String,
        @Query("i") imdbId: String,
        @Query("plot") plot: String = "full"
    ): MovieDetail
} 