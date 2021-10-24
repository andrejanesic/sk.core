package actions;

import exceptions.ActionUndoImpossibleException;

import java.util.ArrayList;
import java.util.List;

/**
 * Menadžer za korisničke radnje.
 */
public class ActionManager {

    /**
     * Red radnji.
     */
    private List<Action> queue;

    /**
     * "Seeker" liste. Pomera se +1 kada se radnja izvrši, -1 kada se radnja poništi (undo).
     */
    private int position = 0;

    private ActionManager() {
        queue = new ArrayList<>();
    }

    /**
     * Vraća instancu ActionManager-a.
     *
     * @return ActionManager instanca.
     */
    public static ActionManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Vraća radnje u redu u vidu List-a.
     *
     * @return Radnje u redu.
     */
    public List<Action> getQueue() {
        return queue;
    }

    /**
     * Dodaje radnju u red.
     *
     * @param a Nova radnja.
     */
    public void addAction(Action a) {
        queue.add(a);
    }

    /**
     * Izvršava sledeću radnju u redu.
     */
    public void run() {
        try {
            queue.get(position).run();
        } finally {
            position += 1;
        }
    }

    /**
     * Poništava (undo) poslednju odrađenu radnju.
     */
    public void undo() {
        position -= 1;
        try {
            queue.get(position).undo();
        } catch (ActionUndoImpossibleException ignored) {
        }
        // #TODO ovde undo treba da bude od prethodne akcije, a run od sledeće
        queue.remove(position);
    }

    /**
     * Holder za thread-safe singleton instancu.
     */
    private static class Holder {
        private static final ActionManager INSTANCE = new ActionManager();
    }
}
