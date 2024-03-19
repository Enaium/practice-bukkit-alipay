package cn.enaium.utility

import cn.enaium.model.entity.TradeStatus
import cn.enaium.model.interceptor.BaseEntityDraftInterceptor
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.pool.HikariPool
import org.babyfish.jimmer.sql.EnumItem
import org.babyfish.jimmer.sql.dialect.PostgresDialect
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.newKSqlClient
import org.babyfish.jimmer.sql.runtime.ScalarProvider
import org.postgresql.util.PGobject
import kotlin.reflect.KClass

val sql: KSqlClient = newKSqlClient {
    setConnectionManager {
        HikariPool(HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
            username = "postgres"
            password = "postgres"
            maximumPoolSize = 10
            connectionTimeout = 30000
            isAutoCommit = true
        }).connection.use {
            proceed(it)
        }
    }
    setDialect(PostgresDialect())
    addDraftInterceptor(BaseEntityDraftInterceptor())
    addScalarProvider(EnumScalar(TradeStatus::class))
}


private class EnumScalar<E : Enum<E>>(val enumType: KClass<E>) :
    ScalarProvider<E, PGobject>(enumType.java, PGobject::class.java) {
    override fun toScalar(sqlValue: PGobject): E {
        return enumType.java.declaredFields.firstOrNull {
            it.getAnnotation(EnumItem::class.java)?.name == sqlValue.value
        }?.let { java.lang.Enum.valueOf(enumType.java, it.name) } ?: throw IllegalArgumentException()
    }

    override fun toSql(scalarValue: E): PGobject {
        return PGobject().apply {
            type = enumType.simpleName?.toSnakeCase() ?: ""
            value = enumType.java.declaredFields.firstOrNull {
                it.name == scalarValue.name
            }?.getAnnotation(EnumItem::class.java)?.name ?: ""
        }
    }

    fun String.toSnakeCase(): String {
        return replace(Regex("(?<!^)([A-Z])"), "_$1").lowercase()
    }
}