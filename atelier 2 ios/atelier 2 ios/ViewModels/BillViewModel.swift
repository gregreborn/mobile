//
//  BillViewModel.swift
//  atelier 2 ios
//
//  Created by user238613 on 12/18/23.
//

import Foundation
import Combine

class BillViewModel: ObservableObject {
    // Published properties to be bound to the UI
    @Published var totalAmount: String = ""
    @Published var numberOfPeople: Int = 2
    @Published var tipPercentage: Double = 15.0
    @Published var includeTaxes: Bool = true

    // Instance of BillCalculator
    private var billCalculator = BillCalculator()

    // Calculated property for total per person
    var totalPerPerson: Double {
        guard let amount = Double(totalAmount), numberOfPeople > 0 else {
            return 0
        }
        return billCalculator.calculateTotalAmount(withSubtotal: amount, numberOfPeople: numberOfPeople, tipPercentage: tipPercentage, includeTaxes: includeTaxes)
    }

    // Constants and additional properties
    private let taxRate = 0.2 // Assuming a 20% tax rate
    var isValidInput: Bool {
        return Double(totalAmount) != nil && numberOfPeople > 0
    }

    // Initialize with default values or specific settings
    init() {
        // Initialization code or setup if needed
    }

    // Additional methods for handling specific logic
    // ...
}
