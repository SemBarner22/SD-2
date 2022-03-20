package server

import io.netty.handler.codec.http.HttpResponseStatus
import data.Currency
import data.Item
import data.User
import exceptions.NoIdException
import exceptions.NoSuchParameter
import rx.Observable

class MongoController(private val mongoReactiveDatabase: MongoReactiveDatabase) {

    fun process(path: String, value: Map<String, List<String>>): Response = when(path) {
        USER_ADD -> addUser(value)
        ITEM_ADD -> addItem(value)
        ITEMS_GET -> getItems(value)
        else -> Response(HttpResponseStatus.BAD_REQUEST, Observable.just("Did not find such page!"))
    }

    private fun get(name: String, value: Map<String, List<String>>): String {
        return (value[name] ?: throw NoSuchParameter("No such parameter"))[0]
    }

    private fun getItems(value: Map<String, List<String>>): Response =
        createOkResponse(mongoReactiveDatabase.itemsByUserId(get("id", value).toInt()).map { toString() })

    private fun addUser(value: Map<String, List<String>>): Response {
        val user = User(
            get("id", value).toIntOrNull() ?: throw NoIdException("Add parameter id to User!"),
            get("name", value),
            getCurrency(value)
        )
        return createOkResponse( mongoReactiveDatabase.user(
            user
        ).map { toString() })
    }

    private fun addItem(value: Map<String, List<String>>): Response {
        val item = Item(
            get("id", value).toIntOrNull() ?: throw NoIdException("Add parameter id to Item!"),
            get("name", value),
            get("price", value).toDouble(),
            getCurrency(value)
        )
        return createOkResponse(mongoReactiveDatabase.item(
            item
        ).map { toString() })
    }

    private fun getCurrency(value: Map<String, List<String>>) = Currency.valueOf(get("currency", value))

    private fun createOkResponse(message: Observable<String>) = Response(HttpResponseStatus.OK, message)
    companion object {
        private const val USER_ADD = "add-user"
        private const val ITEM_ADD = "add-item"
        private const val ITEMS_GET = "get-items"
    }
}