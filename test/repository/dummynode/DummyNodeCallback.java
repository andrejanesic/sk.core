package repository.dummynode;

/**
 * Koristi se kao callback za DummyNode funkcije.
 */
public interface DummyNodeCallback {

    /**
     * Callback funkcija.
     */
    default void execute() {
        execute(null);
    }

    /**
     * Callback funkcija.
     *
     * @param dummyNode Čvor koji se obrađuje.
     */
    void execute(DummyNode dummyNode);
}
