package com.sopterm.makeawish.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AbuseException extends RuntimeException {
    public AbuseException(String message) {
        super(message);
    }
}
