package com.example.corngrain.data.network.outsource

import androidx.lifecycle.LiveData
import com.example.corngrain.data.network.response.Detail
import com.example.corngrain.data.network.response.Reviews
import com.example.corngrain.data.network.response.movies.*
import com.example.corngrain.data.network.response.people.PersonDetail
import com.example.corngrain.data.network.response.people.PersonMovies
import com.example.corngrain.data.network.response.people.PopularPersons
import com.example.corngrain.data.network.response.series.*

interface TmdbNetworkLayer {
    //PopularPersons Movies
    val popularMovies: LiveData<PopularMovies>

    suspend fun loadPopularMovies()

    //Upcoming
    val upcomingMovies: LiveData<UpcomingMovies>

    suspend fun loadUpcomingMovies()

    //TopRated
    val topRatedMovies: LiveData<TopRatedMovies>

    suspend fun loadTopRatedMovies()

    //Playing
    val playingMovies: LiveData<PlayingMovies>

    suspend fun loadPlayingMovies()


    //Detail
    val movieDetail:LiveData<Detail>
    suspend fun loadMovieDetail(id:Int)


    //Cast
    val movieCast:LiveData<MovieCredits>
    suspend fun loadMovieCast(id:Int)

    //Reviews
    val reviews :LiveData<Reviews>
    suspend fun loadReviews(id:Int)

    //SERIES => ON AIR TODAY
    val onAirToday: LiveData<OnAirToday>

    suspend fun loadOnAirToday()

    //SERIES => POPULAR
    val popularSeries: LiveData<PopularSeries>

    suspend fun loadPopularSeries()

    val serieDetail: LiveData<SerieDetail>
    suspend fun loadSerieDetail(id: Int)

    val topRatedSeries: LiveData<TopRatedSeries>
    suspend fun loadTopRatedSeries()

    val currentlyViewingSeries: LiveData<SerieCurrentlyShowing>
    suspend fun loadInshowSeries()


    //PERSONS
    val popularPersons: LiveData<PopularPersons>

    suspend fun loadPopularPersons()

    val personDetail: LiveData<PersonDetail>
    suspend fun loadPersonDetail(id: Int)

    val personMovies: LiveData<PersonMovies>
    suspend fun loadPersonMovies(id: Int)
}