package exceptions;

/**
 * Ukoliko prosleđeni tip čvora za kreiranje u direktorijumu nije validan.
 */
public class DirectoryMakeNodeInvalidNodeType extends Exception {

    public DirectoryMakeNodeInvalidNodeType() {
        super("Node type not valid.");
    }
}
