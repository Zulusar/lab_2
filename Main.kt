import java.io.PrintStream


fun main() {
    val outputConsole: PrintStream =
        PrintStream(System.out, true, "UTF-8")

    println(
        """Приветствую в игре "Крестики-Нолики"!
|Перед Вами находится поле 3х3. Правила игры, думаю, Вы знаете.""".trimMargin()
    )//приветствие игрока
    println("По правилам крестиком ходить первым, так что вперед, дерзайте!")

    var Field: Array<Array<Char>> =
        arrayOf(arrayOf(' ', ' ', ' '), arrayOf(' ', ' ', ' '), arrayOf(' ', ' ', ' '))//поле
    var count = 0// счетчик на выигрыш
    var iter = 0//счетчик для определения нужного символа
    var currentIndex = 0//счетчик хода
    val boardSize = 4//ограничения по вводу
    var result = 1//счетчик победы
    val Game: MutableList<Array<Array<Char>>> = mutableListOf(
        arrayOf(
            arrayOf(' ', ' ', ' '),
            arrayOf(' ', ' ', ' '),
            arrayOf(' ', ' ', ' ')
        )
    )//промежуточное поле
    var game = Array(1, { mutableListOf<Array<Array<Char>>>() })//массив сбора полей
    var Field1: Array<Array<Char>>//копия поля
    var Coordinates: String//координаты

    fun printBoard(board: Array<Array<Char>>) {//вывод игры
        for (row in board) println(row.contentToString())
    }

    fun Array<Array<Char>>.isFill(): Boolean {//проверка на присутствие места для хода
        var a = 9//счетчик пустых полей
        for (row in this) {
            for (cell in row) {
                if (cell != ' ') a--//при заполнении уменьшается счетчик
            }
        }
        return if (a == 0) false//если поля заполнены-ложь
        else true
    }

    fun pointFromString(string: String): Array<Int> {//преобразования строки в массив чисел
        val Coor = string.split(" ").map(String::toInt)//разбеление через _
        val Coor1 = arrayOf(Coor[0], Coor[1])
        return Coor1
    }

    fun read(string: String): Pair<Int, Int> {// то же самое, но преобразование в Pair
        val Coor = string.split(" ").map(String::toInt)
        val Coor1 = Pair(Coor[0], Coor[1])
        return Coor1
    }

    fun Array<Array<Char>>.isRightMove(point: Array<Int>): Boolean {//проверка на правльность введенных координат
        if (point[0] == 3 && point[1] >= 0) return true//команда на возврат хода
        if (point[0] > boardSize || point[1] > boardSize || this[point[0]][point[1]] != ' ') return false//команда на ввод координат
        else return true
    }

    fun Array<Array<Char>>.get(point: Pair<Int, Int>): Char {//замена пробела на нужный символ
        return if (iter % 2 != 0) 'X'
        else 'O'
    }

    fun Array<Array<Char>>.set(point: Pair<Int, Int>) {//постановка символа на доску
        this[point.first][point.second] = this.get(point)//позиция по координатам меняется на результат геттера
        printBoard(Field)//вывод измененного поля
        return
    }

    fun Copy(z: Array<Array<Char>>): Array<Array<Char>> {//копирование поля
        val clone =
            arrayOf(arrayOf(' ', ' ', ' '), arrayOf(' ', ' ', ' '), arrayOf(' ', ' ', ' '))//вспомогательный массив
        //который является результатом работы функции
        for (i in z.indices) {
            clone[i] = z[i].clone()
        }
        return clone
    }

    fun Array<MutableList<Array<Array<Char>>>>.printCopy(x: Int): Array<Array<Char>> {//вывод копии
        val i = 0//вспомогательный индекс
        val needField = this[i][x]//присваивание новому массиву значения
        return needField
    }

    fun Array<Array<Char>>.checkWin(): Char {//проверка на выигрыш
        var a = 0//счетчик
        val winLines = arrayOf(//выигрышные комбинации
            arrayOf(arrayOf(0, 0), arrayOf(0, 1), arrayOf(0, 2)),
            arrayOf(arrayOf(1, 0), arrayOf(1, 1), arrayOf(1, 2)),
            arrayOf(arrayOf(2, 0), arrayOf(2, 1), arrayOf(2, 2)),
            arrayOf(arrayOf(0, 0), arrayOf(1, 0), arrayOf(2, 0)),
            arrayOf(arrayOf(0, 1), arrayOf(1, 1), arrayOf(2, 1)),
            arrayOf(arrayOf(0, 2), arrayOf(1, 2), arrayOf(2, 2)),
            arrayOf(arrayOf(0, 0), arrayOf(1, 1), arrayOf(2, 2)),
            arrayOf(arrayOf(0, 2), arrayOf(1, 1), arrayOf(2, 0))
        )

        for (lines in winLines) {
            val coord1 = lines[0]
            val coord2 = lines[1]
            val coord3 = lines[2]
            if (this[coord1[0]][coord1[1]] == this[coord2[0]][coord2[1]] && this[coord1[0]][coord1[1]] == this[coord3[0]][coord3[1]] && this[coord1[0]][coord1[1]] == 'O') {
                a++//увеличение счетчика для корректного определния победителя
                count++
            }
            if (this[coord1[0]][coord1[1]] == this[coord2[0]][coord2[1]] && this[coord1[0]][coord1[1]] == this[coord3[0]][coord3[1]] && this[coord1[0]][coord1[1]] == 'X') {
                a += 2
                count++
            }
        }

        return if (a == 1) 'O'
        else 'X'
    }

    do {
        do {
            println("\nВведите координаты хода, либо 3 и количество ходов, на которое необходимо вернуться")
            Coordinates = readLine().toString()//считывание координат
            pointFromString(Coordinates)//их проверка
        } while (Field.isRightMove(pointFromString(Coordinates)) == false)
        if (pointFromString(Coordinates)[0] != 3) {//если не команда, то сделать ход
            println("Ход $currentIndex")
            iter++
            Field.get(read(Coordinates))
            Field.set(read(Coordinates))
            Field1 = Copy(Field)//копирование поля
            Game[0] = Field1//запись в массив
            game[currentIndex].add(Game[0])//добавление массива в массив
            game = Array(game.size + 1) { game[currentIndex] } //увеличение размера массива
//            println("Ход №$currentIndex")
            currentIndex++//увеличение хода
            Field.checkWin()//проврека на победу
        } else {//если команда
            currentIndex = pointFromString(Coordinates)[1]
            println("Ход $currentIndex")
            Field = game.printCopy(pointFromString(Coordinates)[1]) //переприсвоение значения полю
            //переприсвоение счетчика хода
            printBoard(Field)
            currentIndex++
//            println("Ход $currentIndex")
            if (iter % 2 == 0) iter == 1
            else iter == 0
        }
        if (Field.isFill() == false) result--

    } while (count == 0)//до выигрышной комбинации
    if (result == 0) {//если result уменьшился - игра завершается
        println("Места нет! Гамовер! Добро пожаловать отсюда!")
        return
    }
    println("Поздравляю, выиграли ${Field.checkWin()}")
}








