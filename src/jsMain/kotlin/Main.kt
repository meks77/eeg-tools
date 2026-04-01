package at.hillstromeeg

import at.hillstromeeg.ui.CalculatorPage
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        CalculatorPage()
    }
}
