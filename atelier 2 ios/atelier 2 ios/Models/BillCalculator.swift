//
//  BillCalculator.swift
//  atelier 2 ios
//
//  Created by user238613 on 12/18/23.
//

import Foundation

struct BillCalculator {
    // Constants for tax and tip calculations, if needed
    private let taxRate = 0.2 // Example tax rate of 20%

    // Function to calculate the total amount including tip and optionally tax
    func calculateTotalAmount(withSubtotal subtotal: Double, numberOfPeople: Int, tipPercentage: Double, includeTaxes: Bool) -> Double {
        let tipAmount = subtotal * tipPercentage / 100
        let taxAmount = includeTaxes ? subtotal * taxRate : 0
        let totalAmount = subtotal + tipAmount + taxAmount
        return totalAmount / Double(numberOfPeople)
    }
}
