package me.syari.ss.votifier.util

import java.math.BigInteger
import java.security.SecureRandom

object TokenUtil {
    private val RANDOM = SecureRandom()

    fun newToken(): String {
        return BigInteger(130, RANDOM).toString(32)
    }
}