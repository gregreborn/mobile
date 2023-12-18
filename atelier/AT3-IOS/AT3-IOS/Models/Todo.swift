import SwiftUI
import Combine


struct Todo: Identifiable, Codable {
    var id: UUID = UUID()
    var name: String
}
