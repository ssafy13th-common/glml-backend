package com.ssafy.a705.domain.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCompanyBoard is a Querydsl query type for CompanyBoard
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompanyBoard extends EntityPathBase<CompanyBoard> {

    private static final long serialVersionUID = 946100046L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCompanyBoard companyBoard = new QCompanyBoard("companyBoard");

    public final com.ssafy.a705.global.common.QBaseEntity _super = new com.ssafy.a705.global.common.QBaseEntity(this);

    public final NumberPath<Integer> commentCount = createNumber("commentCount", Integer.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.ssafy.a705.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath title = createString("title");

    public QCompanyBoard(String variable) {
        this(CompanyBoard.class, forVariable(variable), INITS);
    }

    public QCompanyBoard(Path<? extends CompanyBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCompanyBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCompanyBoard(PathMetadata metadata, PathInits inits) {
        this(CompanyBoard.class, metadata, inits);
    }

    public QCompanyBoard(Class<? extends CompanyBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.ssafy.a705.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

