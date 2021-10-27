package repository;

import config.IConfig;
import core.Core;
import exceptions.IComponentNotInitializedException;
import exceptions.INodeFatalException;
import exceptions.INodeLimitationException;
import exceptions.INodeRootNotInitializedException;
import repository.limitations.INodeLimitation;
import repository.limitations.INodeLimitationType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * INode predstavlja jedan čvor u skladištu, koji može biti fajl ili direktorijum.
 */
public abstract class INode {

    public static final String INODE_ROOT = "/";

    /**
     * Roditeljski čvor.
     */
    private INode parent;

    /**
     * Naziv čvora.
     */
    private String name;

    /**
     * Tip čvora.
     */
    private INodeType type;

    /**
     * Predstavlja ograničenja dodata na INode.
     */
    private Collection<INodeLimitation> limitations;

    /**
     * Podrazumevani konstruktor.
     *
     * @param parent Roditeljski čvor.
     * @param name   Naziv čvora.
     */
    public INode(INode parent, String name, INodeType type) {
        this.parent = parent;
        this.name = name;
        this.type = type;
        this.limitations = new ArrayList<>();

        if (this == parent)
            throw new INodeFatalException("Fatal error: INode cannot be its own parent.");
    }

    /**
     * Vraća tip čvora.
     *
     * @return Tip čvora.
     */
    public INodeType getType() {
        return type;
    }

    /**
     * Vraća roditeljski čvor.
     *
     * @return Roditeljski čvor.
     */
    public INode getParent() {
        return parent;
    }

    /**
     * Omogućava promenu roditeljskog čvora. Dozvoljeno samo na nivou paketa radi premeštanja čvorova.
     *
     * @param iNode Novi roditeljski čvor.
     */
    protected void setParent(INode iNode) {
        parent = iNode;

        if (this == parent)
            throw new RuntimeException("Fatal error: INode cannot be its own parent.");
    }

    /**
     * Vraća tačno ukoliko je dati iNode direktni roditelj instance čvora ili se nalazi u lancu roditelja.
     *
     * @param iNode Potencijalni roditelj koga treba proveriti.
     * @return Tačno ukoliko je iNode (direktni ili indirektni) roditelj čvora, netačno ukoliko nije.
     */
    public boolean isGrandchild(INode iNode) {
        if (parent == iNode) return true;
        if (parent == null) return false;
        return parent.isGrandchild(iNode);
    }

    /**
     * Vraća putanju čvora.
     *
     * @return Putanja čvora.
     */
    public String getPath() {
        // ako je korenski čvor, vratiće samo ime
        if (parent == null) return INODE_ROOT;
        return parent.getPath() + (parent.getParent() == null ? "" : "/") + name;
    }

    /**
     * Vraća naziv čvora, bez putanje.
     *
     * @return Naziv čvora.
     */
    public String getName() {
        return name;
    }

    /**
     * Briše metu iz IO i svoje podčvorove, ukoliko postoje.
     *
     * @param path Putanja do čvora za brisanje.
     * @throws INodeRootNotInitializedException Ukoliko korenski čvor nije inicijalizovan.
     * @throws INodeLimitationException         Ukoliko je određena limitacija dosegnuta.
     */
    public abstract void delete(String path) throws INodeRootNotInitializedException, INodeLimitationException;

    /**
     * Briše sebe iz IO i svoje podčvorove, ukoliko postoje.
     *
     * @throws INodeLimitationException Ukoliko je određena limitacija dosegnuta.
     */
    public abstract void delete() throws INodeLimitationException;

    /**
     * Pomeranje čvora u novi čvor.
     *
     * @param dest Destinacioni čvor.
     * @throws INodeRootNotInitializedException Ukoliko korenski čvor nije inicijalizovan.
     * @throws INodeLimitationException         Ukoliko je određena limitacija dosegnuta.
     */
    public void move(INode dest) throws INodeRootNotInitializedException, INodeLimitationException {
        move(dest.getPath());
    }

    /**
     * Pomeranje čvora u novi čvor.
     *
     * @param path Putanja destinacionog čvora.
     * @throws INodeRootNotInitializedException Ukoliko korenski čvor nije inicijalizovan.
     * @throws INodeLimitationException         Ukoliko je određena limitacija dosegnuta.
     */
    public abstract void move(String path) throws INodeRootNotInitializedException, INodeLimitationException;

