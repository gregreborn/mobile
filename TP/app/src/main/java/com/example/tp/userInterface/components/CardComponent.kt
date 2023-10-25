package com.example.tp.userInterface.components

import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.example.tp.data.model.Card as GameCard // Renamed to avoid naming conflict
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardElevation
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.tp.data.model.CardImages

@Composable
fun CardComponent(card: GameCard, isFaceUpOverride: Boolean? = null) {
    val isFaceUp = isFaceUpOverride ?: card.isFaceUp // Use the override value if provided, otherwise use the card's state

    Card(modifier = Modifier.size(110.dp, 160.dp)) {
        val imageUrl = if (!isFaceUp) card.image else "https://www.deckofcardsapi.com/static/img/back.png"
        val painter = rememberAsyncImagePainter(model = imageUrl)

        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(100.dp, 150.dp)
        )
    }
}



@Composable
fun CardStack(cards: List<GameCard>) {
    Row(
        modifier = Modifier.padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(-80.dp)  // This will cause the cards to overlap
    ) {
        cards.forEach { card ->
            CardComponent(card)
        }
    }
}


@Preview
@Composable
fun PreviewCardComponent() {
    val mockCard = GameCard(
        code = "6H",
        image = "https://deckofcardsapi.com/static/img/6H.png",
        images = CardImages(
            svg = "https://deckofcardsapi.com/static/img/6H.svg",
           png = "https://deckofcardsapi.com/static/img/6H.png"
        ),
        value = "6",
        suit = "HEARTS"
    )
    CardComponent(card = mockCard)
}
