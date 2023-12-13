package com.sopterm.makeawish.common;

import lombok.Getter;

@Getter
public class AbuseException extends RuntimeException {
    public AbuseException(String message) {
        super(message);
    }
}
