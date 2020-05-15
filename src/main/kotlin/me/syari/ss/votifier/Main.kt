package me.syari.ss.votifier

import me.syari.ss.core.auto.OnDisable
import me.syari.ss.core.auto.OnEnable.Companion.register
import org.bukkit.plugin.java.JavaPlugin

class Main: JavaPlugin() {
    companion object {
        internal lateinit var plugin: JavaPlugin
    }

    override fun onEnable() {
        plugin = this
        register(CommandCreator, BootstrapBuilder)
    }

    override fun onDisable() {
        OnDisable.register(BootstrapBuilder)
    }
}