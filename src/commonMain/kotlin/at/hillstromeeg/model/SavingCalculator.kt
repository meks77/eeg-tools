package at.hillstromeeg.model

data class SavingResult(
    val bruttoErsparnis: Double,
    val netznutzungErsparnis: Double = 0.0,
    val elektrizitaetsabgabeErsparnis: Double = 0.0,
    val foerderbeitragNetznutzungErsparnis: Double = 0.0,
    val foerderbeitragNetzverlustErsparnis: Double = 0.0,
    val mitgliedsgebuehr: Double = 10.0,
    val einschreibegebuehr: Double = 15.0
) {
    val gesamtErsparnis: Double = bruttoErsparnis + netznutzungErsparnis + elektrizitaetsabgabeErsparnis + 
            foerderbeitragNetznutzungErsparnis + foerderbeitragNetzverlustErsparnis
    val nettoErsparnisJahr1: Double = gesamtErsparnis - mitgliedsgebuehr - einschreibegebuehr
    val nettoErsparnisJahr2: Double = gesamtErsparnis - mitgliedsgebuehr
}

class SavingCalculator {
    companion object {
        const val NETZNUTZUNG_CENT_PER_KWH = 2.952
        const val ELEKTRIZITAETSABGABE_CENT_PER_KWH = 0.1
        const val FOERDERBEITRAG_NETZNUTZUNG_CENT_PER_KWH = 0.583
        const val FOERDERBEITRAG_NETZVERLUST_CENT_PER_KWH = 0.037
    }

    fun calculate(
        energiebedarfKwh: Double,
        aktuellerPreisProKwh: Double,
        eegPreisProKwh: Double = 0.10,
        abdeckungProzent: Double
    ): SavingResult {
        val eegKwh = energiebedarfKwh * abdeckungProzent
        val brutto = eegKwh * (aktuellerPreisProKwh - eegPreisProKwh)
        
        return SavingResult(
            bruttoErsparnis = brutto,
            netznutzungErsparnis = eegKwh * (NETZNUTZUNG_CENT_PER_KWH / 100.0),
            elektrizitaetsabgabeErsparnis = eegKwh * (ELEKTRIZITAETSABGABE_CENT_PER_KWH / 100.0),
            foerderbeitragNetznutzungErsparnis = eegKwh * (FOERDERBEITRAG_NETZNUTZUNG_CENT_PER_KWH / 100.0),
            foerderbeitragNetzverlustErsparnis = eegKwh * (FOERDERBEITRAG_NETZVERLUST_CENT_PER_KWH / 100.0)
        )
    }
}
