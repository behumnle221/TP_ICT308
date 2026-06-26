import java.io.Serializable;
import java.time.LocalDateTime;

public class CreneauHoraire implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;

    public CreneauHoraire(LocalDateTime dateDebut, LocalDateTime dateFin) {
        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("Les dates ne peuvent pas être null");
        }
        if (dateDebut.isAfter(dateFin) || dateDebut.isEqual(dateFin)) {
            throw new IllegalArgumentException("La date de debut doit etre avant la date de fin");
        }
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public boolean chevauche(CreneauHoraire autre) {
        return this.dateDebut.isBefore(autre.dateFin) && autre.dateDebut.isBefore(this.dateFin);
    }

    public boolean estSurMemeJour(CreneauHoraire autre) {
        return this.dateDebut.toLocalDate().equals(autre.dateDebut.toLocalDate());
    }

    @Override
    public String toString() {
        return dateDebut.toLocalDate() + " " +
               dateDebut.toLocalTime() + " - " +
               dateFin.toLocalTime();
    }
}
