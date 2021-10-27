package repository.limitations;

import exceptions.INodeLimitationException;
import repository.Directory;
import repository.File;
import repository.INode;
import repository.INodeOperation;

/**
 * Implementacija {@link INodeLimitation} za
 */
public class INodeMaxFileCountLimitation extends INodeLimitation {

    public static final int MAX_FILE_COUNT_NOLIMIT = -1;

    /**
     * Maksimalni broj fajlova.
     */
    private long maxFileCount;

    /**
     * Podrazumevani konstruktor.
     *
     * @param host {@link INode} nad kojim je ograničenje implementirano.
     * @param args Maksimalni broj fajlova.
     */
    public INodeMaxFileCountLimitation(INode host, Object... args) {
        super(host, INodeLimitationType.BLACKLIST_EXT, args);
        this.maxFileCount = (long) args[0];
    }

    @Override
    public boolean run(INodeOperation t, Object... args) throws INodeLimitationException {
        if (getHost() instanceof File)
            return true;

        if (!t.equals(INodeOperation.ADD_CHILD_TO_SELF))
            return true;

        if (args.length == 0) return true;
        if (args[0] == null) return true;

        // fetchuj argument i kastuj
        // #TODO ovde može doći do greške tipa ClassCastException
        INode target = (INode) args[0];

        // ako nema ograničenja
        if (maxFileCount == MAX_FILE_COUNT_NOLIMIT) {
            boolean allowedInParent = true;
            if (getHost().getParent() != null)
                allowedInParent = getHost().getParent().checkLimitations(t, args);
            return allowedInParent;
        }

        // ako ima ograničenja
        int targetCount;
        int selfCount = ((Directory) getHost()).getFileCount();
        if (target instanceof Directory) {
            targetCount = ((Directory) target).getFileCount();
        } else {
            targetCount = 1;
        }

        if (selfCount + targetCount > maxFileCount)
            throw new INodeLimitationException();
        return true;
    }
}
