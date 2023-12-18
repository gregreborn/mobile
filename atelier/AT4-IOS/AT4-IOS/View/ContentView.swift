import SwiftUI

struct ContentView: View {
    @StateObject var viewModel = WeatherViewModel()

    var body: some View {
        VStack {
            if let weather = viewModel.weatherData {
                Text("Weather in \(weather.name)")
                Text("\(weather.main.temp)°C")
                ForEach(weather.weather, id: \.main) { weatherDetail in
                    Text(weatherDetail.main)
                    Text(weatherDetail.description)
                }
            }
        }
        .onAppear {
            viewModel.requestLocation()
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
