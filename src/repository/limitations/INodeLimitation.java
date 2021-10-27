package repository.limitations;

import exceptions.INodeLimitationException;
import org.jetbrains.annotations.NotNull;
import repository.INode;
import repository.INodeOperation;

/**
 * Predstavlja ograničenje koje je korisnik postavio nad nekim {@link repository.File} ili {@link repository.Directory}
 * čvorom.
 * <p>
 * Na primer, direktorijum ne može imati preko 10 fajlova, ne prihvata fajlove određenog tipa, ne sme biti preko
 * određene veličine, itd.
 * <p>
 * Implementira se tako što se pre svake akcije na {@link repository.INode} proveravaju limitacije i ukoliko one
 * omogućavaju, akcija se izvršava.
 */
public abstract class INodeLimitation {

    /**
     * {@link INode} nad kojim je ograničenje implementirano.
     */
    private INode host;

    /**
     * Podrazumevani konstruktor.
     *
     * @param host {@link INode} nad kojim je ograničenje implementirano.
     */
    public INodeLimitation(INode host) {
        this.host = host;
    }

    /**
     * Umesto proveravanja putem switch-a, svaka implementacija ima ovu metodu, i reagovaće samo na svoj specifičan tip
     * ograničenja.
     *
     * @param t Tip operacije {@link INodeOperation} koju treba izvršiti.
     * @return True ukoliko ograničenje omogućava nastavak, false ukoliko ne.
     * @throws INodeLimitationException Ukoliko je dosegnuto neko ograničenje.
     */
    public boolean run(INodeOperation t) throws INodeLimitationException {
        return run(t, (Object) null);
    }

    /**
     * Umesto proveravanja putem switch-a, svaka implementacija ima ovu metodu, i reagovaće samo na svoj specifičan tip
     * ograničenja.
     *
     * @param t    Tip operacije {@link INodeOperation} koju treba izvršiti.
     * @param args Objekti nad kojima se proverava.
     * @return True ukoliko ograničenje omogućava nastavak, false ukoliko ne.
     * @throws INodeLimitationException Ukoliko je dosegnuto neko ograničenje.
     */
    public abstract boolean run(INodeOperation t, Object... args) throws INodeLimitationException;

    /**
     * Vraća {@link INode} nad kojim je ograničenje implementirano.
     *
     * @return {@link INode} nad kojim je ograničenje implementirano.
     */
    @NotNull
    public INode getHost() {
        return host;
    }

    /**
     * Vraća tip ograničenja {@link INodeLimitationType}.
     *
     * @return Vraća tip ograničenja {@link INodeLimitationType}.
     */
    @NotNull
    public abstract INodeLimitationType getType();
}
