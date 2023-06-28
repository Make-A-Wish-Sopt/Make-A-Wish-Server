package com.sopterm.makeawish.exception;

public class WrongTokenException extends BusinessLogicException {
    public WrongTokenException(String message) { super("[WrongTokenException] : " + message); }
}
