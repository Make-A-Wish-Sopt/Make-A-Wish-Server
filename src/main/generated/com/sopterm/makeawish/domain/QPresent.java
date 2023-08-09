package com.sopterm.makeawish.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPresent is a Querydsl query type for Present
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPresent extends EntityPathBase<Present> {

    private static final long serialVersionUID = 1558252416L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPresent present = new QPresent("present");

    public final QCake cake;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath message = createString("message");

    public final StringPath name = createString("name");

    public final com.sopterm.makeawish.domain.wish.QWish wish;

    public QPresent(String variable) {
        this(Present.class, forVariable(variable), INITS);
    }

    public QPresent(Path<? extends Present> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPresent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPresent(PathMetadata metadata, PathInits inits) {
        this(Present.class, metadata, inits);
    }

    public QPresent(Class<? extends Present> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cake = inits.isInitialized("cake") ? new QCake(forProperty("cake")) : null;
        this.wish = inits.isInitialized("wish") ? new com.sopterm.makeawish.domain.wish.QWish(forProperty("wish"), inits.get("wish")) : null;
    }

}

