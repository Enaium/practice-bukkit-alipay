package cn.enaium.model.entity

import java.math.BigDecimal
import kotlin.String
import kotlin.collections.List
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.OneToMany
import org.babyfish.jimmer.sql.Table

@Entity
@Table(name = "goods")
public interface Goods : BaseEntity {
    public val name: String

    public val price: BigDecimal

    @OneToMany(mappedBy = "goods")
    public val trades: List<Trade>
}
