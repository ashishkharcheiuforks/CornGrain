package com.example.corngrain.ui.main.series

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.corngrain.data.network.response.Credits
import com.example.corngrain.data.network.response.Reviews
import com.example.corngrain.data.network.response.Videos
import com.example.corngrain.data.network.response.series.SerieDetail
import com.example.corngrain.data.repository.series.SeriesRepository
import com.example.corngrain.utilities.lazyDeferred

class SeriesViewModel(private val repository: SeriesRepository) : ViewModel() {

    val fetchSeries by lazyDeferred {
        repository.getOnAirTodaySeries()
    }
    val fetchPopularSeries by lazyDeferred {
        repository.getPopularSeries()
    }

    val fetchTopRatedSeries by lazyDeferred {
        repository.getRatedSeries()
    }

    val fetchInViewSeries by lazyDeferred {
        repository.getInshowSeries()
    }

    suspend fun fetchDetails(id: Int): LiveData<SerieDetail> {
        val fetchDetail by lazyDeferred {
            repository.getSerieDetail(id)
        }
        return fetchDetail.await()
    }

    suspend fun fetchCredits(id: Int): LiveData<Credits> {
        val fetchCast by lazyDeferred {
            repository.getSerieCast(id)
        }
        return fetchCast.await()
    }
    suspend fun fetchReviews(id: Int): LiveData<Videos> {
        val reviews by lazyDeferred {
            repository.getSerieReviews(id)
        }
        return reviews.await()
    }

}
