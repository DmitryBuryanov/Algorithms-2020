package lesson4

import java.lang.IllegalStateException
import java.util.*

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
        private val listNodes: MutableList<Node> = mutableListOf(root)
        private val listStrings: MutableList<String> = mutableListOf("")
        var i = 0
        var removesCount = 0
        var nextCount = 0

        init {
            fillInLists(root)
        }

        fun fillInLists(currentNode: Node) {
            for (elements in currentNode.children.keys) {
                val newString = listStrings[listNodes.indexOf(currentNode)] + elements
                if (findNode(newString) != null && newString !in listStrings) {
                    listNodes.add(findNode(newString)!!)
                    listStrings.add(newString)
                }
                fillInLists(findNode(newString)!!)
            }
            if (listNodes.size == 1) {
                listNodes.removeAt(0)
                listStrings.removeAt(0)
            }
        }

        override fun hasNext(): Boolean {
            return (i + 1 < listNodes.size && listNodes.size != 0)
        }

        override fun next(): String {
            if (!hasNext()) throw IllegalStateException()
            nextCount++
            i++
            return listStrings[i]
        }

        override fun remove() {
            if (removesCount == 0 && nextCount != 0) {
                remove(listStrings[i])
                for (j in i until listStrings.size - 1) {
                    listStrings[j] = listStrings[j + 1]
                    listNodes[j] = listNodes[j + 1]
                }
                listNodes.removeAt(listNodes.lastIndex)
                listStrings.removeAt(listStrings.lastIndex)
                i--
                removesCount++
            } else throw IllegalStateException()
        }
    }

}