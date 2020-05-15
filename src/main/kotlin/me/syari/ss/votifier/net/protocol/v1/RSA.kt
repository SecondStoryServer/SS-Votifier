package me.syari.ss.votifier.net.protocol.v1

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.spec.RSAKeyGenParameterSpec
import javax.crypto.Cipher

object RSA {
    @Throws(Exception::class)
    fun decrypt(data: ByteArray, key: PrivateKey): ByteArray {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, key)
        return cipher.doFinal(data)
    }

    @Throws(Exception::class)
    fun generate(bits: Int): KeyPair {
        val keygen = KeyPairGenerator.getInstance("RSA")
        val spec = RSAKeyGenParameterSpec(
            bits, RSAKeyGenParameterSpec.F4
        )
        keygen.initialize(spec)
        return keygen.generateKeyPair()
    }
}