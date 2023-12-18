import Foundation

struct WeatherData: Codable {
    let main: Main
    let weather: [Weather]
    let name: String

    struct Main: Codable {
        let temp: Double
    }
    
    struct Weather: Codable {
        let main: String
        let description: String
    }
}
