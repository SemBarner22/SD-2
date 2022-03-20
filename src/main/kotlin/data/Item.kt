package data

import org.bson.Document

class Item(
    private val id: Int,
    private val name: String,
    private var price: Double,
    private var currency: Currency
) : DocumentCastable {

    constructor(doc: Document) : this(
        doc.getInteger("id"),
        doc.getString("name"),
        doc.getDouble("price"),
        Currency.valueOf(doc.getString("currency"))
    )

    override fun toDocument(): Document {
        return Document(
            mapOf(
                "id" to id,
                "name" to name,
                "price" to price,
                "currency" to currency.name
            )
        )
    }

    fun changeCurrency(other: Currency) {
        price = currency.convertTo(other, price)
        currency = other
    }
}