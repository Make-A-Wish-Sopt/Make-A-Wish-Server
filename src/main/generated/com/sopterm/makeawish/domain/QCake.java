package com.sopterm.makeawish.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCake is a Querydsl query type for Cake
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCake extends EntityPathBase<Cake> {

    private static final long serialVersionUID = 1886545299L;

    public static final QCake cake = new QCake("cake");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public QCake(String variable) {
        super(Cake.class, forVariable(variable));
    }

    public QCake(Path<? extends Cake> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCake(PathMetadata metadata) {
        super(Cake.class, metadata);
    }

}

