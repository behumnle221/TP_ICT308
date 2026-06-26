package model;

import java.io.Serializable;

public class Salle implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String nom;
    private int capacite;
    private TypeSalle type;

    public Salle(String id, String nom, int capacite, TypeSalle type) {
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getCapacite() {
        return capacite;
    }

    public TypeSalle getType() {
        return type;
    }

    @Override
    public String toString() {
        return nom + " (" + type + ", cap: " + capacite + ")";
    }
}
