//
//  HighScoreView.swift
//  TP2
//
//  Created by user238613 on 11/21/23.
//

import SwiftUI

struct HighScoreView: View {
    @ObservedObject var viewModel: HangmanViewModel
    @State private var searchWord = ""
    @State private var isSearching = false

    var body: some View {
        VStack {
            HStack {
                TextField("Rechercher un mot", text: $searchWord)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                
                if isSearching {
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle())
                } else {
                    Button("Search") {
                        isSearching = true
                        viewModel.fetchHighScores(forWord: searchWord)
                    }
                }
            }
            .padding()

            if viewModel.highScores.isEmpty && !isSearching {
                Text("No high scores to display.")
                    .foregroundColor(.secondary)
            } else {
                List(viewModel.highScores) { highScore in
                    HStack {
                        Text(highScore.player)
                        Spacer()
                        Text("\(highScore.score)")
                    }
                }
            }
        }
        .navigationBarTitle("High Scores", displayMode: .inline)
        .onReceive(viewModel.$highScores) { _ in
            isSearching = false
        }
    }
}


