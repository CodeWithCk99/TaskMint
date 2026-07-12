package com.codewithck.taskmint.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {

    private const val PREF_NAME = "taskmint_theme"

    private const val KEY_THEME = "theme"

    const val LIGHT = 0
    const val DARK = 1
    const val SYSTEM = 2

    fun applyTheme(context: Context) {

        val prefs =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        when (prefs.getInt(KEY_THEME, SYSTEM)) {

            LIGHT ->
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )

            DARK ->
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )

            else ->
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )
        }
    }

    fun saveTheme(
        context: Context,
        theme: Int
    ) {

        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        ).edit()
            .putInt(KEY_THEME, theme)
            .apply()
    }
}