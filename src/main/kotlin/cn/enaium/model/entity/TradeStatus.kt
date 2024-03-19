package cn.enaium.model.entity

import org.babyfish.jimmer.sql.EnumItem

enum class TradeStatus {
    @EnumItem(name = "WAIT_BUYER_PAY")
    WAIT_BUYER_PAY,

    @EnumItem(name = "TRADE_CLOSED")
    TRADE_CLOSED,

    @EnumItem(name = "TRADE_SUCCESS")
    TRADE_SUCCESS,

    @EnumItem(name = "TRADE_FINISHED")
    TRADE_FINISHED
}