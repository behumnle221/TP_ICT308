import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static GestionReservations gestion;
    private static Scanner scanner;

    public static void main(String[] args) {
        gestion = new GestionReservations();
        scanner = new Scanner(System.in);

        chargerDonnees();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nSauvegarde automatique...");
            FichierIndexe.sauvegarder(gestion.getReservations());
            System.out.println("Donnees sauvegardees.");
        }));

        boolean continuer = true;
        while (continuer) {
            afficherMenu();
            System.out.print("Choix: ");
            String choix = scanner.nextLine();

            switch (choix) {
                case "1" -> ajouterReservation();
                case "2" -> supprimerReservation();
                case "3" -> afficherReservations();
                case "4" -> afficherParSalle();
                case "5" -> continuer = false;
                default -> System.out.println("Choix invalide");
            }
        }

        FichierIndexe.sauvegarder(gestion.getReservations());
        scanner.close();
        System.out.println("Au revoir!");
    }

    private static void afficherMenu() {
        System.out.println("\n=== Systeme de Reservation ===");
        System.out.println("1. Ajouter une reservation");
        System.out.println("2. Supprimer une reservation");
        System.out.println("3. Afficher toutes les reservations");
        System.out.println("4. Afficher par salle");

        System.out.println("5. Quitter");
    }

    private static void ajouterReservation() {
        try {
            System.out.print("ID reservation: ");
            String id = scanner.nextLine();

            System.out.print("ID salle: ");
            String salleId = scanner.nextLine();

            System.out.print("Nom salle: ");
            String nomSalle = scanner.nextLine();

            System.out.print("Capacite: ");
            int capacite = Integer.parseInt(scanner.nextLine());

            System.out.print("Type (REUNION, FORMATION, BUREAU_PARTAGE, CONFERENCE): ");
            TypeSalle type = TypeSalle.valueOf(scanner.nextLine().trim().toUpperCase());

            Salle salle = new Salle(salleId, nomSalle, capacite, type);

            System.out.print("Date début (YYYY-MM-DDTHH:MM): ");
            LocalDateTime debut = LocalDateTime.parse(scanner.nextLine().trim());

            System.out.print("Date fin (YYYY-MM-DDTHH:MM): ");
            LocalDateTime fin = LocalDateTime.parse(scanner.nextLine().trim());

            CreneauHoraire creneau = new CreneauHoraire(debut, fin);
            Reservation reservation = new Reservation(id, salle, "Client", creneau);

            boolean ajoute = gestion.ajouterReservation(reservation);
            if (ajoute) {
                System.out.println("Reservation ajoutee avec succees.");
            } else {
                System.out.println("Erreur: Conflit d'horaires ou doublon detecte.");
            }
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private static void supprimerReservation() {
        System.out.print("ID reservation a supprimer: ");
        String id = scanner.nextLine();
        boolean supprime = gestion.supprimerReservation(id);
        System.out.println(supprime ? "Reservation supprimee." : "Reservation introuvable.");
    }

    private static void afficherReservations() {
        Set<Reservation> reservations = gestion.getReservations();
        if (reservations.isEmpty()) {
            System.out.println("Aucune reservation.");
        } else {
            for (Reservation r : reservations) {
                System.out.println(r);
            }
        }
    }

    private static void afficherParSalle() {
        System.out.print("ID salle: ");
        String salleId = scanner.nextLine();
        Set<Reservation> reservations = gestion.getReservationsParSalle(salleId);
        if (reservations.isEmpty()) {
            System.out.println("Aucune reservation pour cette salle.");
        } else {
            for (Reservation r : reservations) {
                System.out.println(r);
            }
        }
    }

    private static void chargerDonnees() {
        Set<Reservation> reservations = FichierIndexe.charger();
        for (Reservation r : reservations) {
            gestion.getReservations().add(r);
        }
        System.out.println(reservations.size() + " reservation(s) chargee(s).");
    }
}
