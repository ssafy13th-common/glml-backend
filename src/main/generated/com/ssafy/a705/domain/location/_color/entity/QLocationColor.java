package com.ssafy.a705.domain.location._color.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLocationColor is a Querydsl query type for LocationColor
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLocationColor extends EntityPathBase<LocationColor> {

    private static final long serialVersionUID = 738959730L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLocationColor locationColor = new QLocationColor("locationColor");

    public final StringPath color = createString("color");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.ssafy.a705.domain.location.entity.QLocation location;

    public final com.ssafy.a705.domain.member.entity.QMember member;

    public QLocationColor(String variable) {
        this(LocationColor.class, forVariable(variable), INITS);
    }

    public QLocationColor(Path<? extends LocationColor> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLocationColor(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLocationColor(PathMetadata metadata, PathInits inits) {
        this(LocationColor.class, metadata, inits);
    }

    public QLocationColor(Class<? extends LocationColor> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.location = inits.isInitialized("location") ? new com.ssafy.a705.domain.location.entity.QLocation(forProperty("location")) : null;
        this.member = inits.isInitialized("member") ? new com.ssafy.a705.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

