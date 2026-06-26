import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Salle salle;
    private String nomClient;
    private CreneauHoraire creneau;
    private LocalDateTime dateCreation;

    public Reservation(String id, Salle salle, String nomClient, CreneauHoraire creneau) {
        this.id = id;
        this.salle = salle;
        this.nomClient = nomClient;
        this.creneau = creneau;
        this.dateCreation = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public Salle getSalle() {
        return salle;
    }

    public String getNomClient() {
        return nomClient;
    }

    public CreneauHoraire getCreneau() {
        return creneau;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "[" + id + "] " + salle.getNom() + " | " + nomClient + " | " + creneau;
    }
}
