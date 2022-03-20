package data

import org.bson.Document

class User(
    private val id: Int,
    private val name: String,
    private val currency: Currency
) : DocumentCastable {
    override fun toDocument(): Document {
        return Document(
            mapOf(
                "id" to id,
                "name" to name,
                "currency" to currency.name
            )
        )
    }
}