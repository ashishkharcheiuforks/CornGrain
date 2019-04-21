package com.example.corngrain.data.network.response

import com.google.gson.annotations.SerializedName

data class LatestMovies(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: Any,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: Any,
    @SerializedName("budget")
    val budget: Int,
    @SerializedName("genres")
    val genres: List<Any>,
    @SerializedName("homepage")
    val homepage: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imdb_id")
    val imdbId: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("popularity")
    val popularity: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("production_companies")
    val productionCompanies: List<Any>,
    @SerializedName("production_countries")
    val productionCountries: List<Any>,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("revenue")
    val revenue: Int,
    @SerializedName("runtime")
    val runtime: Int,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,
    @SerializedName("status")
    val status: String,
    @SerializedName("tagline")
    val tagline: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Int,
    @SerializedName("vote_count")
    val voteCount: Int
) {
    data class SpokenLanguage(
        @SerializedName("iso_639_1")
        val iso6391: String,
        @SerializedName("name")
        val name: String
    )
}