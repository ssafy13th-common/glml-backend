package com.ssafy.a705.domain.group.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGroup is a Querydsl query type for Group
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroup extends EntityPathBase<Group> {

    private static final long serialVersionUID = -1137948607L;

    public static final QGroup group = new QGroup("group1");

    public final com.ssafy.a705.global.common.QBaseEntity _super = new com.ssafy.a705.global.common.QBaseEntity(this);

    public final StringPath chatRoomId = createString("chatRoomId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final DatePath<java.time.LocalDate> endAt = createDate("endAt", java.time.LocalDate.class);

    public final NumberPath<Integer> feePerMinute = createNumber("feePerMinute", Integer.class);

    public final StringPath gatheringLocation = createString("gatheringLocation");

    public final DateTimePath<java.time.LocalDateTime> gatheringTime = createDateTime("gatheringTime", java.time.LocalDateTime.class);

    public final EnumPath<GroupStatus> groupStatus = createEnum("groupStatus", GroupStatus.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> locationLatitude = createNumber("locationLatitude", Double.class);

    public final NumberPath<Double> locationLongitude = createNumber("locationLongitude", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final DatePath<java.time.LocalDate> startAt = createDate("startAt", java.time.LocalDate.class);

    public final StringPath summary = createString("summary");

    public QGroup(String variable) {
        super(Group.class, forVariable(variable));
    }

    public QGroup(Path<? extends Group> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGroup(PathMetadata metadata) {
        super(Group.class, metadata);
    }

}

