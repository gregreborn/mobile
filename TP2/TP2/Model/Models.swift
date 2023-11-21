//
//  Models.swift
//  TP2
//
//  Created by user238613 on 11/21/23.
//

import Foundation

// Models.swift

import Foundation

struct HighScore: Identifiable, Decodable {
    let id: UUID = UUID()
    let player: String
    let score: Int
}
