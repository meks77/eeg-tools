package at.hillstromeeg

import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        Div {
            H1 {
                Text("Willkommen bei EEG Tools")
            }
            Div {
                Text("Einfache Berechnung der Ersparnis für Verbraucher")
            }
        }
    }
}
