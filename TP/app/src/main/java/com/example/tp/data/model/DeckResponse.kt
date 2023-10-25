package com.example.tp.data.model

data class DeckResponse(
    val success: Boolean,
    val deck_id: String,
    val shuffled: Boolean,
    val remaining: Int,
    var isFaceUp: Boolean = true
)