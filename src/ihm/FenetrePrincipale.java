package ihm;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import model.Salle;
import model.TypeSalle;
import service.GestionReservations;

public class FenetrePrincipale extends JFrame {
    private GestionReservations gestion;
    private ModeleGrille modeleGrille;
    private JTable tableGrille;
    private List<Salle> salles;

    public FenetrePrincipale() {
        this.gestion = new GestionReservations();
        this.salles = creerSallesDemo();
        this.modeleGrille = new ModeleGrille(salles, gestion);

        initUI();
        chargerDonnees();
    }

    private void initUI() {
        setTitle("Systeme de Reservation - Co-working");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tableGrille = new JTable(modeleGrille);
        tableGrille.setRowHeight(40);
        tableGrille.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableGrille.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tableGrille.rowAtPoint(evt.getPoint());
                int col = tableGrille.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 1) {
                    ouvrirFormulaire(row, col);
                }
            }
        });

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if ("Occupé".equals(value)) {
                        c.setBackground(new Color(255, 182, 193));
                    } else if ("Libre".equals(value)) {
                        c.setBackground(new Color(144, 238, 144));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        };
        tableGrille.getColumnModel().getColumn(0).setPreferredWidth(60);
        for (int i = 1; i < tableGrille.getColumnCount(); i++) {
            tableGrille.getColumnModel().getColumn(i).setPreferredWidth(150);
            tableGrille.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scrollPane = new JScrollPane(tableGrille);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBoutons = new JPanel();
        JButton btnQuitter = new JButton("Quitter");
        btnQuitter.addActionListener(e -> {
            sauvegarderEtQuitter();
        });
        panelBoutons.add(btnQuitter);
        add(panelBoutons, BorderLayout.SOUTH);
    }

    private void ouvrirFormulaire(int row, int col) {
        int heureDebut = 8 + row;
        int heureFin = heureDebut + 1;
        Salle salle = salles.get(col - 1);

        LocalDateTime maintenant = LocalDateTime.now();
        LocalDateTime dateReservation = LocalDateTime.of(
            maintenant.getYear(), maintenant.getMonth(), maintenant.getDayOfMonth(),
            heureDebut, 0);
        LocalDateTime dateFin = LocalDateTime.of(
            maintenant.getYear(), maintenant.getMonth(), maintenant.getDayOfMonth(),
            heureFin, 0);

        FormulaireReservation formulaire = new FormulaireReservation(
            this, salle, dateReservation, dateFin, gestion);
        formulaire.setVisible(true);

        modeleGrille.fireTableDataChanged();
    }

    private void chargerDonnees() {
        java.util.Set<model.Reservation> reservations = service.FichierIndexe.charger();
        for (model.Reservation r : reservations) {
            gestion.getReservations().add(r);
        }
    }

    private void sauvegarderEtQuitter() {
        service.FichierIndexe.sauvegarder(gestion.getReservations());
        System.exit(0);
    }

    private List<Salle> creerSallesDemo() {
        List<Salle> liste = new ArrayList<>();
        liste.add(new Salle("S1", "Salle A", 10, TypeSalle.REUNION));
        liste.add(new Salle("S2", "Salle B", 20, TypeSalle.FORMATION));
        liste.add(new Salle("S3", "Salle C", 5, TypeSalle.BUREAU_PARTAGE));
        return liste;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FenetrePrincipale().setVisible(true);
        });
    }
}
