package ihm;

import model.Salle;
import model.Reservation;
import service.GestionReservations;

import javax.swing.table.AbstractTableModel;
import java.time.LocalTime;
import java.util.List;

public class ModeleGrille extends AbstractTableModel {
    private String[] colonnes;
    private Object[][] donnees;
    private List<?> salles;
    private GestionReservations gestion;

    public ModeleGrille(List<?> salles, GestionReservations gestion) {
        this.salles = salles;
        this.gestion = gestion;
        initialiserDonnees();
    }

    private void initialiserDonnees() {
        colonnes = new String[salles.size() + 1];
        colonnes[0] = "Heure";
        for (int i = 0; i < salles.size(); i++) {
            colonnes[i + 1] = ((Salle) salles.get(i)).getNom();
        }

        donnees = new Object[11][salles.size() + 1];
        for (int i = 0; i < 11; i++) {
            int heure = 8 + i;
            donnees[i][0] = String.format("%02d:00", heure);
            for (int j = 0; j < salles.size(); j++) {
                donnees[i][j + 1] = verifierOccupation(heure, ((Salle) salles.get(j)).getId());
            }
        }
    }

    private String verifierOccupation(int heure, String salleId) {
        java.time.LocalDateTime debut = java.time.LocalDateTime.of(
            java.time.LocalDate.now(), java.time.LocalTime.of(heure, 0));
        java.time.LocalDateTime fin = debut.plusHours(1);

        for (Reservation r : gestion.getReservationsParSalle(salleId)) {
            if (r.getCreneau().getDateDebut().equals(debut)
                    && r.getCreneau().getDateFin().equals(fin)) {
                return "Occupé";
            }
        }
        return "Libre";
    }

    @Override
    public int getRowCount() {
        return donnees.length;
    }

    @Override
    public int getColumnCount() {
        return colonnes.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return donnees[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return colonnes[column];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column > 0;
    }
}
