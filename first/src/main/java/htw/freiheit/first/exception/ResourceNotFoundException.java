package htw.freiheit.first.exception;

/**
 * Eine angefragte Ressource (z.B. ein Nutzer oder eine Rolle) existiert nicht.
 */
public class ResourceNotFoundException extends RuntimeException {

    private final String resource;
    private final Long id;

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " nicht gefunden: " + id);
        this.resource = resource;
        this.id = id;
    }

    public String getResource() {
        return resource;
    }

    public Long getId() {
        return id;
    }
}