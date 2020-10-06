package lesson4

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

    override fun iterator(): MutableIterator<String> = TrieIterator()

    inner class TrieIterator internal constructor() : MutableIterator<String> {
        var current: Node? = null
        var strSet: MutableSet<String> = mutableSetOf()
        var strvalue = ""
        var next: String? = null
        val childrens: MutableSet<Char> = mutableSetOf()

        init {
            if (root.children.isNotEmpty()) current = root
        }

        override fun hasNext(): Boolean {
            return next != null
        }

        fun childrensNotInSet(str: String): Boolean {
            val node = findNode(str)
            if (node!!.children.isEmpty()) return false
            for (elements in node!!.children.keys) {
                val newStr = str + elements
                if (!strSet.contains(newStr)) return true
            }
            return false
        }

        override fun next(): String {
            if (next1() == null) throw IllegalStateException()
            else {
                next = next1()
                return next!!
            }
        }

        fun next1(): String? {
            if (current != null && current?.children?.isNotEmpty()!!) {
                childrens.clear()
                childrens.addAll(current!!.children.keys)
                for (elements in childrens) {
                    val new = strvalue + elements
                    if (!strSet.contains(new)) {
                        strvalue = new
                        current = findNode(new)!!
                        strSet.add(strvalue)
                        return strvalue
                    }
                }
            } else if (strvalue.isNotEmpty()) {
                var parent = strvalue.substring(0, strvalue.length - 1)
                var parentNode = findNode(parent)
                if (childrensNotInSet(parent)) {
                    current = parentNode
                    strvalue = parent
                } else {
                    while (!childrensNotInSet(parent)) {
                        current = parentNode
                        strvalue = parent
                        parent = strvalue.substring(0, strvalue.length - 1)
                        parentNode = findNode(parent)
                    }
                }
                next1()
            }
            return null
        }

        override fun remove() {
            TODO()
        }
    }

}