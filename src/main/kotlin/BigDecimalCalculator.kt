import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)
    println("Welcome to BigDecimal Calculator!!!")
    println("Enter an equation : ")
    val equation = scanner.nextLine()

    try {
        val result = separateEquation(equation)
        println("Result of $equation : $result")
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}

fun separateEquation(equation: String): BigDecimal {
    val equationArr = equation.toCharArray() //separate equation into char array
    val values = Stack<BigDecimal>() //stack for numbers
    val operators = Stack<Char>() //stack for operators

    var i = 0
    while (i < equationArr.size) {
        if (equationArr[i] in '0'..'9' || equationArr[i] == '.') {
            val sb = StringBuilder()
            while (i < equationArr.size && (equationArr[i] in '0'..'9' || equationArr[i] == '.')) {
                sb.append(equationArr[i++])
            }
            values.push(sb.toString().toBigDecimal())
            i--
        } else if (equationArr[i] == '(') {
            operators.push(equationArr[i])
        } else if (equationArr[i] == ')') {
            while (operators.peek() != '(') {
                values.push(applyOp(operators.pop(), values.pop(), values.pop()))
            }
            operators.pop()
        } else if (equationArr[i] == '+' || equationArr[i] == '-' || equationArr[i] == '*' || equationArr[i] == '/') {
            while (!operators.isEmpty() && checkPrior(equationArr[i], operators.peek())) {
                values.push(applyOp(operators.pop(), values.pop(), values.pop()))
            }
            operators.push(equationArr[i])
        }
        i++
    }
    //apply remaining op and number in stack
    while (operators.isNotEmpty()) {
        values.push(applyOp(operators.pop(), values.pop(), values.pop()))
    }

    return values.pop() //the last value in the stack
}

//perform math operation
fun applyOp(op: Char, b: BigDecimal, a: BigDecimal): BigDecimal {
    return when (op) {
        '+' -> a.add(b)
        '-' -> a.subtract(b)
        '*' -> a.multiply(b)
        '/' -> a.divide(b,2,RoundingMode.HALF_UP) //initial scale of result after divide
        else -> throw UnsupportedOperationException("Operator not supported")
    }
}

//check priority of operators
fun checkPrior(op1: Char, op2: Char): Boolean {
    if (op2 == '(' || op2 == ')') return false
    if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false
    return true
}
