package com.thetopheadlines.topnews

import com.thetopheadlines.topnews.data.utils.Utils
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun `formatDate returns correctly formatted date`() {
        val inputDate = "2025-02-07T03:43:43Z"
        val expectedOutput = "Feb. 7, 2025"
        assertEquals(expectedOutput, Utils.formatDate(inputDate))
    }

    @Test
    fun `formatDate handles invalid date string`() {
        val inputDate = "invalid-date"
        val expectedOutput = "Unknown Date"
        assertEquals(expectedOutput, Utils.formatDate(inputDate))
    }

    @Test
    fun `formatDate handles empty date string`() {
        val inputDate = ""
        val expectedOutput = "Unknown Date"
        assertEquals(expectedOutput, Utils.formatDate(inputDate))
    }

    @Test
    fun `formatDate handles null date string`() {
        val inputDate: String? = null
        val expectedOutput = "Unknown Date"
        assertEquals(expectedOutput, Utils.formatDate(inputDate ?: ""))
    }

    @Test
    fun `formatDate handles invalidFormat date string`() {
        val inputDate = "03:43:43Z"
        val expectedOutput = "Unknown Date"
        assertEquals(expectedOutput, Utils.formatDate(inputDate))
    }

}