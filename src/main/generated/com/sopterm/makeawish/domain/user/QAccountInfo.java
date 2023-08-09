package com.sopterm.makeawish.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccountInfo is a Querydsl query type for AccountInfo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QAccountInfo extends BeanPath<AccountInfo> {

    private static final long serialVersionUID = 646966643L;

    public static final QAccountInfo accountInfo = new QAccountInfo("accountInfo");

    public final StringPath account = createString("account");

    public final StringPath bank = createString("bank");

    public final StringPath name = createString("name");

    public QAccountInfo(String variable) {
        super(AccountInfo.class, forVariable(variable));
    }

    public QAccountInfo(Path<? extends AccountInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccountInfo(PathMetadata metadata) {
        super(AccountInfo.class, metadata);
    }

}

