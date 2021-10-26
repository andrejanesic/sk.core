package exceptions;

/**
 * Dešava se ukoliko komponenta (ili element čiji je ona "wrapper") nisu učitani, a program pokušava da im pristupi.
 */
public class IComponentNotInitializedException extends RuntimeException {

    public IComponentNotInitializedException(Class<?> classType) {
        super("The component you are trying to access of class \"" + classType + "\" has not yet been initialized. " +
                "Please ensure that you have initialized the component or the interface you are trying to access.");
    }
}
