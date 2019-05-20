package com.example.corngrain.ui.main.movies

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.corngrain.R
import com.example.corngrain.data.network.response.movies.PlayingMovies
import com.example.corngrain.data.network.response.movies.PopularMovies
import com.example.corngrain.data.network.response.movies.TopRatedMovies
import com.example.corngrain.data.network.response.movies.UpcomingMovies
import com.example.corngrain.ui.base.ScopedFragment
import com.example.corngrain.ui.main.movies.adapters.*
import com.example.corngrain.utilities.GlideApp
import kotlinx.android.synthetic.main.movies_fragment.*
import kotlinx.android.synthetic.main.now_playing_movies.*
import kotlinx.android.synthetic.main.popular_layout.*
import kotlinx.android.synthetic.main.rated_movies.*
import kotlinx.android.synthetic.main.upcoming_movies.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class Movies : ScopedFragment(), KodeinAware {


    override val kodein: Kodein by closestKodein()
    private val factory by instance<MovieViewModelFactory>()


    private lateinit var viewModel: MoviesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.movies_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(MoviesViewModel::class.java)
        buildUI()
    }

    @Suppress("ReplaceGetOrSet")
    private fun buildUI() = launch {
        val playMovies = viewModel.loadMorePlayingMoviesAsync(1)
        val upcomingMovies = viewModel.loadMoreUpcomingMoviesAsync(1)
        val popularMovies = viewModel.loadMorePopularMoviesAsync(1)
        val topRatedMovies = viewModel.loadMoreRatedMoviesAsync(1)
        buildingUpcomingMovieUI(upcomingMovies)
        buildingPlayingMovies(playMovies)
        buildingTopRatedMoviesUI(topRatedMovies)
        buildingPopularMoviesUI(popularMovies)
        search_card_container.setOnClickListener { card ->
            toSearchScreen(card)
        }

    }


    private fun buildingPlayingMovies(playingMovies: LiveData<PlayingMovies>) {


        playingMovies.observe(this, Observer { playing ->
            if (playing == null) return@Observer
            loading_container.visibility = View.INVISIBLE
            view_container.visibility = View.VISIBLE
            now_playing_more.setOnClickListener {
                launch {
                    var currentPage = playing.page
                    if (currentPage < playing.totalPages) {
                        currentPage += 1
                        viewModel.loadMorePlayingMoviesAsync(currentPage)
                    }
                }
            }
            settingNormalRecyclerViewConfigs(
                this@Movies.context,
                playing.results.toAdapterItems(),
                now_playing_movies_list,
                LinearLayoutManager.HORIZONTAL
            ).setOnItemClickListener { item, view ->
                (item as PlayingAdapter).let { playing ->
                    toDetailScreen(playing.entry.id, view)
                }
            }


        })


    }


    private fun buildingUpcomingMovieUI(upcomingMovies: LiveData<UpcomingMovies>) {
        upcomingMovies.observe(this, Observer { upcomingData ->

            upcoming_more.setOnClickListener {
                launch {
                    var currentPage = upcomingData.page
                    if (currentPage < upcomingData.totalPages) {
                        currentPage += 1
                        viewModel.loadMoreUpcomingMoviesAsync(currentPage)
                    }
                }
            }
            GlideApp.with(this@Movies.context!!)
                .load(BASE_IMG_URL + upcomingData.results[0].posterPath)
                .into(upcoming_movies_first_img)
            upcoming_movies_first_img.setOnClickListener {
                toDetailScreen(upcomingData.results[0].id, it)
            }

            GlideApp.with(this@Movies.context!!)
                .load(BASE_IMG_URL + upcomingData.results[1].posterPath)
                .into(upcoming_movies_second_img)
            upcoming_movies_second_img.setOnClickListener {
                toDetailScreen(upcomingData.results[1].id, it)

            }
            val list: MutableList<UpcomingMovies.Result> = upcomingData.results.toMutableList()
            list.subList(0, 2).clear()
            settingNormalRecyclerViewConfigs(
                this@Movies.context!!
                , list.toUpcomingMoviesData(),
                upcoming_list,
                LinearLayoutManager.HORIZONTAL
            ).setOnItemClickListener { item, view ->
                (item as UpcomingAdapter).let { upcoming ->
                    toDetailScreen(upcoming.upcomingMovies.id, view)
                }
            }


        })


    }

    private fun buildingPopularMoviesUI(popularMovies: LiveData<PopularMovies>) {
        popularMovies.observe(this, Observer { data ->


            var currentPage = data.page
            if (currentPage < data.totalPages) {
                currentPage += 1
                popular_more.setOnClickListener {
                    launch {
                        viewModel.loadMorePopularMoviesAsync(currentPage)
                    }
                }
            }
            if (popular_list != null)
                settingNormalRecyclerViewConfigs(
                    this.context!!,
                    data.results.toPopularAdapterItems(),
                    popular_list,
                    RecyclerView.VERTICAL
                ).setOnItemClickListener { item, view ->
                    (item as PopularAdapter).let { popular ->
                        toDetailScreen(popular.entry.id, view)

                    }
                }

        })

    }

    @SuppressLint("SetTextI18n")
    private fun buildingTopRatedMoviesUI(topRatedData: LiveData<TopRatedMovies>) {
        topRatedData.observe(this, Observer { data ->
            val generatedNumber = generateRandomizedNumber()
            GlideApp.with(this.context!!)
                .load(BASE_IMG_URL + data.results[generatedNumber].backdropPath)
                .into(rated_movies_first_item)
            rated_movies_first_item.setOnClickListener {
                toDetailScreen(data.results[generatedNumber].id, it)
            }
            rated_movies_first_item_title.text = data.results[generatedNumber].title
            rated_movies_first_item_overview.setOnClickListener {
                toDetailScreen(data.results[generatedNumber].id, it)
            }


            rated_first_movie_bar.rating = data.results[generatedNumber].voteAverage.toFloat() / 2f
            rated_movies_first_item_overview.text = data.results[generatedNumber].overview
            val mutableList = data.results.toMutableList()
            mutableList.removeAt(generatedNumber)
            settingNormalRecyclerViewConfigs(
                this.context!!,
                mutableList.toTopRatedMoviesAdapterItems(),
                movies_rated_list,
                RecyclerView.HORIZONTAL
            ).setOnItemClickListener { item, view ->
                (item as TopRatedAdapter).let { singleItem ->
                    toDetailScreen(singleItem.entry.id, view)
                }
            }
            var currentPage = data.page
            if (currentPage < data.totalPages) {
                currentPage += 1
                rated_more.setOnClickListener {
                    launch {
                        viewModel.loadMoreRatedMoviesAsync(currentPage)
                    }
                }
            }
            val position = generateRandomizedNumber()
            val title = data.results[position].title
            val content = data.results[position].overview
            settingSharedData(title, content)
        })

    }

    private fun settingSharedData(title: String, content: String) {
        val preferences = context?.getSharedPreferences(
            "local_notification_data",
            Context.MODE_PRIVATE
        )
        val editor = preferences?.edit()
        editor?.putString("title", title)
        editor?.putString("content", content)
        editor?.apply()
    }

    private fun toDetailScreen(id: Int, viewClicked: View) {
        val actionWithValue = MoviesDirections.actionMoviesTabToMovieDetail(id)
        Navigation.findNavController(viewClicked).navigate(actionWithValue)

    }

    private fun toSearchScreen(viewClicked: View) {
        val actionToSearchScreen = MoviesDirections.actionSearchMovies()
        Navigation.findNavController(viewClicked).navigate(actionToSearchScreen)
    }

    private fun List<PlayingMovies.Result>.toAdapterItems(): List<PlayingAdapter> {
        return this.map { item ->
            PlayingAdapter(item)
        }
    }

    private fun List<UpcomingMovies.Result>.toUpcomingMoviesData(): List<UpcomingAdapter> {
        return this.map { item ->
            UpcomingAdapter(item)
        }
    }

    private fun List<PopularMovies.Result>.toPopularAdapterItems(): List<PopularAdapter> {
        return this.map { item ->
            PopularAdapter(item)

        }
    }

    private fun List<TopRatedMovies.Result>.toTopRatedMoviesAdapterItems(): List<TopRatedAdapter> {
        return this.map { item ->
            TopRatedAdapter(item)
        }
    }


}
