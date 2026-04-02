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
        val erwarteteBruttoErsparnis = 1200.0 * (0.165 - 0.10) // 78.0
        val erwarteteNetznutzungErsparnis = 1200.0 * (2.952 / 100.0) // 35.424
        val erwarteteElektrizitaetsabgabeErsparnis = 1200.0 * (0.1 / 100.0) // 1.2
        val erwarteteFoerderbeitragNetznutzungErsparnis = 1200.0 * (0.583 / 100.0) // 6.996
        val erwarteteFoerderbeitragNetzverlustErsparnis = 1200.0 * (0.037 / 100.0) // 0.444
        
        val erwarteteGesamtErsparnis = erwarteteBruttoErsparnis + erwarteteNetznutzungErsparnis + 
                erwarteteElektrizitaetsabgabeErsparnis + erwarteteFoerderbeitragNetznutzungErsparnis + 
                erwarteteFoerderbeitragNetzverlustErsparnis // 78 + 35.424 + 1.2 + 6.996 + 0.444 = 122.064
        
        val erwarteteNettoErsparnisJahr1 = erwarteteGesamtErsparnis - 15.0 - 10.0 // 97.064
        val erwarteteNettoErsparnisJahr2 = erwarteteGesamtErsparnis - 10.0 // 112.064

        assertEquals(erwarteteBruttoErsparnis, result.bruttoErsparnis, 0.001)
        assertEquals(erwarteteNetznutzungErsparnis, result.netznutzungErsparnis, 0.001)
        assertEquals(erwarteteElektrizitaetsabgabeErsparnis, result.elektrizitaetsabgabeErsparnis, 0.001)
        assertEquals(erwarteteFoerderbeitragNetznutzungErsparnis, result.foerderbeitragNetznutzungErsparnis, 0.001)
        assertEquals(erwarteteFoerderbeitragNetzverlustErsparnis, result.foerderbeitragNetzverlustErsparnis, 0.001)
        assertEquals(erwarteteGesamtErsparnis, result.gesamtErsparnis, 0.001)
        assertEquals(erwarteteNettoErsparnisJahr1, result.nettoErsparnisJahr1, 0.001)
        assertEquals(erwarteteNettoErsparnisJahr2, result.nettoErsparnisJahr2, 0.001)
    }
}
