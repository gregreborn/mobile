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

            if let highScores = viewModel.highScoreResponse?.list, !highScores.isEmpty {
                List(highScores) { highScore in
                    HStack {
                        Text(highScore.player)
                        Spacer()
                        Text("\(highScore.score)")
                    }
                }
            } else if !isSearching {
                Text("No high scores to display.")
                    .foregroundColor(.secondary)
            }
        }
        .navigationBarTitle("High Scores", displayMode: .inline)
        .navigationBarBackButtonHidden(true) // Hide the back button
        .onReceive(viewModel.$highScoreResponse) { _ in
            isSearching = false
        }
    }
}



