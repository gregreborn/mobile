package com.example.tp.data.repository

import com.example.tp.api.ApiService
import com.example.tp.data.db.dao.StatisticsDao
import com.example.tp.data.db.entities.Statistics
import com.example.tp.data.model.DeckResponse
import com.example.tp.data.model.DrawCardResponse
import retrofit2.Response
import android.util.Log

class GameRepository(private val apiService: ApiService, private val statisticsDao: StatisticsDao) {

    // Fetch a new deck from the API
    suspend fun fetchNewDeck(): Response<DeckResponse> {
        Log.d("GameRepository", "Fetching a new deck from the API...")
        val response = apiService.createNewDeck()
        if(response.isSuccessful) {
            Log.d("GameRepository", "Successfully fetched new deck with ID: ${response.body()?.deck_id}")
        } else {
            Log.e("GameRepository", "Failed to fetch new deck. Error: ${response.errorBody()?.string()}")
        }
        return response
    }

    // Draw a card from a given deck
    suspend fun drawCard(deckId: String): Response<DrawCardResponse> {
        Log.d("GameRepository", "Drawing a card from deck ID: $deckId...")
        val response = apiService.drawCard(deckId)
        if(response.isSuccessful) {
            Log.d("GameRepository", "Successfully drew card: ${response.body()?.cards?.first()?.code}")
        } else {
            Log.e("GameRepository", "Failed to draw card from deck $deckId. Error: ${response.errorBody()?.string()}")
        }
        return response
    }

    // Save game statistics to the database
    suspend fun saveStatistics(statistics: Statistics) {
        Log.d("GameRepository", "Saving statistics to the database...")
        try {
            statisticsDao.insert(statistics)
            Log.d("GameRepository", "Successfully saved statistics for game session: ${statistics.id}")
        } catch(e: Exception) {
            Log.e("GameRepository", "Error saving statistics. Exception: $e")
        }
    }

    // Retrieve all game statistics from the database
    suspend fun getAllStatistics(): List<Statistics> {
        Log.d("GameRepository", "Retrieving all game statistics from the database...")
        return try {
            val stats = statisticsDao.getAllStatistics()
            Log.d("GameRepository", "Successfully retrieved ${stats.size} game statistics records.")
            stats
        } catch(e: Exception) {
            Log.e("GameRepository", "Error retrieving game statistics. Exception: $e")
            emptyList()
        }
    }
}
