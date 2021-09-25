package com.mineversal.githubuser.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteUser)

    @Update
    fun update(favoriteUser: FavoriteUser)

    @Delete
    fun delete(favoriteUser: FavoriteUser)

    @Query("SELECT count(*) FROM favoriteuser WHERE id = :id")
    fun checkUser(id: Int): Int

    @Query("SELECT * from favoriteuser ORDER BY id ASC")
    fun getAllFavUser(): LiveData<List<FavoriteUser>>
}