package com.f1forhelp.ovo

import android.os.Build
import android.view.WindowInsetsController
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SetNavigationBarColor(color: androidx.compose.ui.graphics.Color, darkIcons: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = androidx.core.view.ViewCompat.getWindowInsetsController(view)?.let {
            view.context.findActivity()?.window
        } ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.navigationBarColor = color.toArgb()
            val appearance = if (darkIcons)
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            else 0
            window.insetsController?.setSystemBarsAppearance(appearance, appearance)
        } else {
            // For older APIs, fallback
            window.navigationBarColor = color.toArgb()
        }
    }
}

// Helper to find Activity from context
fun android.content.Context.findActivity(): androidx.activity.ComponentActivity? {
    var ctx = this
    while (ctx is android.content.ContextWrapper) {
        if (ctx is androidx.activity.ComponentActivity) return ctx
        ctx = ctx.baseContext
    }
    return null
}