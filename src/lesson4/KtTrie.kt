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
        private val newList: MutableList<String> = mutableListOf()
        var i = 0
        var removesCount = 0
        var nextCount = 0


        init {
            fillThatWeNeed(root, "")
        }

        //трудоемкость: О(количество узлов)
        //ресурсоемкость: О(количество нужных нам элементов дерева(заканчивающихся 0.toChar()))
        fun fillThatWeNeed(node: Node, string: String) {
            for (elements in node.children) {
                if (elements.key == 0.toChar()) newList.add(string)
                fillThatWeNeed(elements.value, string + elements.key)
            }
        }

        //Трудоемкость: О(1)
        //Ресурсоемкость: О(1)
        override fun hasNext(): Boolean {
            return (i < newList.size && newList.size != 0)
        }

        //Трудоемкость: О(1)
        //Ресурсоемкость: О(1)
        var next: String = ""
        override fun next(): String {
            println(newList.size)
            println(i)
            if (!hasNext()) throw IllegalStateException()
            nextCount++
            removesCount = 0
            next = newList[i]
            i++
            return next
        }

        //Трудоемкость: О(next.length)
        //Ресурсоемкость: O(1)
        override fun remove() {
            if (removesCount == 0 && nextCount != 0) {
                remove(next)
                removesCount++
            } else throw IllegalStateException()
        }
    }

}