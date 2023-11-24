//
//  HangmanView.swift
//  TP2
//
//  Created by user238613 on 11/21/23.
//

import SwiftUI

struct HangmanView: View {
    @ObservedObject var viewModel: HangmanViewModel

    let columns = [
        GridItem(.adaptive(minimum: 40))
    ]
    
    var body: some View {
        VStack {
            // Display the current state of the word
            Text(viewModel.displayedWord)
                .font(.title)
                .kerning(2)
                .padding()

            // Display the hangman image (use your own image asset)
            Image("stage-\((viewModel.game?.maxTries ?? 6) - viewModel.triesLeft)")
                .resizable()
                .scaledToFit()
                .frame(height: 200)
                .padding()


            // Display the alphabet buttons
            LazyVGrid(columns: columns, spacing: 20) {
                ForEach(Array("ABCDEFGHIJKLMNOPQRSTUVWXYZ"), id: \.self) { letter in
                    LetterButtonView(letter: String(letter)) {
                        viewModel.makeGuess(letter: letter)
                    }
                    .disabled(viewModel.guessedLetters.contains(letter))
                }
            }
            .padding()
            
            .alert(isPresented: $viewModel.gameHasEnded) {
                   Alert(
                       title: Text("Game Over"),
                       message: Text(viewModel.gameStatus == .won ? "Congratulations, you won!" : "Sorry, you lost!"),
                       dismissButton: .default(Text("Play Again")) {
                           viewModel.startNewGame() // Restart the game
                       }
                   )
               }
            NavigationLink(destination: HighScoreView(viewModel: viewModel), isActive: $viewModel.showHighScores) {
                           EmptyView()
                       }
        }
    }
}
