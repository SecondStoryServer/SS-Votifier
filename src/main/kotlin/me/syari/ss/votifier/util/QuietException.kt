package me.syari.ss.votifier.util

class QuietException(message: String): Exception(message) {
    @Synchronized
    override fun fillInStackTrace(): Throwable? {
        return null
    }
}