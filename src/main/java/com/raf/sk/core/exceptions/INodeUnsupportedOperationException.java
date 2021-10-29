package com.raf.sk.core.exceptions;

/**
 * Događa se u slučaju da određena operacija nije podržana.
 */
public class INodeUnsupportedOperationException extends UnsupportedOperationException {

    public INodeUnsupportedOperationException(String s) {
        super(s);
    }

    public INodeUnsupportedOperationException() {
        super();
    }
}
