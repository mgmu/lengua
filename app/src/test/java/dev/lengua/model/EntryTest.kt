package dev.lengua.model

import dev.lengua.ui.Entry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class EntryTest {

    @Test
    fun `Given empty term, When constructor called, Then an IllegalArgumentException is thrown`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Entry("", "definition")
        }
        assertEquals("Term can not be empty.", exception.message)
    }

    @Test
    fun `Given empty definition, When constructor called, Then an IllegalArgumentException is thrown`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Entry("term", "")
        }
        assertEquals("Definition can not be empty.", exception.message)
    }

    @Test
    fun `Given term length of 101 characters, When constructor called, Then an IllegalArgumentException is throws`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Entry("loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong", "definition")
        }
        assertEquals(
            "Term length must not exceed 100 characters.",
            exception.message
        )
    }

    @Test
    fun `Given definition length of 1001 characters, When constructor called, Then an IllegalArgumentException is thrown`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Entry("term", "loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong")
        }
        assertEquals(
            "Definition length must not exceed 1000 characters.",
            exception.message
        )
    }

    @Test
    fun `Given term composed solely of whitespaces, When constructor called, Then an IllegalArgumentException is throws`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Entry("    ", "definition")
        }
        assertEquals("Term can not be empty.", exception.message)
    }

    @Test
    fun `Given definition composed solely of whitespaces, When constructor called, Then an IllegalArgumentException is thrown`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Entry("term", "        ")
        }
        assertEquals("Definition can not be empty.", exception.message)
    }
}