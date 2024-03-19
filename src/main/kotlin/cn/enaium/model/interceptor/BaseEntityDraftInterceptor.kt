package cn.enaium.model.interceptor

import cn.enaium.model.entity.BaseEntity
import cn.enaium.model.entity.BaseEntityDraft
import cn.enaium.model.entity.BaseEntityProps.CREATED_TIME
import org.babyfish.jimmer.ImmutableObjects
import org.babyfish.jimmer.sql.DraftInterceptor
import java.time.LocalDateTime

class BaseEntityDraftInterceptor : DraftInterceptor<BaseEntity, BaseEntityDraft> {
    override fun beforeSave(draft: BaseEntityDraft, original: BaseEntity?) {
        if (!ImmutableObjects.isLoaded(draft, CREATED_TIME)) {
            draft.modifiedTime = LocalDateTime.now()
        }

        if (original == null && !ImmutableObjects.isLoaded(draft, CREATED_TIME)) {
            draft.createdTime = LocalDateTime.now()
        }
    }
}