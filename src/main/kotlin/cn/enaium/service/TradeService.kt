package cn.enaium.service

import cn.enaium.model.entity.*
import cn.enaium.utility.sql
import org.babyfish.jimmer.kt.new
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import java.util.*

object TradeService {
    /**
     * Create a trade
     * @param goods Goods
     * @param playerId UUID of the player
     * @return UUID of the trade
     */
    fun create(goods: Goods, playerId: UUID): UUID {

        // Check if the player has a trade with the same goods and status is WAIT_BUYER_PAY
        sql.createQuery(Trade::class) {
            where(table.goods.name.eq(goods.name))
            where(table.playerId.eq(playerId))
            where(table.status.eq(TradeStatus.WAIT_BUYER_PAY))
            select(table.id)
        }.fetchOneOrNull()?.let {
            return it
        }


        // Insert a new trade
        return sql.insert(new(Trade::class).by {
            this.goodsId = goods.id
            this.playerId = playerId
            this.status = TradeStatus.WAIT_BUYER_PAY
        }).modifiedEntity.id
    }

    /**
     * Find trade by id
     * @param id UUID of the trade
     *
     * @return Trade or null
     */
    fun findById(id: UUID): Trade? {
        return sql.createQuery(Trade::class) {
            where(table.id eq id)
            select(table.fetchBy {
                allScalarFields()
                goods {
                    name()
                    price()
                }
            })
        }.fetchOneOrNull()
    }

    /**
     * Update trade status by id
     * @param id UUID of the trade
     * @param status TradeStatus
     *
     * @return TradeStatus
     */
    fun updateStatusById(id: UUID, status: TradeStatus): TradeStatus {
        return sql.update(new(Trade::class).by {
            this.id = id
            this.status = status
        }).modifiedEntity.status
    }
}