    /**
     * Dodavanje, odnosno odpremanje fajla/direktorijuma iz eksternog sistema na skladište.
     *
     * @param path Putanja na OS-u gde se nalazi čvor za otpremanje.
     */
    public abstract INode upload(String path) throws INodeLimitationException;

    /**
     * Preuzimanje fajlova.
     *
     * @param path Putanja na OS-u gde treba sačuvati čvor.
     */
    public abstract void download(String path) throws INodeLimitationException;

    /**
     * Dodaje novi {@link INodeLimitation}.
     *
     * @param limitation {@link INodeLimitation} za dodati.
     */
    public void addLimitation(INodeLimitation limitation) {
        limitations.add(limitation);
        if (Core.getInstance().ConfigManager().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);
        //noinspection ConstantConditions
        Core.getInstance().ConfigManager().getConfig().addLimitation(limitation);
    }

    /**
     * Vraća {@link INodeLimitation} tipa {@link INodeLimitationType} t ukoliko postoji, ili null ukoliko ne postoji.
     *
     * @param t Tip {@link INodeLimitationType}.
     * @return {@link INodeLimitation} tipa {@link INodeLimitationType} t ukoliko postoji, ili null ukoliko ne postoji.
     */
    public INodeLimitation getLimitation(INodeLimitationType t) {
        for (INodeLimitation l : limitations)
            if (l.getType().equals(t))
                return l;
        return null;
    }

    /**
     * Vraća sve limitacije {@link INodeLimitation} dodate na instancu koje odgovaraju zadatom tipu.
     *
     * @return Sve limitacije {@link INodeLimitation} dodate na instancu.
     */
    public Collection<INodeLimitation> getLimitations() {
        return limitations;
    }

    /**
     * Briše {@link INodeLimitation} sa instance.
     *
     * @param limitation {@link INodeLimitation} za obrisati.
     */
    public void deleteLimitation(INodeLimitation limitation) {
        limitations.remove(limitation);
        if (Core.getInstance().ConfigManager().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);
        //noinspection ConstantConditions
        Core.getInstance().ConfigManager().getConfig().deleteLimitation(limitation);
    }

    /**
     * Briše sve {@link INodeLimitation} zadatog tipa sa instance.
     *
     * @param type {@link INodeLimitationType} za obrisati.
     */
    public void deleteLimitation(INodeLimitationType type) {
        Iterator<INodeLimitation> it = limitations.iterator();
        //noinspection WhileLoopReplaceableByForEach
        while (it.hasNext()) {
            INodeLimitation t = it.next();
            if (t.getType().equals(type)) {
                deleteLimitation(t);
            }
        }
    }

    /**
     * Proverava sve {@link INodeLimitation} u sebi, ali i u roditeljima, i izbacuje grešku ukoliko postoji limitacija
     * koja onemogućava pokušanu radnju.
     *
     * @param t Tip operacije {@link INodeOperation} koju treba izvršiti.
     * @return True ukoliko limitacije <em>dozvoljavaju izvršenje</em> operacije, false u protivnom.
     * @throws INodeLimitationException Ukoliko je određena limitacija dosegnuta.
     */
    public boolean checkLimitations(INodeOperation t) throws INodeLimitationException {
        return checkLimitations(t, (Object) null);
    }

    /**
     * Proverava sve {@link INodeLimitation} u sebi, ali i u roditeljima, i izbacuje grešku ukoliko postoji limitacija
     * koja onemogućava pokušanu radnju.
     *
     * @param t    Tip operacije {@link INodeOperation} koju treba izvršiti.
     * @param args Objekti zbog kojih se provera limitacija.
     * @return True ukoliko limitacije <em>dozvoljavaju izvršenje</em> operacije, false u protivnom.
     * @throws INodeLimitationException Ukoliko je određena limitacija dosegnuta.
     */
    public boolean checkLimitations(INodeOperation t, Object... args) throws INodeLimitationException {
        for (INodeLimitation l : limitations)
            if (!l.run(t, args))
                return false;

        if (parent == null)
            return true;

        // proveri u svim parent elementima, rekurentno
        boolean allowedInParent = parent.checkLimitations(t, args);
        if (!allowedInParent)
            throw new INodeLimitationException();
        return true;
    }

}
