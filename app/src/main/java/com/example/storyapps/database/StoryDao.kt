package com.example.storyapps.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapps.remote.response.ListStoryItem

@Dao
interface StoryDao {
    @Query("SELECT * FROM stories")
    fun getStories(): PagingSource<Int,ListStoryItem>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(stories: List<ListStoryItem>)
}