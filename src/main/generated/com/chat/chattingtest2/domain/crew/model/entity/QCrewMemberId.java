package com.chat.chattingtest2.domain.crew.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCrewMemberId is a Querydsl query type for CrewMemberId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCrewMemberId extends BeanPath<CrewMemberId> {

    private static final long serialVersionUID = -99467108L;

    public static final QCrewMemberId crewMemberId = new QCrewMemberId("crewMemberId");

    public final NumberPath<Long> crewId = createNumber("crewId", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public QCrewMemberId(String variable) {
        super(CrewMemberId.class, forVariable(variable));
    }

    public QCrewMemberId(Path<? extends CrewMemberId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCrewMemberId(PathMetadata metadata) {
        super(CrewMemberId.class, metadata);
    }

}

