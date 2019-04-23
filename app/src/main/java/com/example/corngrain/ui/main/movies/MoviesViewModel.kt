package com.example.corngrain.ui.main.movies

import androidx.lifecycle.ViewModel;
import com.example.corngrain.data.repository.di.TmdbRepository
import com.example.corngrain.utilities.lazyDeferred

class MoviesViewModel(private val repository: TmdbRepository) : ViewModel() {
    val fetchLatestMovies by lazyDeferred {
        repository.getPopularMovies()
    }
    val fetchUpcomingMovies by lazyDeferred {
        repository.getUpcomingMovies()
    }

}
