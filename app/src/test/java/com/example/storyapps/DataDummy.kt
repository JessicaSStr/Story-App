package com.example.storyapps

import com.example.storyapps.remote.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val stories = ListStoryItem(
                i.toString(),
                createdAt = "2023-11-08T12:00:00",
                name = "Story $i",
                id = "ID_$i",
                description = "Description $i",
                lon = 0.0,
                lat = 0.0,
            )
            items.add(stories)
        }
        return items
    }
}