package at.hillstromeeg

import at.hillstromeeg.model.SavingCalculator
import kotlin.test.Test
import kotlin.test.assertEquals

class SavingCalculatorTest {
    private val calculator = SavingCalculator()

    @Test
    fun testErsparnisBerechnung() {
        val energiebedarf = 3000.0
        val aktuellerPreis = 0.165
        val abdeckung = 0.40

        val result = calculator.calculate(
            energiebedarfKwh = energiebedarf,
            aktuellerPreisProKwh = aktuellerPreis,
            abdeckungProzent = abdeckung
        )
        val erwarteteBruttoErsparnis = 78.0 // energiebedarf * abdeckung * (aktuellerPreis - 0.10)
        val erwarteteNettoErsparnisJahr1 = 53.0 // 78 - Einschreibgebühr(15) - Mitgliedsgebühr(10)
        val erwarteteNettoErsparnisJahr2 = 68.0 // 78 - Mitgliedsgebühr(10)
        assertEquals(erwarteteBruttoErsparnis, result.bruttoErsparnis, "Die Brutto-Ersparnis sollte 300 Euro betragen")
        assertEquals(erwarteteNettoErsparnisJahr1, result.nettoErsparnisJahr1, "Die Netto-Ersparnis im 1. Jahr sollte 53 Euro betragen")
        assertEquals(erwarteteNettoErsparnisJahr2, result.nettoErsparnisJahr2, "Die Netto-Ersparnis im 2. Jahr sollte 68 Euro betragen")
    }
}
