package lesson3

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class KtBinarySearchTreeTest : AbstractBinarySearchTreeTest() {

    override fun create(): CheckableSortedSet<Int> =
        KtBinarySearchTree()

    @Test
    @Tag("Example")
    fun initTest() {
        doInitTest()
    }

    @Test
    @Tag("Example")
    fun addTest() {
        doAddTest()
    }

    @Test
    @Tag("Example")
    fun firstAndLastTest() {
        doFirstAndLastTest()
    }

    @Test
    @Tag("5")
    fun removeTest() {
        doRemoveTest()
        myRemoveTest()
    }

    @Test
    @Tag("5")
    fun iteratorTest() {
        doIteratorTest()
        myIteratorTest()
    }

    @Test
    @Tag("8")
    fun iteratorRemoveTest() {
        doIteratorRemoveTest()
        myRemoveIteratorTest()
        myRemoveIteratorTest2()
    }

    @Test
    @Tag("5")
    fun subSetTest() {
        doSubSetTest()
    }

    @Test
    @Tag("8")
    fun subSetRelationTest() {
        doSubSetRelationTest()
    }

    @Test
    @Tag("7")
    fun subSetFirstAndLastTest() {
        doSubSetFirstAndLastTest()
    }

    @Test
    @Tag("4")
    fun headSetTest() {
        doHeadSetTest()
    }

    @Test
    @Tag("7")
    fun headSetRelationTest() {
        doHeadSetRelationTest()
    }

    @Test
    @Tag("4")
    fun tailSetTest() {
        doTailSetTest()
    }

    @Test
    @Tag("7")
    fun tailSetRelationTest() {
        doTailSetRelationTest()
    }

}