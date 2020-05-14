package me.syari.ss.votifier.net.protocol.v1crypto

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.RSAKeyGenParameterSpec

object RSAKeygen {
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