package com.ssafy.a705.domain.group._memo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupMemo is a Querydsl query type for GroupMemo
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroupMemo extends EntityPathBase<GroupMemo> {

    private static final long serialVersionUID = 425418384L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroupMemo groupMemo = new QGroupMemo("groupMemo");

    public final com.ssafy.a705.global.common.QBaseEntity _super = new com.ssafy.a705.global.common.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final com.ssafy.a705.domain.group._member.entity.QGroupMember groupMember;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public QGroupMemo(String variable) {
        this(GroupMemo.class, forVariable(variable), INITS);
    }

    public QGroupMemo(Path<? extends GroupMemo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGroupMemo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGroupMemo(PathMetadata metadata, PathInits inits) {
        this(GroupMemo.class, metadata, inits);
    }

    public QGroupMemo(Class<? extends GroupMemo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.groupMember = inits.isInitialized("groupMember") ? new com.ssafy.a705.domain.group._member.entity.QGroupMember(forProperty("groupMember"), inits.get("groupMember")) : null;
    }

}

