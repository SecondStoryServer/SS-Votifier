package me.syari.ss.votifier.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject

object JsonUtil {
    private val gson: Gson = GsonBuilder().create()

    fun toJson(jsonObject: JsonObject): String {
        return gson.toJson(jsonObject)
    }

    fun fromJson(payload: String): JsonObject {
        return gson.fromJson(payload, JsonObject::class.java)
    }
}