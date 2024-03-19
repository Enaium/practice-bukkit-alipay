package cn.enaium.service

import cn.enaium.model.entity.Trade
import cn.enaium.model.entity.TradeStatus
import com.alipay.easysdk.factory.Factory

object AlipayService {
    fun pay(trade: Trade): String {
        val pay = Factory.Payment.Page()
            .pay(trade.goods.name, trade.id.toString(), trade.goods.price.toString(), "https://enaium.cn/")
        pay.validate()
        return pay.body
    }

    fun query(trade: Trade): TradeStatus {
        val query = Factory.Payment.Common().query(trade.id.toString())
        if (query.getTradeStatus() == null) {
            return TradeStatus.WAIT_BUYER_PAY
        }
        return TradeStatus.valueOf(query.getTradeStatus())
    }
}