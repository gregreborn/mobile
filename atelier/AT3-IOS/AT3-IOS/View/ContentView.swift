import SwiftUI
import Combine

struct ContentView: View {
    @StateObject var viewModel = TodosViewModel()
    @State private var newTodoName: String = ""

    var body: some View {
        NavigationView {
            List {
                // Add a section for the TextField
                Section(header: Text("Add New Todo")) {
                    HStack {
                        TextField("Add todo...", text: $newTodoName)
                        Button(action: {
                            guard !newTodoName.isEmpty else { return }
                            viewModel.addTodo(name: newTodoName)
                            newTodoName = "" // Clear the TextField after adding
                        }) {
                            Image(systemName: "plus.circle.fill")
                                .foregroundColor(.blue)
                        }
                    }
                }

                Section {
                    ForEach(viewModel.todos) { todo in
                        Text(todo.name)
                    }
                    .onDelete(perform: viewModel.deleteTodo)
                }
            }
            .navigationBarTitle("Todos")
            .onAppear {
                viewModel.load()
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
