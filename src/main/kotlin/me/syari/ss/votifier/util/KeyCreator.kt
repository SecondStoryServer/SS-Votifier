package me.syari.ss.votifier.util

import java.nio.charset.StandardCharsets
import java.security.Key
import javax.crypto.spec.SecretKeySpec

object KeyCreator {
    fun createKeyFrom(token: String): Key {
        return SecretKeySpec(token.toByteArray(StandardCharsets.UTF_8), "HmacSHA256")
    }
}