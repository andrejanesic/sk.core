package com.raf.sk.core.actions;

import com.raf.sk.core.exceptions.IActionUndoImpossibleException;

import java.util.ArrayList;
import java.util.List;

/**
 * Menadžer za korisničke radnje.
 */
public class ActionManager implements IActionManager {

    /**
     * Red radnji.
     */
    private List<IAction> queue;

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
    public static IActionManager getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public List<IAction> getQueue() {
        return queue;
    }

    @Override
    public void addAction(IAction a) {
        queue.add(a);
    }

    @Override
    public Object run() {
        Object r = null;
        try {
            r = queue.get(position).run();
        } finally {
            position += 1;
            return r;
        }
    }

    @Override
    public Object undo() {
        Object r;
        position -= 1;
        try {
            r = queue.get(position).undo();
        } catch (IActionUndoImpossibleException ignored) {
            r = null;
        }
        // #TODO ovde undo treba da bude od prethodne akcije, a run od sledeće
        queue.remove(position);
        return r;
    }

    /**
     * Holder za thread-safe singleton instancu.
     */
    private static class Holder {
        private static final IActionManager INSTANCE = new ActionManager();
    }

    /**
     * Factory za {@link IAction} korisničke akcije.
     */
    public static class Factory {

        /**
         * Kreira novu akciju {@link IAction}. Akcija se <em>NE DODAJE</em> u red automatski, to program mora uraditi
         * pozivom {@link ActionManager#addAction(IAction)}.
         *
         * @return Nova instanca {@link IAction}.
         */
        public static IAction makeAction() {
            return null;
        }
    }
}
