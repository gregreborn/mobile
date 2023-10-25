package com.example.tp.data.model

import com.example.tp.data.model.Card
data class DrawCardResponse(
    val success: Boolean,
    val cards: List<Card>,
    val deck_id: String,
    val remaining: Int,
    var isFaceUp: Boolean = true
)
