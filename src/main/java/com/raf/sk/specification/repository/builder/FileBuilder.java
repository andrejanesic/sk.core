package com.raf.sk.specification.repository.builder;

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
     * Veličina čvora.
     */
    private long size;

    /**
     * Podrazumevani konstruktor. Postavlja roditelja. Potrebno je dodati instancu u roditelja preko addChild() metode.
     *
     * @param parent Roditeljski DirectoryBuilder.
     * @param name   Naziv fajla.
     * @param size   Veličina fajla.
     */
    public FileBuilder(DirectoryBuilder parent, String name, long size) {
        this.parent = parent;
        this.name = name;
        this.type = INodeBuilderType.FILE;
        this.size = size;
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public INodeBuilderType getType() {
        return type;
    }

    public void setType(INodeBuilderType type) {
        this.type = type;
    }
}
