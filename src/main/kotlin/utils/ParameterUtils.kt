package utils

import data.Currency
import exceptions.NoSuchParameter
import org.bson.Document

object ParameterUtils {

    fun getCurrency(doc: Document): Currency = Currency.valueOf(doc.getString("currency"))

    fun getCurrency(value: Map<String, List<String>>) = Currency.valueOf(getParameterByName("currency", value))

    fun getParameterByName(name: String, value: Map<String, List<String>>): String {
        return (value[name] ?: throw NoSuchParameter("No such parameter"))[0]
    }

}