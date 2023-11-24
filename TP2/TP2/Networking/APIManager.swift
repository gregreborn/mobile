//
//  APIManager.swift
//  TP2
//
//  Created by user238613 on 11/21/23.
//

import Foundation

struct APIManager {

    let baseURL = "https://kubernetes.drynish.synology.me"
    
    // Fetch a new word and secret from the API
    func fetchNewWord(completion: @escaping (WordSecret?, Error?) -> Void) {
        let newWordURL = URL(string: "\(baseURL)/new")!
        
        URLSession.shared.dataTask(with: newWordURL) { data, response, error in
            if let error = error {
                print("API request error: \(error.localizedDescription)")
                completion(nil, error)
                return
            }
            
            guard let data = data else {
                print("API response data is nil")
                completion(nil, NSError(domain: "", code: -1, userInfo: nil))
                return
            }
            
            // Print raw response data
              ///print(String(data: data, encoding: .utf8) ?? "No data")
            
            do {
                let wordSecret = try JSONDecoder().decode(WordSecret.self, from: data)
                print("API response word: \(wordSecret.word)")
                print("API response secret: \(wordSecret.secret)")
                completion(wordSecret, nil)
            } catch {
                print("Error decoding API response: \(error.localizedDescription)")
                completion(nil, error)
            }
        }.resume()
    }
    
    // Send the player's name and score to the API
    func submitScore(word: String, secret: String, player: String, score: Int, completion: @escaping (Error?) -> Void) {
        let submitScoreURL = URL(string: "\(baseURL)/solve/\(word)/\(secret)/\(player)/\(score)")!
        
        var request = URLRequest(url: submitScoreURL)
        request.httpMethod = "POST"
        
        URLSession.shared.dataTask(with: request) { _, _, error in
            completion(error)
        }.resume()
    }
    
    // Fetch the high scores for a word from the API
    func fetchHighScores(forWord word: String, completion: @escaping ([HighScore]?, Error?) -> Void) {
        let highScoresURL = URL(string: "\(baseURL)/score/\(word)")!
        
        URLSession.shared.dataTask(with: highScoresURL) { data, response, error in
            if let error = error {
                completion(nil, error)
                return
            }
            
            guard let data = data else {
                completion(nil, NSError(domain: "", code: -1, userInfo: nil))
                return
            }
            
            do {
                let highScores = try JSONDecoder().decode([HighScore].self, from: data)
                completion(highScores, nil)
            } catch {
                completion(nil, error)
            }
        }.resume()
    }
}

struct WordSecret: Decodable {
    let word: String
    let secret: String

    enum CodingKeys: String, CodingKey {
        case word = "Word"
        case secret = "Secret"
    }
}


