package cn.enaium.model.entity

import java.util.UUID
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.ManyToOne
import org.babyfish.jimmer.sql.Table

@Entity
@Table(name = "trade")
public interface Trade : BaseEntity {
    public val playerId: UUID

    @ManyToOne
    public val goods: Goods

    public val status: TradeStatus
}
