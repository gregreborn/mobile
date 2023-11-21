//
//  HangmanGame.swift
//  TP2
//
//  Created by user238613 on 11/21/23.
//

import Foundation

class HangmanGame {
    // The word that needs to be guessed
    var word: String = ""
    var secret: String = ""

    
    // The current state of the guessed word displayed to the user, initially filled with underscores
    var displayedWord: String {
        didSet {
            // Whenever displayedWord is updated, check for win condition
            if displayedWord == word {
                onWin?()
            }
        }
    }
    
    // The number of tries the player has to guess the word
    let maxTries: Int = 6
    var triesLeft: Int = 6
    
    // The letters that have been guessed so far
    var guessedLetters: [Character] = []
    
    // Callbacks for win and lose conditions
    var onWin: (() -> Void)?
    var onLose: (() -> Void)?
    
    // Initializes a new game with a word
    init(word: String, secret: String) {
            self.word = word
            self.secret = secret
            self.displayedWord = String(repeating: "_", count: word.count)
        }
    
    // Function to guess a letter
    func guess(letter: Character) {
        // Check if the letter has already been guessed
        guard !guessedLetters.contains(letter) else { return }
        
        // Add the letter to the list of guessed letters
        guessedLetters.append(letter)
        
        // Check if the word contains the letter
        if word.contains(letter) {
            // Reveal all occurrences of the letter in the displayedWord
            revealLetter(letter)
        } else {
            // Decrement the number of tries left
            triesLeft -= 1
            if triesLeft <= 0 {
                // If no tries left, trigger the lose condition
                onLose?()
            }
        }
    }
    
    // Function to reveal a letter in the displayedWord
    private func revealLetter(_ letter: Character) {
        // Update the displayedWord with the guessed letter
        for (index, wordLetter) in word.enumerated() {
            if wordLetter == letter {
                let startIndex = displayedWord.index(displayedWord.startIndex, offsetBy: index)
                displayedWord.replaceSubrange(startIndex...startIndex, with: String(letter))
            }
        }
    }
    
    // Function to check if the player has won
    func checkWinCondition() -> Bool {
        return word == displayedWord
    }
    
    // Function to start a new game with a new word
    func resetGame(with newWord: String) {
        word = newWord
        displayedWord = String(repeating: "_", count: newWord.count)
        triesLeft = maxTries
        guessedLetters = []
    }
}

