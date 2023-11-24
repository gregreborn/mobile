//
//  ContentView.swift
//  TP2
//
//  Created by user238613 on 11/21/23.
//

import SwiftUI

struct ContentView: View {
    @StateObject var viewModel = HangmanViewModel()  // ViewModel instance
    @State private var showGameView = false  // State to control navigation

    var body: some View {
        NavigationView {
            VStack {
                TextField("Enter your name", text: $viewModel.playerName)  // Two-way binding to viewModel's playerName
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .padding()
                
                Button("Start Game") {
                    viewModel.startNewGame()  // Start the game
                    showGameView = true  // Trigger navigation
                }
                .disabled(viewModel.playerName.isEmpty)  // Disable button if no name
                .padding()
                
                NavigationLink(destination: HangmanView(viewModel: viewModel), isActive: $showGameView) {
                    EmptyView()  // Invisible view to trigger navigation
                }
            }
            .navigationBarTitle("Welcome to Hangman")
        }
    }
}


