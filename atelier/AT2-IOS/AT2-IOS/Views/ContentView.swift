//
//  ContentView.swift
//  atelier 2 ios
//
//  Created by user238613 on 12/18/23.
//

import SwiftUI

struct ContentView: View {
    // Instantiate the ViewModel
    @ObservedObject var viewModel = BillViewModel()

    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("Bill Details")) {
                    TextField("Total Amount", text: $viewModel.totalAmount)
                        .keyboardType(.decimalPad)

                    Picker("Number of People", selection: $viewModel.numberOfPeople) {
                        ForEach(2..<9) {
                            Text("\($0) people")
                        }
                    }

                    Slider(value: $viewModel.tipPercentage, in: 5...30, step: 1)
                    Text("Tip Percentage: \(viewModel.tipPercentage, specifier: "%.0f")%")
                }

                Section(header: Text("Options")) {
                    Toggle(isOn: $viewModel.includeTaxes) {
                        Text("Include Taxes")
                    }
                }

                Section(header: Text("Total Per Person")) {
                    Text("$\(viewModel.totalPerPerson, specifier: "%.2f")")
                }
            }
            .navigationBarTitle("Restaurant Bill Calculator")
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
