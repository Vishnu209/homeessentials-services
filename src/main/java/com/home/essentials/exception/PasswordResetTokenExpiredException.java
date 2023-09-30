package com.home.essentials.exception;

public class PasswordResetTokenExpiredException extends Exception {

    private static final long serialVersionUID = -5393503386009604589L;

    public PasswordResetTokenExpiredException(String message) {
        super(message);
    }
}
