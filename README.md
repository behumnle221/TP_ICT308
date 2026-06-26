# TP_ICT308 — Système de Réservation et d'Allocation de Salles de Co-working

## Résumé technique

### 1. POO & Encapsulation
- **Encapsulation totale** : tous les attributs des classes (`Salle`, `Reservation`, `CreneauHoraire`) sont `private`. Seuls des getters sont exposés ; les objets sont immutables une fois créés.
- **Responsabilités séparées** :
  - `Salle` : identité et caractéristiques d'une salle.
  - `CreneauHoraire` : validation et détection de chevauchement d'horaires.
  - `Reservation` : agrège `Salle` + `CreneauHoraire` + client ; implémente `Serializable`.
- **Collections** : `HashSet<Reservation>` garantit l'unicité par `equals()`/`hashCode()` basés sur l'ID. Un doublon (même ID) est automatiquement rejeté.




### 2. Algorithmes & Gestion des conflits
- **Détection de chevauchement** (`CreneauHoraire.chevauche`) : condition standard `[debutA < finB && debutB < finA]`.
- **Contrôle métier** (`GestionReservations.ajouterReservation`) : avant insertion, parcours du `HashSet` pour vérifier qu'aucune réservation existante sur la même salle ne possède un créneau chevauchant.




### 3. Persistance (fichiers sérialisés + fichier index)
- **Sauvegarde** (`FichierIndexe.sauvegarder`) :
  - *Flux binaire* : sérialisation du `HashSet<Reservation>` dans `reservations.dat` via `ObjectOutputStream` (try-with-resources → fermeture auto).
  - *Index texte* : écriture ligne par ligne dans `reservations_index.txt` au format `index|id|salleId|client|debut|fin` pour consultation humaine sans désérialisation.
- **Chargement** (`FichierIndexe.charger`) :
  - Vérifie l'existence du fichier `.dat` ; si absent, retourne un `HashSet` vide.
  - Désérialisation via `ObjectInputStream` (try-with-resources). En cas d'erreur, capture silencieuse + retour d'un ensemble vide (pas de crash).
- **Sauvegarde automatique** : un *shutdown hook* JVM déclenche la persistance à la fermeture du programme.




### 4. CRUD couvert
| Opération | Méthode | Description |
|-----------|---------|-------------|
| **Create** | `ajouterReservation` | Ajoute si pas de conflit ni doublon |
| **Read** | `getReservations`, `getReservationsParSalle` | Retourne des copies défensives du Set |
| **Delete** | `supprimerReservation` | Suppression par ID |
| **Update** | *non implémenté* (hors périmètre console) | Pourrait être ajouté via suppression + recréation |




### 5. Thread-safety
- Toutes les méthodes de `GestionReservations` sont `synchronized` : l'accès concurrent au `HashSet` est protégé.

### Points forts démontrés
- Zéro fuite de ressource (try-with-resources systématique).
- Séparation nette couche métier / couche persistance.
- Unicité garantie par la collection, pas par vérification manuelle redondante.
- Format de date ISO-8601 (`LocalDateTime`) pour éviter les ambiguïtés.

### Compilation & Exécution
```bash
javac -d bin src/*.java
java -cp bin Main
```

### Format de date attendu
`YYYY-MM-DDTHH:MM` (ex: `2026-06-26T09:00`)
