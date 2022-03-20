package server

import io.netty.handler.codec.http.HttpResponseStatus
import data.Item
import data.User
import exceptions.NoIdException
import rx.Observable
import utils.ParameterUtils.getCurrency
import utils.ParameterUtils.getParameterByName

class MongoController(private val mongoReactiveDatabase: MongoReactiveDatabase) {

    fun process(path: String, value: Map<String, List<String>>): Response = when(path) {
        USER_ADD -> addUser(value)
        ITEM_ADD -> addItem(value)
        ITEMS_GET -> getItems(value)
        else -> Response(HttpResponseStatus.BAD_REQUEST, Observable.just("Did not find such page!"))
    }

    private fun getItems(value: Map<String, List<String>>): Response =
        createOkResponse(mongoReactiveDatabase.itemsByUserId(getParameterByName("id", value).toInt()).map { toString() })

    private fun addUser(value: Map<String, List<String>>): Response = createOkResponse(
        mongoReactiveDatabase.user(
            User(
                getParameterByName("id", value).toIntOrNull() ?: throw NoIdException("Add parameter id to User!"),
                getParameterByName("name", value),
                getCurrency(value)
            )
        ).map { toString() })

    private fun addItem(value: Map<String, List<String>>): Response =
        createOkResponse(mongoReactiveDatabase.item(
            Item(
                getParameterByName("id", value).toIntOrNull() ?: throw NoIdException("Add parameter id to Item!"),
                getParameterByName("name", value),
                getParameterByName("price", value).toDouble(),
                getCurrency(value)
            )
        ).map { toString() })

    private fun createOkResponse(message: Observable<String>) = Response(HttpResponseStatus.OK, message)
    companion object {
        private const val USER_ADD = "add-user"
        private const val ITEM_ADD = "add-item"
        private const val ITEMS_GET = "get-items"
    }
}