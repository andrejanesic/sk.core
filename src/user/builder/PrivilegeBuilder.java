package user.builder;

/**
 * Builder klasa za korisničke privilegije.
 */
public class PrivilegeBuilder {

    private Object referencedObject;

    private PrivilegeTypeBuilder privilegeType;

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
}
