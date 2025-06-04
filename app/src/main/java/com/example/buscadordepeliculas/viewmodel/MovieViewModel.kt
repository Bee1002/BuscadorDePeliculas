package com.example.buscadordepeliculas.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscadordepeliculas.data.Movie
import com.example.buscadordepeliculas.data.MovieDetail
import com.example.buscadordepeliculas.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val repository = MovieRepository()
    
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies
    
    private val _selectedMovie = MutableLiveData<MovieDetail>()
    val selectedMovie: LiveData<MovieDetail> = _selectedMovie
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.searchMovies(query)
                if (response.Response == "True") {
                    _movies.value = response.Search
                    _error.value = null
                } else {
                    _error.value = "No se encontraron resultados"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun getMovieDetails(imdbId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val movie = repository.getMovieDetails(imdbId)
                if (movie.Response == "True") {
                    _selectedMovie.value = movie
                    _error.value = null
                } else {
                    _error.value = "No se encontró la película"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
} 