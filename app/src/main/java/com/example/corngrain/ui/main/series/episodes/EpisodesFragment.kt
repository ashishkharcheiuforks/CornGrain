package com.example.corngrain.ui.main.series.episodes

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView

import com.example.corngrain.R
import com.example.corngrain.data.network.response.series.Season
import com.example.corngrain.ui.base.ScopedFragment
import com.example.corngrain.ui.main.series.episodes.adapter.EpisodesAdapter
import com.example.corngrain.utilities.BASE_IMG_URL
import com.example.corngrain.utilities.GlideApp
import com.example.corngrain.utilities.normalRecyclerView
import kotlinx.android.synthetic.main.episodes_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory2

class EpisodesFragment : ScopedFragment(), KodeinAware {


    override val kodein: Kodein by closestKodein()
    private val viewmodelInstanceFactory: ((Int, Int) -> EpisodesViewmodelFactory) by factory2()

    private lateinit var viewModel: EpisodesViewModel
    private var title: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.episodes_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val episodeArguements = arguments?.let { bundle ->
            EpisodesFragmentArgs.fromBundle(bundle)

        }
        val seasonId = episodeArguements?.id
        val seasonNumber = episodeArguements?.seasonNumber
        title = episodeArguements?.title

        if (seasonId != null && seasonNumber != null)
            viewModel =
                ViewModelProviders.of(this, viewmodelInstanceFactory(seasonId, seasonNumber))
                    .get(EpisodesViewModel::class.java)

        launch {
            val seasonJob = viewModel.seasonData.await()
            buildUI(seasonJob)
        }
    }

    private fun buildUI(data: LiveData<Season>) {

        data.observe(this@EpisodesFragment, Observer { seasonData ->
            if (seasonData == null) return@Observer
            group_loading.visibility = View.INVISIBLE
            views_container.visibility = View.VISIBLE
            GlideApp.with(this@EpisodesFragment)
                .load(BASE_IMG_URL + seasonData.posterPath)
                .into(season_img)
            serie_title.text = title!!
            season_number.text = seasonData.name
            season_air_date.text = seasonData.airDate
            season_overview.text = seasonData.overview

            val episodesAdapter = EpisodesAdapter(seasonData.episodes[0])
            normalRecyclerView(
                context!!, episodesAdapter.toGroupeAdapterItems(seasonData.episodes)
                , RecyclerView.HORIZONTAL, episodes_list
            )

        })
    }


}
