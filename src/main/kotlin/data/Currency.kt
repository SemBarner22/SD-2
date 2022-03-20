package data

enum class Currency(private val amount: Double) {
    EUR(110.0),
    RUB(1.0),
    USD(100.0);

    fun convertTo(second: Currency, count: Double): Double {
        return count * (this.amount / second.amount)
    }
}