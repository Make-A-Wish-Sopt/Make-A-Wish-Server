package com.sopterm.makeawish.exception;

public class WrongAccessTokenException extends BusinessLogicException {

    public WrongAccessTokenException(String message) { super("[WrongAccessTokenException] : " + message); }
}
