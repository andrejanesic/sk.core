package com.raf.sk.core.exceptions;

/**
 * Dešava se kada u {@link com.raf.sk.core.io.IOManager} nije registrovan ni jedan drajver, a neka komponenta pokuša da pristupi
 * {@link com.raf.sk.core.io.IODriver} metodi.
 */
public class IOManagerNoDriverException extends RuntimeException {

    public IOManagerNoDriverException() {
        super("No IODriver registered in IOManager. You must register an IODriver first.");
    }
}
