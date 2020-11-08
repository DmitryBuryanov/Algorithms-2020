package lesson4

import java.lang.IllegalStateException

/**
 * Префиксное дерево для строк
 */
class KtTrie : AbstractMutableSet<String>(), MutableSet<String> {

    class Node {
        val children: MutableMap<Char, Node> = linkedMapOf()
    }

    private var root = Node()

    override var size: Int = 0
        private set

    override fun clear() {
        root.children.clear()
        size = 0
    }

    private fun String.withZero() = this + 0.toChar()

    private fun findNode(element: String): Node? {
        var current = root
        for (char in element) {
            current = current.children[char] ?: return null
        }
        return current
    }

    override fun contains(element: String): Boolean =
        findNode(element.withZero()) != null

    override fun add(element: String): Boolean {
        var current = root
        var modified = false
        for (char in element.withZero()) {
            val child = current.children[char]
            if (child != null) {
                current = child
            } else {
                modified = true
                val newChild = Node()
                current.children[char] = newChild
                current = newChild
            }
        }
        if (modified) {
            size++
        }
        return modified
    }

    override fun remove(element: String): Boolean {
        val current = findNode(element) ?: return false
        if (current.children.remove(0.toChar()) != null) {
            size--
            return true
        }
        return false
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: [java.util.Iterator] (Ctrl+Click по Iterator)
     *
     * Сложная
     */

    override fun iterator(): MutableIterator<String> =
        IteratorKtTrie()

    inner class IteratorKtTrie : MutableIterator<String> {
        var i = 0
        var removesCount = 0
        var nextCount = 0
        var next: String? = ""
        var current: String? = ""
        private val visitedNodes: MutableSet<Node> = mutableSetOf()

        // Создается сет посещенных вершин для того, чтобы не проходить по ним заново.
        // Пробовал в классе Node создать Boolean поле, которое помечает, что мы прошли по этому элементу,
        // но в таком случае становится невозможным создание еще одного итератора одного и того же дерева,
        // так как он не пройдет ни по одному элементу дерева, потому что они будут посещены первым итератором. В итоге
        // я посчитал, что создание сета является наиболее оптимальным решением данной проблемы.
        // При такой реализации итератора он не проходит сразу все возможные варианты слов, добавляя их в список, а
        // обходит все узлы по одному, работая лениво и постепенно заполняя список посещенных улов, что значит,
        // что список будет полностью заполнен лишь в тот момент, когда итератор уже не будет иметь следующих элементов

        init {
            next = findTrueNext()
        }

        //Трудоемкость: О(1)
        //Ресурсоемкость: О(1)
        override fun hasNext(): Boolean {
            return next != null
        }

        //Трудоемкость: О(высота дерева)
        //Ресурсоемкость: О(количество узлов)
        override fun next(): String {
            if (!hasNext()) throw IllegalStateException()
            nextCount++
            removesCount = 0
            if (next != null) {
                current = next
                next = findTrueNext()
                return current!!
            }
            throw IllegalStateException()
        }

        //выполняется поиск следующего элемента дерева(узла, ребро которого имеет значение 0.toChar())
        private fun findTrueNext(): String? {
            next = findNext(next!!)
            if (next != null) {
                while (next!!.last() != 0.toChar()) {
                    next = findNext(next!!)
                    if (next == null) break
                }
                if (next != null) return next!!.substring(0, next!!.lastIndex)
            }
            return null
        }

        //выполняется поиск следующего узла, который мы еще не посещали
        private fun findNext(string: String): String? {
            val current = if (string == "") root else findNode(string)!!
            var newString = string
            for (elements in current.children) {
                if (elements.value !in visitedNodes) {
                    visitedNodes.add(elements.value)
                    newString += elements.key
                    return newString
                }
            }
            return if (current == root) null else findNext(newString.substring(0, newString.lastIndex))
        }

        //Трудоемкость: О(next.length)
        //Ресурсоемкость: O(1)
        override fun remove() {
            if (removesCount == 0 && nextCount != 0) {
                remove(current)
                removesCount++
            } else throw IllegalStateException()
        }
    }
}
