package com.example.tp.data.model

data class Card(
    val image: String,      // URL of the card image (for backward compatibility or direct access)
    val images: CardImages, // Contains both SVG and PNG URLs
    val value: String,      // Value of the card (e.g., "2", "KING", "ACE")
    val suit: String,       // Suit of the card (e.g., "HEARTS", "SPADES")
    val code: String,        // Code of the card (e.g., "KH", "AS")
    var isFaceUp: Boolean = true
)

data class CardImages(
    val svg: String,
    val png: String
)
