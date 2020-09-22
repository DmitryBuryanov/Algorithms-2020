@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
import kotlin.math.min

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
 * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
 *
 * Пример:
 *
 * 01:15:19 PM
 * 07:26:57 AM
 * 10:00:03 AM
 * 07:56:14 PM
 * 01:15:19 PM
 * 12:40:31 AM
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 12:40:31 AM
 * 07:26:57 AM
 * 10:00:03 AM
 * 01:15:19 PM
 * 01:15:19 PM
 * 07:56:14 PM
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortTimes(inputName: String, outputName: String) {

    val timeList = mutableListOf<String>()
    for (line in File(inputName).readLines()) {
        require(line.matches(Regex("[0-2][0-9]:[0-5][0-9]:[0-5][0-9] (PM|AM)")))
        timeList.add(line)
    }

    fun merge(elements: Array<String>, begin: Int, middle: Int, end: Int) {
        val left = elements.copyOfRange(begin, middle)
        val right = elements.copyOfRange(middle, end)
        var li = 0
        var ri = 0
        for (i in begin until end) {
            if (li < left.size && (ri == right.size || compare(left[li], right[ri]) == 0 || compare(
                    left[li], right[ri]
                ) == 2)
            ) {
                elements[i] = left[li++]
            } else {
                elements[i] = right[ri++]
            }
        }
    }

    fun mergeSort(elements: Array<String>, begin: Int, end: Int) {
        if (end - begin <= 1) return
        val middle = (begin + end) / 2
        mergeSort(elements, begin, middle)
        mergeSort(elements, middle, end)
        merge(elements, begin, middle, end)
    }

    fun mergeSort(elements: Array<String>) {
        mergeSort(elements, 0, elements.size)
    }

    val timeArray = timeList.toTypedArray()
    mergeSort(timeArray)

    val writer = File(outputName).bufferedWriter()
    for (line in timeArray) {
        writer.write(line)
        writer.newLine()
    }
    writer.close()
}

fun compare(name1: String, name2: String): Int {
    val list1 = name1.split(Regex("[: ]"))
    val list2 = name2.split(Regex("[: ]"))
    if (list1[3] != list2[3]) {
        return if (list1[3] == "PM") 1 else 2
    } else {
        if (list1[0] != list2[0]) {
            if (list1[0] == "12") return 2
            if (list2[0] == "12") return 1
        }
        for (i in 0..2) {
            if (list1[i].toInt() > list2[i].toInt()) return 1
            else if (list1[i].toInt() < list2[i].toInt()) return 2
        }
    }
    return 0
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortAddresses(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 */
fun sortTemperatures(inputName: String, outputName: String) {
    val tempList = mutableListOf<Double>()
    for (line in File(inputName).readLines()) {
        tempList.add(line.toDouble())
    }
    val tempArray = tempList.toDoubleArray()
    quickSort(tempArray)
    val writer = File(outputName).bufferedWriter()
    for (line in tempArray) {
        writer.write(line.toString())
        writer.newLine()
    }
    writer.close()
}

fun partition(elements: DoubleArray, min: Int, max: Int): Int {
    val x = elements[min + random.nextInt(max - min + 1)]
    var left = min
    var right = max
    while (left <= right) {
        while (elements[left] < x) {
            left++
        }
        while (elements[right] > x) {
            right--
        }
        if (left <= right) {
            val temp = elements[left]
            elements[left] = elements[right]
            elements[right] = temp
            left++
            right--
        }
    }
    return right
}

fun quickSort(elements: DoubleArray, min: Int, max: Int) {
    if (min < max) {
        val border = partition(elements, min, max)
        quickSort(elements, min, border)
        quickSort(elements, border + 1, max)
    }
}

fun quickSort(elements: DoubleArray) {
    quickSort(elements, 0, elements.size - 1)
}

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 */
fun sortSequence(inputName: String, outputName: String) {
    val stat = mutableMapOf<Int, Int>()
    val numberList = mutableListOf<Int>()
    for (lines in File(inputName).readLines()) {
        if (stat[lines.toInt()] == null) stat[lines.toInt()] = 1
        else {
            val x = stat[lines.toInt()]
            if (x != null) {
                stat.replace(lines.toInt(), x + 1)
            }
        }
        numberList.add(lines.toInt())
    }
    var minValueKey = Integer.MAX_VALUE
    var maxValue = 0
    for ((key, value) in stat) {
        if (value > maxValue) {
            maxValue = value
            minValueKey = key
        } else if (value == maxValue) {
            if (key < minValueKey) minValueKey = key
        }
    }
    val writer = File(outputName).bufferedWriter()
    for (line in numberList) {
        if (line != minValueKey) {
            writer.write(line.toString())
            writer.newLine()
        }
    }
    for (i in 1..maxValue) {
        writer.write(minValueKey.toString())
        writer.newLine()
    }
    writer.close()
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    TODO()
}

