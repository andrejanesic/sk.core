package actions;

import exceptions.ActionUndoImpossibleException;
import loader.Loader;

/**
 * Radnja inicijalizacije skladišta.
 */
public class ActionInitStorage implements Action {

    /**
     * Broj puta koji je akcija izvršena. #OGRANIČENJE dozvoljeno samo 1.
     */
    private static int count = 0;

    /**
     * Putanja do skladišta u OS okruženju.
     */
    private String path;

    /**
     * Podrazumevani konstruktor.
     *
     * @param path Putanja do skladišta u OS okruženju.
     */
    public ActionInitStorage(String path) {
        this.path = path;
    }

    @Override
    public Boolean run() {
        if (count != 0)
            return false;

        count++;
        Loader.getInstance().initStorage(path);
        return true;
    }

    @Override
    public Boolean undo() {
        throw new ActionUndoImpossibleException("storage initialization");
    }
}
