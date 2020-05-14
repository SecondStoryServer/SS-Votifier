package me.syari.ss.votifier.net.protocol

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.CorruptedFrameException
import io.netty.handler.codec.MessageToMessageDecoder
import me.syari.ss.votifier.BootstrapBuilder
import me.syari.ss.votifier.model.Vote
import me.syari.ss.votifier.net.VotifierSession
import me.syari.ss.votifier.util.JsonUtil.fromJson
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.Key
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object VotifierProtocol2Decoder: MessageToMessageDecoder<String>() {
    private val RANDOM = SecureRandom()

    @Throws(CorruptedFrameException::class, RuntimeException::class)
    override fun decode(ctx: ChannelHandlerContext, s: String, list: MutableList<Any>) {
        val voteMessage = fromJson(s)
        val session = ctx.channel().attr(VotifierSession.KEY).get()
        val payload = voteMessage["payload"].asString
        val votePayload = fromJson(payload)
        if (votePayload["challenge"].asString != session.challenge) {
            throw CorruptedFrameException("Challenge is not valid")
        }
        val key = BootstrapBuilder.key
        val sigHash = voteMessage["signature"].asString
        val sigBytes = Base64.getDecoder().decode(sigHash)
        if (!hmacEqual(sigBytes, payload.toByteArray(StandardCharsets.UTF_8), key)) {
            throw CorruptedFrameException("Signature is not valid (invalid token?)")
        }
        if (votePayload["username"].asString.length > 16) {
            throw CorruptedFrameException("Username too long")
        }
        val vote = Vote(votePayload)
        list.add(vote)
        ctx.pipeline().remove(this)
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
    private fun hmacEqual(sig: ByteArray, message: ByteArray, key: Key): Boolean {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(key)
        val calculatedSig = mac.doFinal(message)
        val randomKey = ByteArray(32)
        RANDOM.nextBytes(randomKey)
        val mac2 = Mac.getInstance("HmacSHA256")
        mac2.init(SecretKeySpec(randomKey, "HmacSHA256"))
        val clientSig = mac2.doFinal(sig)
        mac2.reset()
        val realSig = mac2.doFinal(calculatedSig)
        return MessageDigest.isEqual(clientSig, realSig)
    }
}