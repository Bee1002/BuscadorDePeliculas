package com.example.buscadordepeliculas

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.buscadordepeliculas.databinding.ActivityMovieDetailBinding
import com.example.buscadordepeliculas.viewmodel.MovieViewModel
import com.squareup.picasso.Picasso

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailBinding
    private val viewModel: MovieViewModel by viewModels()
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        val imdbId = intent.getStringExtra(EXTRA_IMDB_ID)
        if (!imdbId.isNullOrEmpty()) {
            viewModel.getMovieDetails(imdbId)
            observeViewModel()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.movie_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_favorite -> {
                isFavorite = !isFavorite
                item.setIcon(if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border)
                // TODO: Implementar lógica de favoritos
                true
            }
            R.id.action_share -> {
                shareMovie()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareMovie() {
        viewModel.selectedMovie.value?.let { movie ->
            val shareText = """
                ${movie.Title} (${movie.Year})
                
                Director: ${movie.Director}
                Actores: ${movie.Actors}
                
                Sinopsis:
                ${movie.Plot}
                
                IMDB Rating: ${movie.imdbRating}
            """.trimIndent()

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            startActivity(Intent.createChooser(shareIntent, "Compartir película"))
        }
    }

    private fun observeViewModel() {
        viewModel.selectedMovie.observe(this) { movie ->
            binding.apply {
                movieTitle.text = movie.Title
                movieReleaseDate.text = "Año: ${movie.Year}\nClasificación: ${movie.Rated}\nFecha de estreno: ${movie.Released}"
                movieRating.text = "IMDB Rating: ${movie.imdbRating}\nVotos: ${movie.imdbVotes}"
                movieOverview.text = """
                    Género: ${movie.Genre}
                    Director: ${movie.Director}
                    Escritores: ${movie.Writer}
                    Actores: ${movie.Actors}
                    
                    Sinopsis:
                    ${movie.Plot}
                    
                    Idioma: ${movie.Language}
                    País: ${movie.Country}
                    Premios: ${movie.Awards}
                """.trimIndent()

                Picasso.get()
                    .load(movie.Poster)
                    .fit()
                    .centerCrop()
                    .into(moviePoster)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                android.widget.Toast.makeText(this, it, android.widget.Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        const val EXTRA_IMDB_ID = "extra_imdb_id"
    }
} 