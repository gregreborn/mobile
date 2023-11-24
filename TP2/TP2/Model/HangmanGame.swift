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
        self.onWin = {
                   print("You won")
               }
        }
    
    // Function to guess a letter
    func guess(letter: Character) {
        let lowercasedLetter = Character(letter.lowercased())
        guard !guessedLetters.contains(lowercasedLetter) else { return }
        
        guessedLetters.append(lowercasedLetter)
        
        if word.lowercased().contains(lowercasedLetter) {
            revealLetter(lowercasedLetter)
        } else {
            triesLeft -= 1
            if triesLeft <= 0 {
                onLose?()
            }
        }
    }

    
    private func revealLetter(_ letter: Character) {
        var newDisplayedWord = displayedWord
        for (index, wordLetter) in word.enumerated() {
            if wordLetter.lowercased() == letter.lowercased() {
                let startIndex = newDisplayedWord.index(newDisplayedWord.startIndex, offsetBy: index)
                newDisplayedWord.replaceSubrange(startIndex...startIndex, with: String(letter))
            }
        }
        displayedWord = newDisplayedWord
        // Check if the word has been fully guessed
        if triesLeft <= 0 {
                onLose?()
            } else if checkWinCondition() {
                onWin?()
            }
        print("Updated displayedWord: \(displayedWord)")
    }

    
    // Function to check if the player has won
    func checkWinCondition() -> Bool {
        let hasWon = word.lowercased() == displayedWord.lowercased()
        if hasWon {
            print("Win condition met: Player has guessed the word correctly.")
        }
        return hasWon
    }



    // Function to start a new game with a new word
    func resetGame(with newWord: String) {
        word = newWord
        displayedWord = String(repeating: "_", count: newWord.count)
        triesLeft = maxTries
        guessedLetters = []
    }
}

