package me.syari.ss.votifier.model

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.syari.ss.votifier.Main.Companion.plugin
import java.util.Arrays
import java.util.Base64

data class Vote(
    val serviceName: String,
    val username: String,
    val address: String,
    val timeStamp: String,
    val additionalData: ByteArray?
) {
    @Suppress("DEPRECATION")
    val player by lazy {
        val player = plugin.server.getOfflinePlayer(username)
        if (player.name.equals(username, true)) player else null
    }

    override fun toString(): String {
        val data = if (additionalData != null) Base64.getEncoder().encodeToString(additionalData) else "null"
        return "Vote (from:$serviceName username:$username address:$address timeStamp:$timeStamp additionalData:$data)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vote) return false
        if (serviceName != other.serviceName) return false
        if (username != other.username) return false
        if (address != other.address) return false
        if (timeStamp != other.timeStamp) return false
        return Arrays.equals(additionalData, other.additionalData)
    }

    override fun hashCode(): Int {
        var result = serviceName.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + timeStamp.hashCode()
        return result
    }

    companion object {
        fun from(jsonObject: JsonObject): Vote? {
            return try {
                val serviceName = jsonObject["serviceName"].asString
                val userName = jsonObject["username"].asString
                val address = jsonObject["address"].asString
                val timeStamp = jsonObject["timestamp"].asTimeStamp
                val additionalData = jsonObject.additionalData
                Vote(serviceName, userName, address, timeStamp, additionalData)
            } catch (ex: Exception) {
                null
            }
        }

        private inline val JsonElement.asTimeStamp
            get() = try {
                asLong.toString()
            } catch (ex: Exception) {
                asString
            }

        private inline val JsonObject.additionalData
            get() = if (has("additionalData")) Base64.getDecoder().decode(get("additionalData").asString) else null
    }

}