package user;

/**
 * JavaBean klasa korisničke privilegije. Predstavlja "jednu dozvolu" korisniku da radi nešto na nekom objektu, ili
 * dozvolu da radi nešto generalno.
 */
public class Privilege {

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

    public Object getReferencedObject() {
        return referencedObject;
    }

    public PrivilegeType getType() {
        return type;
    }
}
