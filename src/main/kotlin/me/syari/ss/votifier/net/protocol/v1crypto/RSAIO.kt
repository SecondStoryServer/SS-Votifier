/*
 * Copyright (C) 2011 Vex Software LLC
 * This file is part of Votifier.
 * 
 * Votifier is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Votifier is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Votifier.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.syari.ss.votifier.net.protocol.v1crypto

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.security.KeyFactory
import java.security.KeyPair
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

object RSAIO {
    @Throws(Exception::class)
    fun save(directory: File, keyPair: KeyPair) {
        val privateKey = keyPair.private
        val publicKey = keyPair.public
        val publicSpec = X509EncodedKeySpec(publicKey.encoded)
        val privateSpec = PKCS8EncodedKeySpec(privateKey.encoded)
        FileOutputStream("$directory/public.key").use { publicOut ->
            FileOutputStream("$directory/private.key").use { privateOut ->
                publicOut.write(Base64.getEncoder().encode(publicSpec.encoded))
                privateOut.write(Base64.getEncoder().encode(privateSpec.encoded))
            }
        }
    }

    @Throws(IOException::class)
    fun readB64File(directory: File, name: String): ByteArray {
        val f = File(directory, name)
        val contents = Files.readAllBytes(f.toPath())
        var strContents = String(contents)
        strContents = strContents.trim { it <= ' ' }
        return try {
            Base64.getDecoder().decode(strContents)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(
                "Base64 decoding exception: This is probably due to a corrupted file, " + "but in case it isn't, here is a b64 representation of what we read: " + String(
                    Base64.getEncoder().encode(contents), StandardCharsets.UTF_8
                ), e
            )
        }
    }

    @Throws(Exception::class)
    fun load(directory: File): KeyPair {
        val encodedPublicKey = readB64File(directory, "public.key")
        val encodedPrivateKey = readB64File(directory, "private.key")
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKeySpec = X509EncodedKeySpec(encodedPublicKey)
        val publicKey = keyFactory.generatePublic(publicKeySpec)
        val privateKeySpec = PKCS8EncodedKeySpec(encodedPrivateKey)
        val privateKey = keyFactory.generatePrivate(privateKeySpec)
        return KeyPair(publicKey, privateKey)
    }
}