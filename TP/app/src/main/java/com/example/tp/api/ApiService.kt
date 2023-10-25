package com.example.tp.api

import com.example.tp.data.model.DeckResponse
import com.example.tp.data.model.DrawCardResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("deck/new/shuffle/")
    suspend fun createNewDeck(): Response<DeckResponse>

    @GET("deck/{deck_id}/draw/")
    suspend fun drawCard(@Path("deck_id") deckId: String, @Query("count") count: Int = 1): Response<DrawCardResponse>

}
