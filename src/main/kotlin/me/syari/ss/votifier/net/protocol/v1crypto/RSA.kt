package me.syari.ss.votifier.net.protocol.v1crypto

import java.security.PrivateKey
import javax.crypto.Cipher

object RSA {
    @Throws(Exception::class)
    fun decrypt(data: ByteArray, key: PrivateKey): ByteArray {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, key)
        return cipher.doFinal(data)
    }
}