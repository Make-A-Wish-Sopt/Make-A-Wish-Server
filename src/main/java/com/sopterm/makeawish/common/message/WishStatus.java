package com.sopterm.makeawish.common.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WishStatus {
    IS_BEFORE_FUNDING(1),
    IS_WHILE_FUNDING(2),
    IS_END_OF_FUNDING(3);

    private final int status;
}
