package com.sopterm.makeawish.common.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WishStatus {
    BEFORE(1),
    WHILE(2),
    END(3);

    private final int status;
}
