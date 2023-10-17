package com.chat.chattingtest2.domain.crew.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCrewMessage is a Querydsl query type for CrewMessage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCrewMessage extends EntityPathBase<CrewMessage> {

    private static final long serialVersionUID = -412807200L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCrewMessage crewMessage = new QCrewMessage("crewMessage");

    public final com.chat.chattingtest2.global.entity.QBaseTime _super = new com.chat.chattingtest2.global.entity.QBaseTime(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QCrewMember crewMember;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCrewMessage(String variable) {
        this(CrewMessage.class, forVariable(variable), INITS);
    }

    public QCrewMessage(Path<? extends CrewMessage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCrewMessage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCrewMessage(PathMetadata metadata, PathInits inits) {
        this(CrewMessage.class, metadata, inits);
    }

    public QCrewMessage(Class<? extends CrewMessage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.crewMember = inits.isInitialized("crewMember") ? new QCrewMember(forProperty("crewMember"), inits.get("crewMember")) : null;
    }

}

