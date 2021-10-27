package repository.limitations;

import exceptions.INodeLimitationException;
import org.jetbrains.annotations.NotNull;
import repository.Directory;
import repository.File;
import repository.INode;
import repository.INodeOperation;

/**
 * Implementacija {@link INodeLimitation} za maksimalnu veličinu.
 */
public class INodeMaxSizeLimitation extends INodeLimitation {

    public static final int MAX_SIZE_NOLIMIT = -1;

    /**
     * Maksimalni broj fajlova.
     */
    private long maxSize;

    /**
     * Podrazumevani konstruktor.
     *
     * @param host {@link INode} nad kojim je ograničenje implementirano.
     * @param args Maksimalna veličina poddirektorijuma, u [B].
     */
    public INodeMaxSizeLimitation(INode host, Object... args) {
        super(host, INodeLimitationType.BLACKLIST_EXT, args);
        this.maxSize = (long) args[0];
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
        if (maxSize == MAX_SIZE_NOLIMIT) {
            boolean allowedInParent = true;
            if (getHost().getParent() != null)
                allowedInParent = getHost().getParent().checkLimitations(t, args);
            return allowedInParent;
        }

        // ako ima ograničenja
        long targetSize;
        long selfSize = ((Directory) getHost()).getSize();
        if (target instanceof Directory) {
            targetSize = ((Directory) target).getSize();
        } else {
            targetSize = ((File) target).getSize();
        }

        if (selfSize + targetSize > maxSize)
            throw new INodeLimitationException();
        return true;
    }

    @NotNull
    @Override
    public INodeLimitationType getType() {
        return INodeLimitationType.MAX_SIZE;
    }
}
