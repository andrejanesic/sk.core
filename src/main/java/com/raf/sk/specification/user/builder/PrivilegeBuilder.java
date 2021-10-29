package com.raf.sk.specification.user.builder;

import java.util.Objects;

/**
 * Builder klasa za korisničke privilegije.
 */
public class PrivilegeBuilder {

    private Object referencedObject;

    private PrivilegeTypeBuilder privilegeType;

    /**
     * Konstruktor za privilegije koje referenciraju određeni objekat.
     *
     * @param referencedObject Objekat koji klasa referencira. Preporučuje se da objekat ima equals() metod
     *                         implementiran tako da važi jednakost među instancama pre i nakon serijalizacije.
     * @param privilegeType    Tip privilegije. {@link PrivilegeTypeBuilder}
     */
    public PrivilegeBuilder(Object referencedObject, PrivilegeTypeBuilder privilegeType) {
        this.referencedObject = referencedObject;
        this.privilegeType = privilegeType;
    }

    public PrivilegeBuilder(PrivilegeTypeBuilder privilegeType) {
        this.privilegeType = privilegeType;
    }

    public Object getReferencedObject() {
        return referencedObject;
    }

    public void setReferencedObject(Object referencedObject) {
        this.referencedObject = referencedObject;
    }

    public PrivilegeTypeBuilder getPrivilegeType() {
        return privilegeType;
    }

    public void setPrivilegeType(PrivilegeTypeBuilder privilegeType) {
        this.privilegeType = privilegeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrivilegeBuilder)) return false;
        PrivilegeBuilder that = (PrivilegeBuilder) o;
        return Objects.equals(getReferencedObject(), that.getReferencedObject()) &&
                getPrivilegeType() == that.getPrivilegeType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReferencedObject(), getPrivilegeType());
    }
}
