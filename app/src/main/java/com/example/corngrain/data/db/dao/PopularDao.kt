package com.example.corngrain.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.corngrain.data.db.entity.PopularEntity


@Dao
interface PopularDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(entries:List<PopularEntity>)

    @Query("select * from popular")
    fun getMoviesInLocalDatabase() : List<PopularEntity>

    @Query("select count(movieId) from popular")
    fun entriesInDatabase():Int
}