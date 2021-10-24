package actions;

/**
 * Korisnička radnja. Nalik na migraciju, ima dva metoda: run() i undo(), koji kontrolišu stanje aplikacije.
 */
interface Action {

    /**
     * Izvršava radnju. Pomera red u ActionManager-u za +1.
     */
    Object run();

    /**
     * Opoziva radnju. Vraća aplikaciju u prethodno stanje, tj. stanje pre nego što je izvršena radnja.
     */
    Object undo();
}
