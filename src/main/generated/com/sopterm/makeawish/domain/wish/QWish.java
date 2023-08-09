package com.sopterm.makeawish.domain.wish;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWish is a Querydsl query type for Wish
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWish extends EntityPathBase<Wish> {

    private static final long serialVersionUID = 1729024851L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWish wish = new QWish("wish");

    public final com.sopterm.makeawish.domain.QBaseEntity _super = new com.sopterm.makeawish.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> endAt = createDateTime("endAt", java.time.LocalDateTime.class);

    public final StringPath hint = createString("hint");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath initial = createString("initial");

    public final StringPath presentImageUrl = createString("presentImageUrl");

    public final NumberPath<Integer> presentPrice = createNumber("presentPrice", Integer.class);

    public final ListPath<com.sopterm.makeawish.domain.Present, com.sopterm.makeawish.domain.QPresent> presents = this.<com.sopterm.makeawish.domain.Present, com.sopterm.makeawish.domain.QPresent>createList("presents", com.sopterm.makeawish.domain.Present.class, com.sopterm.makeawish.domain.QPresent.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> startAt = createDateTime("startAt", java.time.LocalDateTime.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.sopterm.makeawish.domain.user.QUser wisher;

    public QWish(String variable) {
        this(Wish.class, forVariable(variable), INITS);
    }

    public QWish(Path<? extends Wish> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWish(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWish(PathMetadata metadata, PathInits inits) {
        this(Wish.class, metadata, inits);
    }

    public QWish(Class<? extends Wish> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.wisher = inits.isInitialized("wisher") ? new com.sopterm.makeawish.domain.user.QUser(forProperty("wisher"), inits.get("wisher")) : null;
    }

}

