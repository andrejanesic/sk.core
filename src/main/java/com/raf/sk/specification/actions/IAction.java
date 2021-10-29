package com.raf.sk.specification.actions;

import com.raf.sk.specification.exceptions.IActionUndoImpossibleException;

/**
 * Korisnička radnja.
 * <p>
 * Nalik na migraciju, ima dva metoda: run() i undo(), koji kontrolišu stanje aplikacije.
 * <p>
 * Sva komunikacija korisnika sa aplikacijom se odvija preko Akcija, odnosno preko {@link ActionManager.Factory} koja
 * pravi Akcije i {@link ActionManager} koji ih izvršava.
 */
interface IAction {

    /**
     * Izvršava radnju. Pomera red u ActionManager-u za +1.
     *
     * @return Vraća objekat specifičan za zadatu radnju. Objekat se dalje može kastovati kako bi mu se pristupilo. Taj
     * objekat obično predstavlja rezultat radnje.
     */
    Object run();

    /**
     * Opoziva radnju. Vraća aplikaciju u prethodno stanje, tj. stanje pre nego što je izvršena radnja, ukoliko je to
     * moguće.
     *
     * @return Vraća objekat specifičan za zadatu radnju. Objekat se dalje može kastovati kako bi mu se pristupilo. Taj
     * objekat obično predstavlja rezultat radnje.
     * @throws IActionUndoImpossibleException Greška ukoliko nije moguće izvršiti undo radnju.
     */
    Object undo();
}
