package server

import com.mongodb.rx.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.rx.client.MongoClients
import data.Currency
import data.Item
import data.User
import org.bson.Document
import rx.Observable

class MongoReactiveDatabase {

    fun user(user: User): Observable<Boolean> = toCollection(user.toDocument(), users)

    fun item(item: Item): Observable<Boolean> = toCollection(item.toDocument(), items)

    private val database = MongoClients
        .create("mongodb://localhost:27017")
        .getDatabase("webCatalog")

    private val users: MongoCollection<Document> = database.getCollection("users")

    private val items: MongoCollection<Document> = database.getCollection("items")

    private fun toCollection(
        doc: Document,
        collection: MongoCollection<Document>
    ): Observable<Boolean> = collection
        .find(Filters.eq("id", doc.getInteger("id")))
        .toObservable()
        .singleOrDefault(null)
        .flatMap { gotParameter ->
            when (gotParameter) {
                null -> {
                    collection
                        .insertOne(doc)
                        .asObservable()
                        .isEmpty
                        .map { !it }
                }
                else -> Observable.just(false)
            }
        }

    fun itemsByUserId(id: Int): Observable<Item> = users
        .find(Filters.eq("id", id))
        .toObservable()
        .map { getCurrency(it) }
        .flatMap { currency -> items
            .find()
            .toObservable()
            .map {
                val item = Item(it)
                item.changeCurrency(currency)
                item
            }
        }

    private fun getCurrency(doc: Document): Currency = Currency.valueOf(doc.getString("currency"))
}