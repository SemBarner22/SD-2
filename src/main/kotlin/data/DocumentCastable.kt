package data

import org.bson.Document

interface DocumentCastable {
    fun toDocument(): Document
}