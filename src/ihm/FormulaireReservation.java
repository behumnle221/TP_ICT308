package ihm;

import java.awt.*;
import java.time.LocalDateTime;
import javax.swing.*;
import model.CreneauHoraire;
import model.Reservation;
import model.Salle;
import service.GestionReservations;

public class FormulaireReservation extends JDialog {
    private Salle salle;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private GestionReservations gestion;
    private boolean valide = false;

    private JTextField tfId;
    private JTextField tfClient;

    public FormulaireReservation(Frame owner, Salle salle, LocalDateTime dateDebut,
                                LocalDateTime dateFin, GestionReservations gestion) {
        super(owner, "Réservation - " + salle.getNom(), true);
        this.salle = salle;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.gestion = gestion;

        initUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelInfos = new JPanel(new GridLayout(3, 2, 5, 5));
        panelInfos.setBorder(BorderFactory.createTitledBorder("Informations"));

        panelInfos.add(new JLabel("Salle:"));
        panelInfos.add(new JLabel(salle.getNom() + " (" + salle.getType() + ")"));
        panelInfos.add(new JLabel("Date:"));
        panelInfos.add(new JLabel(dateDebut.toLocalDate().toString()));
        panelInfos.add(new JLabel("Horaires:"));
        panelInfos.add(new JLabel(dateDebut.toLocalTime() + " - " + dateFin.toLocalTime()));

        JPanel panelForm = new JPanel(new GridLayout(2, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Réservation"));

        panelForm.add(new JLabel("ID Reservation:"));
        tfId = new JTextField();
        panelForm.add(tfId);

        panelForm.add(new JLabel("Nom Client:"));
        tfClient = new JTextField();
        panelForm.add(tfClient);

        JPanel panelBoutons = new JPanel();
        JButton btnReserver = new JButton("Reserver");
        btnReserver.addActionListener(e -> validerReservation());
        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.addActionListener(e -> dispose());

        panelBoutons.add(btnReserver);
        panelBoutons.add(btnAnnuler);

        add(panelInfos, BorderLayout.NORTH);
        add(panelForm, BorderLayout.CENTER);
        add(panelBoutons, BorderLayout.SOUTH);
    }

    private void validerReservation() {
        String id = tfId.getText().trim();
        String client = tfClient.getText().trim();

        if (id.isEmpty() || client.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Veuillez remplir tous les champs.",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CreneauHoraire creneau = new CreneauHoraire(dateDebut, dateFin);
        Reservation reservation = new Reservation(id, salle, client, creneau);

        boolean ajoute = gestion.ajouterReservation(reservation);
        if (ajoute) {
            JOptionPane.showMessageDialog(this,
                "Reservation ajoutee avec succes !",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            valide = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Conflit d'horaires ou doublon detecte !",
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isValide() {
        return valide;
    }
}
