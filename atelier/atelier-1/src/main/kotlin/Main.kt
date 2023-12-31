fun main(args: Array<String>) {

    var positionCol: Int
    var positionRow: Int
    val ROW = 4
    val COL = 4
    var turn = 'X'
    var num = 0
    val array = arrayOf(
        arrayOf('*','*','*','*'),
        arrayOf('*','*','*','*'),
        arrayOf('*','*','*','*'),
        arrayOf('*','*','*','*')
    )

     print ("Player 1, enter your name: ")
    val player1: String = readln()
    println("$player1,you are X's! You will also have the first turn")
    print("Player 2, enter your name: ")
    val player2: String = readln()
    println("$player2,you are O's!")
    println("COLUMNS AND ROWS START WITH 0")
    for (row in array.indices){
        println(array[row].contentToString())
    }

    while (true) {
        println("type the column you would like to place your position")
        while (true) {
            try {
                positionCol = readln().toInt()
                if (positionCol in 0..4) {
                    break
                }
            } catch (e: NumberFormatException) {
                println("invalid data")
                println("type the column you would like to place your position")
            }
        }

        println("type the row you would like to place your position")
        while (true) {
            try {
                positionRow = readln().toInt()
                if (positionRow in 0..4) {
                    break
                }
            } catch (e: NumberFormatException) {
                println("invalid data")
                println("type the row you would like to place your position")
            }
        }

        if (array[positionRow][positionCol] == '*') {
            array[positionRow][positionCol] = turn
            for (row in array) {
                println(row.contentToString())
            }
            num++

            if (num >= 4 && checkWin(array, positionRow, positionCol, turn)) {
                println("$turn wins!")
                break
            } else if (num == ROW * COL) {
                println("It's a draw!")
                break
            }

            turn = if (turn == 'X') 'O' else 'X'
        }else {
            println("Position is already taken. Choose another position.")
        }

    }
}

fun checkWin(array: Array<Array<Char>>, row: Int, col: Int, symbol: Char): Boolean {
    // Check row
    var win = true
    for (i in array.indices) {
        if (array[row][i] != symbol) {
            win = false
            break
        }
    }
    if (win) return true

    // Check column
    win = true
    for (i in array.indices) {
        if (array[i][col] != symbol) {
            win = false
            break
        }
    }
    if (win) return true

    // Check diagonal from top-left to bottom-right
    if (row == col) {
        win = true
        for (i in array.indices) {
            if (array[i][i] != symbol) {
                win = false
                break
            }
        }
        if (win) return true
    }

    // Check diagonal from top-right to bottom-left
    if (row + col == array.size - 1) {
        win = true
        for (i in array.indices) {
            if (array[i][array.size - i - 1] != symbol) {
                win = false
                break
            }
        }
        if (win) return true
    }

    return false
}



