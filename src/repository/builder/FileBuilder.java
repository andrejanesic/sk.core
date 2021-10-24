package repository.builder;

/**
 * FileBuilder se koristi prilikom inicijalizacije skladišta. FileBuilder predstavlja jedan fajl u direktorijumu, a
 * od njega se kreira instanca File, koja dalje upravlja fajlom. FileBuilder ne može imati dece.
 */
public class FileBuilder implements INodeBuilder {

    /**
     * Naziv fajla.
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
     * Podrazumevani konstruktor. Postavlja roditelja. Potrebno je dodati instancu u roditelja preko addChild() metode.
     *
     * @param parent Roditeljski DirectoryBuilder.
     * @param name   Naziv fajla.
     */
    public FileBuilder(DirectoryBuilder parent, String name) {
        this.parent = parent;
        this.name = name;
        this.type = INodeBuilderType.FILE;
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

    @Override
    public INodeBuilderType getType() {
        return type;
    }
}
