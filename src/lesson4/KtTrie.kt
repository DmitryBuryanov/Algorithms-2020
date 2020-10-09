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
        private val listNodes: MutableList<Node> = mutableListOf(root)
        private val listStrings: MutableList<String> = mutableListOf("")
        private val newList: MutableList<String> = mutableListOf()
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
                    if (elements == 0.toChar()) {
                        newList.add(newString.substring(0, newString.lastIndex))
                    }
                }
                fillInLists(findNode(newString)!!)
            }
        }

        override fun hasNext(): Boolean {
            return (i < newList.size && newList.size != 0)
        }

        var next: String = ""
        override fun next(): String {
            if (!hasNext()) throw IllegalStateException()
            nextCount++
            next = newList[i]
            i++
            return next
        }

        override fun remove() {
            if (removesCount == 0 && nextCount != 0) {
                remove(next)
                removesCount++
            } else throw IllegalStateException()
        }
    }

}

fun main() {
    val kttrie: KtTrie = KtTrie()
    val bnm =
        ("ache, dhfcdgffh, bcg, hbhabgaac, d, h, bcdc, aedcegadbf, fecbehg, babfbbfabd, hbgb, fchbe, ghfheedggb, eheecb, bdhbcchc").split(
            Regex(", ")
        )
    kttrie.addAll(bnm)

    println(kttrie)
    val xx = kttrie.iterator()
    val yy = xx.next()
    val zz = xx.next()
    print("$yy, $zz")
    xx.remove()
    println(" ")
    println(kttrie)
}