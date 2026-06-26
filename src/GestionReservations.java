import java.util.HashSet;
import java.util.Set;

public class GestionReservations {
    private Set<Reservation> reservations;

    public GestionReservations() {
        this.reservations = new HashSet<>();
    }

    public synchronized boolean ajouterReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("La reservation ne peut pas etre null");
        }

        for (Reservation r : reservations) {
            if (r.getSalle().getId().equals(reservation.getSalle().getId())) {
                if (r.getCreneau().chevauche(reservation.getCreneau())) {
                    return false;
                }
            }
        }

        return reservations.add(reservation);
    }

    public synchronized boolean supprimerReservation(String id) {
        return reservations.removeIf(r -> r.getId().equals(id));
    }

    public synchronized Set<Reservation> getReservations() {
        return new HashSet<>(reservations);
    }

    public synchronized Set<Reservation> getReservationsParSalle(String salleId) {
        Set<Reservation> result = new HashSet<>();
        for (Reservation r : reservations) {
            if (r.getSalle().getId().equals(salleId)) {
                result.add(r);
            }
        }
        return result;
    }

    public synchronized int getNombreReservations() {
        return reservations.size();
    }
}
