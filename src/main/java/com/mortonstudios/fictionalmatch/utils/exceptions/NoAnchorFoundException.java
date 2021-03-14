package com.mortonstudios.fictionalmatch.utils.exceptions;

public class NoAnchorFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoAnchorFoundException(final String id) {
        super("Could not find anchor " + id);
    }
}