package service;

import model.Reservation;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class FichierIndexe {
    private static final String FICHIER_DONNEES = "reservations.dat";
    private static final String FICHIER_INDEX = "reservations_index.txt";

    public static void sauvegarder(Set<Reservation> reservations) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FICHIER_DONNEES))) {
            oos.writeObject(new HashSet<>(reservations));
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde donnees: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FICHIER_INDEX))) {
            int index = 0;
            for (Reservation r : reservations) {
                writer.write(index + "|" + r.getId() + "|" + r.getSalle().getId() + "|" +
                             r.getNomClient() + "|" + r.getCreneau().getDateDebut() + "|" +
                             r.getCreneau().getDateFin());
                writer.newLine();
                index++;
            }
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde index: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static Set<Reservation> charger() {
        File fichier = new File(FICHIER_DONNEES);
        if (!fichier.exists()) {
            return new HashSet<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FICHIER_DONNEES))) {
            return (Set<Reservation>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur chargement: " + e.getMessage());
            return new HashSet<>();
        }
    }
}
