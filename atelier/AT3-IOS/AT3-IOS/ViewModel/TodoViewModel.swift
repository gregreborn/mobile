import SwiftUI
import Combine


class TodosViewModel: ObservableObject {
    @Published var todos: [Todo] = []
    
    init() {
        load()
    }
    
    func save() {
        if let encodedData = try? PropertyListEncoder().encode(todos) {
            UserDefaults.standard.set(encodedData, forKey: "todosKey")
        }
    }
    
    func load() {
        if let data = UserDefaults.standard.value(forKey: "todosKey") as? Data {
            if let decodedTodos = try? PropertyListDecoder().decode(Array<Todo>.self, from: data) {
                self.todos = decodedTodos
            }
        }
    }
    
    func addTodo(name: String) {
        let newTodo = Todo(name: name)
        todos.append(newTodo)
        save()
    }
    
    func deleteTodo(at offsets: IndexSet) {
        todos.remove(atOffsets: offsets)
        save()
    }
}
