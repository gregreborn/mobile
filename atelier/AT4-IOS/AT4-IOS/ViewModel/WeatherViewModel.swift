import Foundation
import CoreLocation

class WeatherViewModel: NSObject, ObservableObject, CLLocationManagerDelegate {
    @Published var weatherData: WeatherData?
    private let locationManager = CLLocationManager()
    private let apiKey = "60d0eb7dbaf2e76cb76df9fbd9fbf3d9" //votre cle d'API openweather
    
    override init() {
        super.init()
        locationManager.delegate = self
    }
    
    func requestLocation() {
        locationManager.requestWhenInUseAuthorization()
        locationManager.requestLocation()
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.first {
            fetchWeather(for: location)
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print(error)
    }
    
    private func fetchWeather(for location: CLLocation) {
        let urlString = "https://api.openweathermap.org/data/2.5/weather?lat=\(location.coordinate.latitude)&lon=\(location.coordinate.longitude)&appid=\(apiKey)&units=metric"
        
        guard let url = URL(string: urlString) else { return }
        
        URLSession.shared.dataTask(with: url) { data, response, error in
            if let data = data {
                if let response = try? JSONDecoder().decode(WeatherData.self, from: data) {
                    DispatchQueue.main.async {
                        self.weatherData = response
                    }
                }
            }
        }.resume()
    }
}
