package loader;

/**
 * Učitava skladište i njegove fajlove, konfiguraciju i korisnike.
 */
public class Loader {

    /**
     * Podrazumevani konstruktor.
     */
    private Loader() {
    }

    /**
     * Vraća instancu Loader-a.
     *
     * @return Loader instanca.
     */
    public static Loader getInstance() {
        return Holder.INSTANCE;
    }


    /**
     * Holder za thread-safe singleton instancu.
     */
    private static class Holder {
        private static final Loader INSTANCE = new Loader();
    }
}
