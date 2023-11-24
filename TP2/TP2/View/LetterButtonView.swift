//
//  LetterButtonView.swift
//  TP2
//
//  Created by user238613 on 11/21/23.
//

import SwiftUI

struct LetterButtonView: View {
    var letter: String
    var action: () -> Void

    var body: some View {
        Button(action: action) {
            Text(letter)
                .font(.title)
                .fontWeight(.medium)
                .padding()
                .frame(width: 45, height: 45)
                .background(Circle().fill(Color.blue))
                .foregroundColor(.white)
                .overlay(Circle().stroke(Color.blue.opacity(0.5), lineWidth: 1))
        }
    }
}
