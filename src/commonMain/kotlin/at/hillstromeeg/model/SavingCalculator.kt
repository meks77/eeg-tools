package at.hillstromeeg.model

data class SavingResult(
    val bruttoErsparnis: Double,
    val mitgliedsgebuehr: Double = 10.0,
    val einschreibegebuehr: Double = 15.0
) {
    val nettoErsparnisJahr1: Double = bruttoErsparnis - mitgliedsgebuehr - einschreibegebuehr
    val nettoErsparnisJahr2: Double = bruttoErsparnis - mitgliedsgebuehr
}

class SavingCalculator {
    fun calculate(
        energiebedarfKwh: Double,
        aktuellerPreisProKwh: Double,
        eegPreisProKwh: Double = 0.10,
        abdeckungProzent: Double
    ): SavingResult {
        val brutto = energiebedarfKwh * abdeckungProzent * (aktuellerPreisProKwh - eegPreisProKwh)
        return SavingResult(bruttoErsparnis = brutto)
    }
}
