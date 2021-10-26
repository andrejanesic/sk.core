package exceptions;

/**
 * Dešava se kada u {@link io.IOManager} nije registrovan ni jedan drajver, a neka komponenta pokuša da pristupi
 * {@link io.IODriver} metodi.
 */
public class IOManagerNoDriverException extends RuntimeException {

    public IOManagerNoDriverException() {
        super("No IODriver registered in IOManager. You must register an IODriver first.");
    }
}
