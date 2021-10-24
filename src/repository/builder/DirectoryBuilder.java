package repository.builder;

import repository.Directory;

import java.util.Collection;
import java.util.HashSet;

/**
 * DirectoryBuilder se koristi prilikom inicijalizacije skladišta. DirectoryBuilder predstavlja jedan direktorijum,
 * a od njega se kreira instanca Directory, koja dalje upravlja fajlom. DirectoryBuilder može imati dece i može biti
 * ugnježden u drugi DirectoryBuilder. Svi DirectoryBuilder moraju imati roditelja osim korenskog, kome je roditelj
 * null.
 */
public class DirectoryBuilder implements INodeBuilder {

    /**
     * Naziv direktorijuma.
     */
    private String name;

    /**
     * Roditeljski direktorijum.
     */
    private DirectoryBuilder parent;

    /**
     * Tip čvora.
     */
    private INodeBuilderType type;

    /**
     * Poddirektorijumi.
     */
    private Collection<INodeBuilder> children;

    /**
     * Podrazumevani konstruktor, prihvata roditeljski DirectoryBuilder. Postavlja roditelja. Potrebno je dodati u
     * roditelja pozivom na addChild().
     *
     * @param parent Roditeljski DirectoryBuilder.
     * @param name   Naziv direktorijuma.
     */
    public DirectoryBuilder(DirectoryBuilder parent, String name) {
        this.parent = parent;
        this.name = name;
        this.type = INodeBuilderType.DIRECTORY;
        children = new HashSet<>();
    }

    /**
     * Konstruktor za korenski DirectoryBuilder.
     */
    public DirectoryBuilder() {
        this(null, Directory.ROOT_DIRECTORY);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DirectoryBuilder getParent() {
        return parent;
    }

    public void setParent(DirectoryBuilder parent) {
        this.parent = parent;
    }

    public Collection<INodeBuilder> getChildren() {
        return children;
    }

    /**
     * Dodaje INodeBuilder u direktorijum.
     *
     * @param iNodeBuilder INodeBuilder za dodavanje.
     */
    public void addChild(INodeBuilder iNodeBuilder) {
        children.add(iNodeBuilder);
    }

    /**
     * Briše INodeBuilder iz direktorijuma.
     *
     * @param iNodeBuilder INodeBuilder za brisanje.
     */
    public void removeChild(INodeBuilder iNodeBuilder) {
        children.remove(iNodeBuilder);
    }

    @Override
    public INodeBuilderType getType() {
        return type;
    }
}
