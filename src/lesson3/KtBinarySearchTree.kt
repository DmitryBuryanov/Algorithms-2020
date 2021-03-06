package lesson3

import java.lang.IllegalStateException
import java.util.*
import kotlin.NoSuchElementException
import kotlin.math.max


// attention: Comparable is supported but Comparator is not
class KtBinarySearchTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    class Node<T>(
        val value: T
    ) {
        var left: Node<T>? = null
        var right: Node<T>? = null
    }

    private var root: Node<T>? = null

    override var size = 0
        private set

    private fun find(value: T): Node<T>? =
        root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    private fun findParent(node: Node<T>): Node<T>? {
        var current = root
        var parent: Node<T>? = null
        while (current != null) {
            val result = current.value.compareTo(node.value)
            if (result > 0) {
                parent = current
                current = current.left
            } else if (result < 0) {
                parent = current
                current = current.right
            } else {
                break
            }
        }
        return parent
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    /**
     * Добавление элемента в дерево
     *
     * Если элемента нет в множестве, функция добавляет его в дерево и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     *
     * Спецификация: [java.util.Set.add] (Ctrl+Click по add)
     *
     * Пример
     */
    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
            }
        }
        size++
        return true
    }

    /**
     * Удаление элемента из дерева
     *
     * Если элемент есть в множестве, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: [java.util.Set.remove] (Ctrl+Click по remove)
     * (в Котлине тип параметера изменён с Object на тип хранимых в дереве данных)
     *
     * Средняя
     */

    //Трудоемкость: O(высоты дерева)
    //Ресурсоемкость: О(1)
    override fun remove(element: T): Boolean {
        val current = find(element) ?: return false
        return removeKnown(current, element)
    }

    private fun removeKnown(current: Node<T>, element: T): Boolean {
        if (element.compareTo(current.value) != 0)
            return false
        val parent = findParent(current)
        size--
        if (current.right == null) {
            if (parent == null) {
                root = current.left
            } else {
                val result = parent.value.compareTo(current.value)
                if (result > 0) {
                    parent.left = current.left
                } else {
                    if (result < 0) {
                        parent.right = current.left
                    }
                }
            }
        } else {
            val righter = current.right
            if (righter != null) {
                if (righter.left == null) {
                    righter.left = current.left
                    if (parent == null) root = current.right else {
                        val result = parent.value.compareTo(current.value)
                        if (result > 0) {
                            parent.left = current.right
                        } else {
                            if (result < 0) {
                                parent.right = current.right
                            }
                        }
                    }
                } else {
                    var leftmost = righter.left
                    var leftmostParent = righter
                    while (leftmost!!.left != null) {
                        leftmostParent = leftmost
                        leftmost = leftmost.left
                    }
                    leftmostParent!!.left = leftmost.right
                    leftmost.left = current.left
                    leftmost.right = current.right
                    if (parent == null) {
                        root = leftmost
                    } else {
                        val result = parent.value.compareTo(current.value)
                        if (result > 0) {
                            parent.left = leftmost
                        } else {
                            if (result < 0) parent.right = leftmost
                        }
                    }
                }
            }
        }
        return true
    }


    override fun comparator(): Comparator<in T>? =
        null

    override fun iterator(): MutableIterator<T> =
        BinarySearchTreeIterator()

    inner class BinarySearchTreeIterator internal constructor() : MutableIterator<T> {

        var next: Node<T>? = null
        var removesCount = 0
        var nextCount = 0
        lateinit var removed: T
        lateinit var removedNode: Node<T>

        init {
            if (root != null) {
                next = root
                while (next!!.left != null) {
                    next = next!!.left
                }
            }
        }

        /**
         * Проверка наличия следующего элемента
         *
         * Функция возвращает true, если итерация по множеству ещё не окончена (то есть, если вызов next() вернёт
         * следующий элемент множества, а не бросит исключение); иначе возвращает false.
         *
         * Спецификация: [java.util.Iterator.hasNext] (Ctrl+Click по hasNext)
         *
         * Средняя
         */

        //Трудоемкость: О(1)
        //Ресурсоемкость:О(1)
        override fun hasNext(): Boolean {
            return next != null
        }

        /**
         * Получение следующего элемента
         *
         * Функция возвращает следующий элемент множества.
         * Так как BinarySearchTree реализует интерфейс SortedSet, последовательные
         * вызовы next() должны возвращать элементы в порядке возрастания.
         *
         * Бросает NoSuchElementException, если все элементы уже были возвращены.
         *
         * Спецификация: [java.util.Iterator.next] (Ctrl+Click по next)
         *
         * Средняя
         */

        /** @return the next smallest number
         */
        //Трудоемкость: О(высоты дерева)
        //Ресурсоемкость:О(1)
        override fun next(): T {
            nextCount++
            removesCount = 0
            if (!hasNext()) throw IllegalStateException()
            val r = next

            val righter = next?.right
            if (righter != null) {
                next = righter
                while (next!!.left != null)
                    next = next!!.left;
                removed = r!!.value
                removedNode = r
                return removed
            }
            while (true) {
                val parent = findParent(next!!)
                if (parent == null) {
                    next = null
                    removed = r!!.value
                    removedNode = r
                    return removed
                }
                if (parent.left == next) {
                    next = parent
                    removed = r!!.value
                    removedNode = r
                    return removed
                }
                next = parent
            }
        }

        /**
         * Удаление предыдущего элемента
         *
         * Функция удаляет из множества элемент, возвращённый крайним вызовом функции next().
         *
         * Бросает IllegalStateException, если функция была вызвана до первого вызова next() или же была вызвана
         * более одного раза после любого вызова next().
         *
         * Спецификация: [java.util.Iterator.remove] (Ctrl+Click по remove)
         *
         * Сложная
         */
        //Трудоемкость: О(высоты дерева)
        //Ресурсоемкость:О(1)
        override fun remove() {
            if (removesCount == 0 && nextCount != 0) {
                removeKnown(removedNode, removed)
                removesCount++
            } else throw IllegalStateException()
        }

    }

    /**
     * Подмножество всех элементов в диапазоне [fromElement, toElement)
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева, которые
     * больше или равны fromElement и строго меньше toElement.
     * При равенстве fromElement и toElement возвращается пустое множество.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: [java.util.SortedSet.subSet] (Ctrl+Click по subSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Очень сложная (в том случае, если спецификация реализуется в полном объёме)
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Подмножество всех элементов строго меньше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева строго меньше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: [java.util.SortedSet.headSet] (Ctrl+Click по headSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Подмножество всех элементов нестрого больше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева нестрого больше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: [java.util.SortedSet.tailSet] (Ctrl+Click по tailSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        TODO()
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }

    override fun height(): Int =
        height(root)

    private fun height(node: Node<T>?): Int {
        if (node == null) return 0
        return 1 + max(height(node.left), height(node.right))
    }

    override fun checkInvariant(): Boolean =
        root?.let { checkInvariant(it) } ?: true

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

}