package me.syari.ss.votifier

import me.syari.ss.core.auto.OnEnable
import me.syari.ss.core.command.create.CreateCommand.createCommand
import me.syari.ss.core.command.create.CreateCommand.elementIf
import me.syari.ss.core.command.create.CreateCommand.tab
import me.syari.ss.votifier.BootstrapBuilder.reload
import me.syari.ss.votifier.Main.Companion.plugin
import org.bukkit.command.ConsoleCommandSender

object CommandCreator: OnEnable {
    override fun onEnable() {
        createCommand(plugin, "votifier", "SS-Votifier", tab { sender, _ ->
            elementIf(sender is ConsoleCommandSender, "reload")
        }) { sender, args ->
            when (args.whenIndex(0)) {
                "reload" -> {
                    if (sender !is ConsoleCommandSender) sendError("コンソールからのみ実行可能です")
                    sendWithPrefix("コンフィグをリロードします")
                    reload(sender)
                }
                else -> {
                    sendHelp(
                        "votifier reload" to "コンフィグをリロードします"
                    )
                }
            }
        }
    }
}