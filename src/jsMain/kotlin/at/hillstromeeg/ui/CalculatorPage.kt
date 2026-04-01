package at.hillstromeeg.ui

import androidx.compose.runtime.*
import at.hillstromeeg.model.SavingCalculator
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.*

@Composable
fun CalculatorPage() {
    var energiebedarfKwh by remember { mutableStateOf(3500.0) }
    var aktuellerPreisProKwh by remember { mutableStateOf(0.25) }
    var eegPreisProKwh by remember { mutableStateOf(0.12) }
    var abdeckungProzent by remember { mutableStateOf(0.30) }

    val calculator = SavingCalculator()
    val result = calculator.calculate(
        energiebedarfKwh = energiebedarfKwh,
        aktuellerPreisProKwh = aktuellerPreisProKwh,
        eegPreisProKwh = eegPreisProKwh,
        abdeckungProzent = abdeckungProzent
    )

    Div({ classes("container") }) {
        H1 { Text("EEG Ersparnisrechner") }
        P { Text("Berechnen Sie Ihre potenzielle Ersparnis durch die Teilnahme an der Hillstrom EEG.") }

        Div({ classes("calculator-grid") }) {
            // Input section
            Div({ classes("input-section") }) {
                FormGroup("Jährlicher Stromverbrauch (kWh)", energiebedarfKwh) {
                    energiebedarfKwh = it
                }
                FormGroup("Aktueller Strompreis (€/kWh)", aktuellerPreisProKwh) {
                    aktuellerPreisProKwh = it
                }
                FormGroup("EEG Strompreis (€/kWh)", eegPreisProKwh) {
                    eegPreisProKwh = it
                }
                FormGroup("Abdeckung durch EEG (%)", abdeckungProzent * 100.0) {
                    abdeckungProzent = it / 100.0
                }
            }

            // Results section
            Div({ classes("results-section") }) {
                H3 { Text("Ihre Ersparnis") }
                ResultRow("Brutto Ersparnis / Jahr", result.bruttoErsparnis)
                ResultRow("Mitgliedsgebühr / Jahr", result.mitgliedsgebuehr)
                ResultRow("Einschreibegebühr (einmalig)", result.einschreibegebuehr)
                
                Hr()
                
                Div({ classes("net-result", "highlight") }) {
                    Span { Text("Netto Ersparnis (1. Jahr): ") }
                    Span { Text("${result.nettoErsparnisJahr1.format(2)} €") }
                }
                Div({ classes("net-result") }) {
                    Span { Text("Netto Ersparnis (Folgejahre): ") }
                    Span { Text("${result.nettoErsparnisJahr2.format(2)} €") }
                }
            }
        }
    }
}

@Composable
fun FormGroup(label: String, value: Double, onValueChange: (Double) -> Unit) {
    // Lokaler String-State, um die Benutzereingabe (inkl. Komma) zu erlauben, ohne dass sie sofort überschrieben wird
    // Wir formatieren den Double so, dass keine unnötigen Nullen am Ende stehen (z.B. 3500 statt 3500.0)
    val formattedInitial = if (value % 1.0 == 0.0) value.toInt().toString() else value.toString().replace('.', ',')
    var textValue by remember(value) { mutableStateOf(formattedInitial) }

    Div({ classes("form-group") }) {
        Label(forId = null) { Text(label) }
        Input(type = InputType.Text) {
            classes("form-control")
            value(textValue)
            onInput { event ->
                val rawValue = event.value
                val newValueString = rawValue.replace(',', '.')
                textValue = rawValue // Den originalen Text anzeigen (z.B. mit Komma)
                
                val parsedValue = newValueString.toDoubleOrNull()
                if (parsedValue != null) {
                    onValueChange(parsedValue)
                }
            }
            onKeyDown { event ->
                // Support für Pfeiltasten (Up/Down) wie bei InputType.Number
                val step = if (value < 1.0) 0.01 else 1.0
                when (event.key) {
                    "ArrowUp" -> {
                        onValueChange(value + step)
                        event.preventDefault()
                    }
                    "ArrowDown" -> {
                        onValueChange(value - step)
                        event.preventDefault()
                    }
                }
            }
        }
    }
}

@Composable
fun ResultRow(label: String, value: Double) {
    Div({ classes("result-row") }) {
        Span { Text(label) }
        Span { Text("${value.format(2)} €") }
    }
}

fun Double.format(digits: Int): String = this.asDynamic().toFixed(digits).toString()
