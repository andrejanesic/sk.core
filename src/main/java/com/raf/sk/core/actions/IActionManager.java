package com.raf.sk.core.actions;

import java.util.List;

/**
 * Komponenta za menadžment akcija.
 * <p>
 * Svaka korisnička interakcija vrši se preko {@link IAction} instanca. One predstavljaju male "kontrolere" koji
 * izvršavaju tok aplikacije u zavisnosti od korisničkog ulaza.
 */
public interface IActionManager {

    /**
     * Vraća radnje u redu u vidu List-a.
     *
     * @return Radnje u redu.
     */
    List<IAction> getQueue();

    /**
     * Dodaje radnju u red.
     *
     * @param a Nova radnja.
     */
    void addAction(IAction a);

    /**
     * Izvršava sledeću radnju u redu.
     */
    void run();

    /**
     * Poništava (undo) poslednju odrađenu radnju.
     */
    void undo();
}
