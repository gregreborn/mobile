package com.example.tp.utils

import com.example.tp.data.model.Card

object GameLogic {

    fun calculateSum(cards: List<Card>): Int {
        var sum = 0
        var aceCount = 0
        for (card in cards) {
            when (card.value) {
                "ACE" -> {
                    sum += 11
                    aceCount += 1
                }
                "KING", "QUEEN", "JACK", "10" -> sum += 10
                else -> sum += card.value.toInt()
            }
        }

        while (sum > 21 && aceCount > 0) {
            sum -= 10
            aceCount -= 1
        }

        return sum
    }

    fun isBlackjack(cards: List<Card>): Boolean {
        if (cards.size != 2) return false
        val values = cards.map { it.value.toIntOrNull() ?: 10 }
        return values.contains(1) && values.sum() == 11
    }

    fun isBust(cards: List<Card>): Boolean {
        return calculateSum(cards) > 21
    }

}
