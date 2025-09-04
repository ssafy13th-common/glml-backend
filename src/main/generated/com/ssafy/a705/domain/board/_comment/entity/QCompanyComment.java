package com.ssafy.a705.domain.board._comment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCompanyComment is a Querydsl query type for CompanyComment
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompanyComment extends EntityPathBase<CompanyComment> {

    private static final long serialVersionUID = -1783052411L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCompanyComment companyComment = new QCompanyComment("companyComment");

    public final com.ssafy.a705.global.common.QBaseEntity _super = new com.ssafy.a705.global.common.QBaseEntity(this);

    public final com.ssafy.a705.domain.board.entity.QCompanyBoard companyBoard;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.ssafy.a705.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final QCompanyComment parent;

    public QCompanyComment(String variable) {
        this(CompanyComment.class, forVariable(variable), INITS);
    }

    public QCompanyComment(Path<? extends CompanyComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCompanyComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCompanyComment(PathMetadata metadata, PathInits inits) {
        this(CompanyComment.class, metadata, inits);
    }

    public QCompanyComment(Class<? extends CompanyComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.companyBoard = inits.isInitialized("companyBoard") ? new com.ssafy.a705.domain.board.entity.QCompanyBoard(forProperty("companyBoard"), inits.get("companyBoard")) : null;
        this.member = inits.isInitialized("member") ? new com.ssafy.a705.domain.member.entity.QMember(forProperty("member")) : null;
        this.parent = inits.isInitialized("parent") ? new QCompanyComment(forProperty("parent"), inits.get("parent")) : null;
    }

}

