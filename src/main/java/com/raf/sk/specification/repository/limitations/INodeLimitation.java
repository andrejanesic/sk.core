package com.raf.sk.specification.repository.limitations;

import com.raf.sk.specification.exceptions.INodeLimitationException;
import com.raf.sk.specification.repository.INode;
import com.raf.sk.specification.repository.INodeOperation;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Predstavlja ograničenje koje je korisnik postavio nad nekim {@link com.raf.sk.specification.repository.File} ili {@link com.raf.sk.specification.repository.Directory}
 * čvorom.
 * <p>
 * Na primer, direktorijum ne može imati preko 10 fajlova, ne prihvata fajlove određenog tipa, ne sme biti preko
 * određene veličine, itd.
 * <p>
 * Implementira se tako što se pre svake akcije na {@link com.raf.sk.specification.repository.INode} proveravaju limitacije i ukoliko one
 * omogućavaju, akcija se izvršava.
 */
public class INodeLimitation {

    /**
     * Putanja do {@link INode} koji instanca referencira.
     */
    private String path;

    /**
     * {@link INode} nad kojim je ograničenje implementirano.
     */
    private transient INode host;

    /**
     * Argumenti za ograničenje. Ovo je implementirano da bi klasa mogla da se generički serijalizuje.
     */
    private Object[] args;

    /**
     * Tip ograničenja {@link INodeLimitationType}.
     */
    private INodeLimitationType type;

    /**
     * Podrazumevani konstruktor.
     *
     * @param host {@link INode} nad kojim je ograničenje implementirano.
     * @param args Argumenti za ograničenje.
     */
    public INodeLimitation(INode host, @NotNull INodeLimitationType type, Object... args) {
        this.host = host;
        this.type = type;
        this.args = args;
    }

    /**
     * Podrazumevani konstruktor, <em>SAMO ZA TESTIRANJE!</em>
     *
     * @param path Putanja do {@link INode} nad kojim je ograničenje implementirano.
     * @param args Argumenti za ograničenje.
     */
    public INodeLimitation(String path, @NotNull INodeLimitationType type, Object... args) {
        this.path = path;
        this.type = type;
        this.args = args;
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
    public boolean run(INodeOperation t, Object... args) throws INodeLimitationException {
        INodeLimitation e;
        switch (type) {
            case MAX_SIZE:
                e = new INodeMaxSizeLimitation(host, this.args);
                break;
            case BLACKLIST_EXT:
                e = new INodeBlacklistExtLimitation(host, this.args);
                break;
            case MAX_FILE_COUNT:
                e = new INodeMaxFileCountLimitation(host, this.args);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return e.run(t, args);
    }

    public INodeLimitationType getType() {
        return type;
    }

    public void setType(INodeLimitationType type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public INode getHost() {
        return host;
    }

    public void setHost(INode host) {
        this.host = host;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof INodeLimitation)) return false;
        INodeLimitation that = (INodeLimitation) o;
        return Objects.equals(getPath(), that.getPath()) &&
                Objects.equals(getHost(), that.getHost()) &&
                Arrays.equals(args, that.args) &&
                getType() == that.getType();
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getPath(), getHost(), getType());
        result = 31 * result + Arrays.hashCode(args);
        return result;
    }

    @Override
    public String toString() {
        return "INodeLimitation{" +
                "path='" + path + '\'' +
                ", host=" + host +
                ", args=" + Arrays.toString(args) +
                ", type=" + type +
                '}';
    }
}
