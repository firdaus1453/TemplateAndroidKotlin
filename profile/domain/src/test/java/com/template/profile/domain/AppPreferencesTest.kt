package com.template.profile.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class AppPreferencesTest {

    @Test
    fun `ThemeMode has correct entries count`() {
        assertEquals(3, ThemeMode.entries.size)
    }

    @Test
    fun `ThemeMode entries are correct`() {
        assertEquals(ThemeMode.SYSTEM, ThemeMode.valueOf("SYSTEM"))
        assertEquals(ThemeMode.LIGHT, ThemeMode.valueOf("LIGHT"))
        assertEquals(ThemeMode.DARK, ThemeMode.valueOf("DARK"))
    }

    @Test
    fun `AppLanguage has correct locale values`() {
        assertEquals("en", AppLanguage.ENGLISH.locale)
        assertEquals("in", AppLanguage.INDONESIAN.locale)
        assertEquals("ja", AppLanguage.JAPANESE.locale)
    }

    @Test
    fun `AppLanguage has correct entries count`() {
        assertEquals(3, AppLanguage.entries.size)
    }
}
