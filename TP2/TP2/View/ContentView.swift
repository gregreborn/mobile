//
//  ContentView.swift
//  TP2
//
//  Created by user238613 on 11/21/23.
//

import SwiftUI

struct ContentView: View {
    @StateObject var viewModel = HangmanViewModel()
    @State private var showGameView = false

    var body: some View {
        NavigationView {
            VStack {
                TextField("Enter your name", text: $viewModel.playerName)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .padding()
                
                Button("Start Game") {
                    viewModel.startNewGame()
                    showGameView = true
                }
                .disabled(viewModel.playerName.isEmpty)
                .padding()
                
                

                NavigationLink(destination: HangmanView(viewModel: viewModel), isActive: $showGameView) {
                    EmptyView()
                }
                
            
            }
            .navigationBarTitle("Hangman")
        }
    }
}


