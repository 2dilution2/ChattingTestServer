package com.chat.chattingtest2.domain.crew.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCrewMember is a Querydsl query type for CrewMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCrewMember extends EntityPathBase<CrewMember> {

    private static final long serialVersionUID = 1649056673L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCrewMember crewMember = new QCrewMember("crewMember");

    public final QCrewMemberId id;

    public final BooleanPath isOwner = createBoolean("isOwner");

    public QCrewMember(String variable) {
        this(CrewMember.class, forVariable(variable), INITS);
    }

    public QCrewMember(Path<? extends CrewMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCrewMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCrewMember(PathMetadata metadata, PathInits inits) {
        this(CrewMember.class, metadata, inits);
    }

    public QCrewMember(Class<? extends CrewMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QCrewMemberId(forProperty("id")) : null;
    }

}

