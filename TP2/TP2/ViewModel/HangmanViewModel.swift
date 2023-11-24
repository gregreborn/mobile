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
    public var game: HangmanGame?
    
    // Use @Published to automatically update the view when these properties change
    @Published var displayedWord: String
    @Published var triesLeft: Int
    @Published var guessedLetters: [Character]
    @Published var gameStatus: GameStatus = .ongoing
    @Published var errorMessage: String?
    @Published var highScores: [HighScore] = []
    @Published var gameHasEnded = false
    @Published var playerName: String = ""  // Player's name
    @Published var successMessage: String? = nil
    @Published var showHighScores = false


    


    
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
            
            
            self.displayedWord = game?.displayedWord ?? ""
            self.triesLeft = game?.maxTries ?? 0
            self.guessedLetters = game?.guessedLetters ?? []
            game?.onLose = { [weak self] in
                    self?.handleGameLost()
                }
            }
    
    // Function to start a new game
    func startNewGame() {
        gameHasEnded = false
            showHighScores = false
            errorMessage = nil
        print("startNewGame() called") // Debug statement

        apiManager.fetchNewWord { [weak self] wordSecret, error in
            DispatchQueue.main.async {
                if let wordSecret = wordSecret {
                    self?.game = HangmanGame(word: wordSecret.word, secret: wordSecret.secret)
                    self?.updatePublishedProperties()
                    
                    // Print the fetched word to the Xcode console
                    if let gameWord = self?.game?.word {
                        print("Fetched word from API: \(gameWord)")
                    }
                    
                } else if let error = error {
                    self?.errorMessage = error.localizedDescription
                }
            }
        }
    }

    private func handleGameWon() {
            DispatchQueue.main.async { [weak self] in
                guard let self = self else { return }
                self.gameStatus = .won
                self.gameHasEnded = true
                self.showHighScores = true
            }
        }

        private func handleGameLost() {
            DispatchQueue.main.async { [weak self] in
                guard let self = self else { return }
                self.gameStatus = .lost
                self.gameHasEnded = true
                // Handle any additional logic for when the game is lost, if necessary
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
                self.submitScore(playerName: self.playerName)
                self.handleGameWon()
            } else if game.triesLeft == 0 {
                gameStatus = .lost
                self.handleGameLost()
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
                    self?.errorMessage = "Failed to submit score: \(error.localizedDescription)"
                    // Here you might want to show an alert to the user to inform them of the error.
                } else {
                    // The score was successfully submitted.
                    self?.successMessage = "Score submitted successfully!"
                    // You could navigate to the high scores view, or fetch the latest high scores to update the view.
                    self?.fetchHighScores(forWord: game.word)
                }
            }
        }
    }

}
