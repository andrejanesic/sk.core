package repository.limitations;

import exceptions.INodeLimitationException;
import repository.*;

/**
 * Implementacija limitacije za tip fajl ekstenzije.
 */
public class INodeBlacklistExtLimitation extends INodeLimitation {

    /**
     * Nedozvoljena fajl ekstenzija.
     */
    private String extension;

    /**
     * Podrazumevani konstruktor.
     *
     * @param host {@link INode} nad kojim je ograničenje implementirano.
     * @param args {@link String} zabranjena ekstenzija.
     */
    public INodeBlacklistExtLimitation(INode host, Object... args) {
        super(host, INodeLimitationType.BLACKLIST_EXT, args);
        this.extension = (String) args[0];
    }

    @Override
    public boolean run(INodeOperation t, Object... args) throws INodeLimitationException, ClassCastException {
        if (getHost() instanceof File)
            return true;

        if (!t.equals(INodeOperation.ADD_CHILD_TO_SELF))
            return true;

        if (args.length == 0) return true;
        if (args[0] == null) return true;

        String ext;
        if (args[0] instanceof String && args[1] != null && args[1] instanceof INodeType) {
            String i = (String) args[0];
            INodeType j = (INodeType) args[1];
            if (j.equals(INodeType.DIRECTORY))
                return true;

            if (i.lastIndexOf('.') == -1) return true;
            ext = i.substring(i.lastIndexOf('.') + 1);
        } else {
            // fetchuj argument i kastuj
            // #TODO ovde može doći do greške tipa ClassCastException
            INode target = (INode) args[0];
            if (target instanceof Directory)
                return true;

            // ako fajl nema ekstenziju, propusti
            // #TODO ovde može doći do greške tipa ClassCastException
            ext = ((File) target).getExtension();
            if (ext == null) return true;
        }

        if (extension.equals(ext))
            throw new INodeLimitationException();
        return true;
    }
}
