package com.raf.sk.core.repository.limitations;

import com.raf.sk.core.exceptions.INodeLimitationException;
import com.raf.sk.core.repository.Directory;
import com.raf.sk.core.repository.File;
import com.raf.sk.core.repository.INode;
import com.raf.sk.core.repository.INodeOperation;
import org.jetbrains.annotations.NotNull;

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
        if (args[0].getClass().equals(Double.class)) {
            maxSize = Math.round(Double.parseDouble(String.valueOf(args[0])));
        } else if (args[0].getClass().equals(Float.class)) {
            maxSize = Math.round(Float.parseFloat(String.valueOf(args[0])));
        } else {
            maxSize = (long) args[0];
        }
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
            // #TODO ne bi trebalo da getHost ikada bude null
            if (getHost() == null) {
                return true;
            }
            if (getHost().getParent() != null)
                allowedInParent = getHost().getParent().checkLimitations(t, args);
            return allowedInParent;
        }

        // ako ima ograničenja
        long targetSize;
        // #TODO ne bi trebalo da getHost ikada bude null
        if (getHost() == null) {
            return true;
        }
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
