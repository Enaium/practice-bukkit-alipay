package cn.enaium.model.entity

import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.MappedSuperclass
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator
import java.time.LocalDateTime
import java.util.*

@MappedSuperclass
public interface BaseEntity {
    public val createdTime: LocalDateTime

    public val modifiedTime: LocalDateTime

    @Id
    @GeneratedValue(generatorType = UUIDIdGenerator::class)
    public val id: UUID
}
