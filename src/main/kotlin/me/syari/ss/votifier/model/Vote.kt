package me.syari.ss.votifier.model

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.util.Arrays
import java.util.Base64

data class Vote(
    val serviceName: String,
    val username: String,
    val address: String,
    val timeStamp: String,
    val additionalData: ByteArray?
) {
    constructor(jsonObject: JsonObject): this(
        jsonObject["serviceName"].asString,
        jsonObject["username"].asString,
        jsonObject["address"].asString,
        getTimestamp(jsonObject["timestamp"]),
        jsonObject.additionalData
    )

    override fun toString(): String {
        val data = if (additionalData != null) Base64.getEncoder().encodeToString(additionalData) else "null"
        return "Vote (from:$serviceName username:$username address:$address timeStamp:$timeStamp additionalData:$data)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val vote = other as Vote
        if (serviceName != vote.serviceName) return false
        if (username != vote.username) return false
        if (address != vote.address) return false
        return if (timeStamp != vote.timeStamp) false else Arrays.equals(additionalData, vote.additionalData)
    }

    override fun hashCode(): Int {
        var result = serviceName.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + timeStamp.hashCode()
        return result
    }

    companion object {
        private fun getTimestamp(jsonElement: JsonElement): String {
            return try {
                jsonElement.asLong.toString()
            } catch (e: Exception) {
                jsonElement.asString
            }
        }

        inline val JsonObject.additionalData
            get() = if (has("additionalData")) Base64.getDecoder().decode(get("additionalData").asString) else null
    }

}