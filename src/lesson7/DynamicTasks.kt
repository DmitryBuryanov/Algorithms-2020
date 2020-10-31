@file:Suppress("UNUSED_PARAMETER")

package lesson7

import java.io.File
import java.lang.Integer.min
import kotlin.math.max

/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
 * При сравнении подстрок, регистр символов *имеет* значение.
 */
//Трудоемкость: O(first.length * second.length)
//Ресурсоемкость: O(first.length * second.length)
fun longestCommonSubSequence(first: String, second: String): String {
    val n = first.length
    val m = second.length
    val table: Array<Array<Int>> = Array(n + 1) { Array(m + 1) { 0 } }
    for (i in 1..n) {
        for (j in 1..m) {
            if (first[i - 1] == second[j - 1]) table[i][j] = table[i - 1][j - 1] + 1
            else table[i][j] = max(table[i - 1][j], table[i][j - 1])
        }
    }
    var answer = ""
    var i = n
    var j = m
    while (i > 0 && j > 0) {
        when {
            (first[i - 1] == second[j - 1]) -> {
                answer += first[i - 1]
                i--
                j--
            }
            (table[i - 1][j] == table[i][j]) -> i--
            else -> j--
        }
    }
    return answer.reversed()
}

/**
 * Наибольшая возрастающая подпоследовательность
 * Сложная
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 */
fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    /*val n = list.size
    val d = IntArray(n + 1)
    val pos = IntArray(n + 1)
    val prev = IntArray(n)
    var length = 0

    pos[0] = -1
    d[0] = Int.MIN_VALUE
    for (i in 1..n) d[i] = Int.MAX_VALUE
    for (i in 0 until n) {
        val j = binSearch(d.sorted().toTypedArray(), list[i])
        if (d[j - 1] < list[i] && list[i] < d[j]) {
            d[j] = list[i]
            pos[j] = i
            prev[i] = pos[j - 1]
            length = max(length, j)
        }
    }
    val answer = mutableListOf<Int>()
    var p = pos[length]
    while (p != -1) {
        answer.add(list[p])
        p = prev[p]
    }
    return answer.reversed()
}

fun binSearch(a: Array<Int>, key: Int): Int {
    var l = -1
    var r = a.size
    while (l < r - 1) {
        val m = (l + r) / 2
        if (a[m] < key) l = m else r = m
    }
    return r*/
    TODO()
}

/**
 * Самый короткий маршрут на прямоугольном поле.
 * Средняя
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 */

//Трудоемкость: O(длина поля * ширина поля)
//Ресурсоемкость: О(длина поля * ширина поля)
fun shortestPathOnField(inputName: String): Int {
    val lines = File(inputName).readLines()
    val counts = lines[0].split(" ")
    val a = counts.size
    val b = lines.size
    val array: Array<Array<Int>> = Array(b) { Array(a) { 0 } }
    val weightArray: Array<Array<Int>> = Array(b) { Array(a) { 0 } }
    for (i in 0 until b) {
        for (j in 0 until a) {
            array[i][j] = lines[i].split(" ")[j].toInt()
        }
    }
    for (i in 0 until b) {
        for (j in 0 until a) {
            when {
                (i == 0 && j == 0) -> {
                    weightArray[i][j] = 0
                }
                (i == 0) -> {
                    weightArray[i][j] = weightArray[i][j - 1] + array[i][j]
                }
                (j == 0) -> {
                    weightArray[i][j] = weightArray[i - 1][j] + array[i][j]
                }
                else -> weightArray[i][j] =
                    min(weightArray[i - 1][j - 1], min(weightArray[i - 1][j], weightArray[i][j - 1])) + array[i][j]
            }
        }
    }
    return weightArray[b - 1][a - 1]
}
// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5