package com.raf.sk.core.exceptions;

/**
 * Dešava se kada {@link com.raf.sk.core.repository.INode} instanca pokuša da odradi neku akciju koju određena
 * {@link com.raf.sk.core.repository.limitations.INodeLimitation} ne dozvoljava.
 */
public class INodeLimitationException extends Exception {

    public INodeLimitationException(String s) {
        super("You cannot perform that operation due to the following limitation: " + s);
    }

    public INodeLimitationException() {
        super("You cannot perform that operation due to a limitation in the configuration.");
    }
}
