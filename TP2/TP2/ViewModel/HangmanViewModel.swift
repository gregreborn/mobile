//
//  HangmanViewModel.swift
//  TP2
//
//  Created by user238613 on 11/21/23.
//

import Foundation
import SwiftUI
import Combine

class HangmanViewModel: ObservableObject {
    // Reference to the Hangman game logic
    private var game: HangmanGame?
    
    // Use @Published to automatically update the view when these properties change
    @Published var displayedWord: String
    @Published var triesLeft: Int
    @Published var guessedLetters: [Character]
    @Published var gameStatus: GameStatus = .ongoing
    @Published var errorMessage: String?
    @Published var highScores: [HighScore] = []
    


    
    // API Manager for network requests
    private var apiManager = APIManager()
    
    // Enum to represent the status of the game
    enum GameStatus {
        case ongoing, won, lost
    }
    
    init() {
            // Create a new game instance with placeholder values
            let placeholderWord = "placeholder"
            let placeholderSecret = "secret"
            self.game = HangmanGame(word: placeholderWord, secret: placeholderSecret)
            
            // Now that game is guaranteed to be initialized, we can access its properties
            self.displayedWord = game?.displayedWord ?? ""
            self.triesLeft = game?.maxTries ?? 0
            self.guessedLetters = game?.guessedLetters ?? []
            
            // Set up the win and lose callbacks
            // These need to be set up after game has been initialized
            game?.onWin = { [weak self] in
                self?.gameStatus = .won
                // Logic for when the game is won
            }
            
            game?.onLose = { [weak self] in
                self?.gameStatus = .lost
                // Logic for when the game is lost
            }
        }
    
    // Function to start a new game
    func startNewGame() {
        apiManager.fetchNewWord { [weak self] wordSecret, error in
            DispatchQueue.main.async {
                if let wordSecret = wordSecret {
                    self?.game = HangmanGame(word: wordSecret.word, secret: wordSecret.secret)
                    self?.updatePublishedProperties()
                } else if let error = error {
                    self?.errorMessage = error.localizedDescription
                }
            }
        }
    }
    
    // Function to fetch high scores for a word
    func fetchHighScores(forWord word: String) {
        apiManager.fetchHighScores(forWord: word) { [weak self] result, error in
            DispatchQueue.main.async {
                if let highScores = result {
                    self?.highScores = highScores
                } else {
                    self?.errorMessage = error?.localizedDescription ?? "Unknown error"
                }
            }
        }
    }


    
    func clearErrorMessage() {
            errorMessage = nil
        }
    
    // Function to make a guess
    func makeGuess(letter: Character) {
        // Safely unwrap 'game' before using it
        if let game = game {
            game.guess(letter: letter)
            updatePublishedProperties()
            
            // Check for game status updates
            if game.checkWinCondition() {
                gameStatus = .won
                // You might want to submit the score here if the game is won
            } else if game.triesLeft == 0 {
                gameStatus = .lost
            }
        }
    }

    // Function to update the view model's published properties to reflect the game's state
    private func updatePublishedProperties() {
        // Safely unwrap 'game' before using its properties
        if let game = game {
            displayedWord = game.displayedWord
            triesLeft = game.triesLeft
            guessedLetters = game.guessedLetters
        } else {
            // Handle the case where 'game' is nil if needed
        }
    }

    private func submitScore(playerName: String) {
        guard gameStatus == .won, let game = game else { return }

        let score = game.maxTries - game.triesLeft
        apiManager.submitScore(word: game.word, secret: game.secret, player: playerName, score: score) { [weak self] error in
            DispatchQueue.main.async {
                if let error = error {
                    self?.errorMessage = error.localizedDescription
                    // Handle the error
                } else {
                    // Success logic
                }
            }
        }
    }


}
