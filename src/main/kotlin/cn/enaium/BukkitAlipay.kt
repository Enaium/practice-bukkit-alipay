package cn.enaium

import cn.enaium.model.entity.TradeStatus
import cn.enaium.service.AlipayService
import cn.enaium.service.GoodsService
import cn.enaium.service.TradeService
import cn.enaium.utility.toUUID
import com.alipay.easysdk.factory.Factory
import com.alipay.easysdk.kernel.Config
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.GreedyStringArgument
import dev.jorel.commandapi.executors.CommandExecutor
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class BukkitAlipay : JavaPlugin() {
    override fun onEnable() {
        Factory.setOptions(Config().apply {
            protocol = "https"
            gatewayHost = "openapi-sandbox.dl.alipaydev.com"
            signType = "RSA2"
            appId = System.getenv("ALIPAY_APP_ID")
            merchantPrivateKey = System.getenv("ALIPAY_MERCHANT_PRIVATE_KEY")
            alipayPublicKey = System.getenv("ALIPAY_PUBLIC_KEY")
        })

        CommandAPICommand("alipay")
            .withSubcommands(
                CommandAPICommand("place")
                    .withArguments(GreedyStringArgument("goods"))
                    .executes(CommandExecutor { sender, args ->

                        val goods = GoodsService.findByName(args["goods"] as String)
                        if (goods == null) {
                            sender.sendMessage(Component.text("Goods not found"))
                            return@CommandExecutor
                        }

                        if (sender is Player) {
                            val create = TradeService.create(goods, sender.uniqueId)
                            sender.sendMessage(
                                Component.text("Trade created:${create}")
                                    .clickEvent(ClickEvent.suggestCommand("/alipay pay $create"))
                            )
                        }
                    })
            ).withSubcommands(
                CommandAPICommand("goods")
                    .executes(CommandExecutor { sender, args ->
                        sender.openBook(
                            Book.book(
                                Component.text("Goods"),
                                Component.text("Goods"),
                                Component.text(GoodsService.findAll().joinToString(",") { it.name })
                            )
                        )
                    })
            )
            .withSubcommands(
                CommandAPICommand("pay")
                    .withArguments(GreedyStringArgument("trade-id"))
                    .executes(CommandExecutor { sender, args ->
                        if (sender is Player) {
                            val trade = TradeService.findById((args["trade-id"] as String).toUUID())
                            if (trade == null) {
                                sender.sendMessage(Component.text("Trade not found"))
                                return@CommandExecutor
                            }

                            if (trade.playerId != sender.uniqueId) {
                                sender.sendMessage(Component.text("Trade not yours"))
                                return@CommandExecutor
                            }

                            val html = AlipayService.pay(trade)

                            sender.sendMessage(
                                Component.text("Click here to copy the payment link")
                                    .clickEvent(
                                        ClickEvent.copyToClipboard(
                                            "data:text/html;base64,${Base64.getEncoder().encode(html.toByteArray()).toString(Charsets.UTF_8)}"
                                        )
                                    )
                            )
                        }
                    })
            )
            .withSubcommands(
                CommandAPICommand("take")
                    .withArguments(GreedyStringArgument("trade-id"))
                    .executes(CommandExecutor { sender, args ->
                        if (sender is Player) {
                            val trade = TradeService.findById((args["trade-id"] as String).toUUID())
                            if (trade == null) {
                                sender.sendMessage(Component.text("Trade not found"))
                                return@CommandExecutor
                            }

                            if (trade.playerId != sender.uniqueId) {
                                sender.sendMessage(Component.text("Trade not yours"))
                                return@CommandExecutor
                            }

                            val tradeStatus = TradeService.updateStatusById(
                                trade.id,
                                when (trade.status) {
                                    TradeStatus.WAIT_BUYER_PAY -> {
                                        AlipayService.query(trade)
                                    }

                                    else -> {
                                        trade.status
                                    }
                                }
                            )

                            if (tradeStatus == TradeStatus.WAIT_BUYER_PAY) {
                                sender.sendMessage(Component.text("Trade not paid"))
                                return@CommandExecutor
                            }

                            if (tradeStatus == TradeStatus.TRADE_CLOSED) {
                                sender.sendMessage(Component.text("Trade closed"))
                                return@CommandExecutor
                            }

                            if (tradeStatus == TradeStatus.TRADE_SUCCESS) {
                                TradeService.updateStatusById(trade.id, TradeStatus.TRADE_FINISHED)


                                val material = Material.getMaterial(trade.goods.name.uppercase()) ?: Material.DIAMOND

                                sender.openInventory(
                                    Bukkit.createInventory(
                                        sender,
                                        9,
                                        Component.text(trade.goods.name)
                                    ).apply {
                                        addItem(ItemStack(material))
                                    }
                                )

                                sender.sendMessage(Component.text("Trade finished"))
                                return@CommandExecutor
                            }

                            if (tradeStatus == TradeStatus.TRADE_FINISHED) {
                                sender.sendMessage(Component.text("Trade finished"))
                                return@CommandExecutor
                            }
                        }
                    })
            )
            .register()
    }
}