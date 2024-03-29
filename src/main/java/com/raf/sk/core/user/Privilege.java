package com.raf.sk.core.user;

import com.raf.sk.core.user.builder.PrivilegeBuilder;
import com.raf.sk.core.user.builder.PrivilegeTypeBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * JavaBean klasa korisničke privilegije. Predstavlja "jednu dozvolu" korisniku da radi nešto na nekom objektu, ili
 * dozvolu da radi nešto generalno.
 */
public class Privilege implements IPrivilege {

    /**
     * Objekat vezan za odobrenje.
     */
    private Object referencedObject;

    /**
     * Tip odobrenja.
     */
    private PrivilegeType type;

    /**
     * Konstruktor za generalna odobrenja.
     *
     * @param type Tip odobrenja.
     */
    public Privilege(PrivilegeType type) {
        this.type = type;
    }

    /**
     * Konstruktor za odobrenja specifična objektu.
     *
     * @param referencedObject Objekat vezan za odobrenje.
     * @param type             Tip odobrenja.
     */
    public Privilege(Object referencedObject, PrivilegeType type) {
        this.referencedObject = referencedObject;
        this.type = type;
    }

    /**
     * Konstruktor na osnovu bilder klase PrivilegeBuilder.
     *
     * @param privilegeBuilder PrivilegeBuilder instanca.
     * @throws EnumConstantNotPresentException Ukoliko PrivilegeBuilder sadrži PrivilegeBuilderType koji nije definisan
     *                                         u PrivilegeType.
     */
    public Privilege(PrivilegeBuilder privilegeBuilder) throws EnumConstantNotPresentException {
        referencedObject = privilegeBuilder.getReferencedObject();
        type = PrivilegeType.valueOf(privilegeBuilder.getPrivilegeType().toString());
    }

    @Override
    public Object getReferencedObject() {
        return referencedObject;
    }

    @NotNull
    @Override
    public PrivilegeType getType() {
        return type;
    }

    @NotNull
    @Override
    public PrivilegeBuilder toBuilder() {
        return new PrivilegeBuilder(referencedObject, PrivilegeTypeBuilder.valueOf(type.toString()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Privilege)) return false;
        IPrivilege that = (IPrivilege) o;
        return Objects.equals(getReferencedObject(), that.getReferencedObject()) &&
                getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReferencedObject(), getType());
    }
}
