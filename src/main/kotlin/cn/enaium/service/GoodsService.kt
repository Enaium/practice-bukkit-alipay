package cn.enaium.service

import cn.enaium.model.entity.Goods
import cn.enaium.model.entity.name
import cn.enaium.utility.sql
import org.babyfish.jimmer.sql.kt.ast.expression.eq

object GoodsService {
    /**
     * Find all goods
     */
    fun findAll(): List<Goods> {
        return sql.createQuery(Goods::class) {
            select(table)
        }.execute()
    }

    /**
     * Find goods by name
     */
    fun findByName(name: String): Goods? {
        return sql.createQuery(Goods::class) {
            where(table.name eq name)
            select(table)
        }.fetchOneOrNull()
    }
}