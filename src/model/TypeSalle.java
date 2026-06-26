package model;

public enum TypeSalle {
    REUNION,
    FORMATION,
    BUREAU_PARTAGE,
    CONFERENCE
}

//javac -d bin src/*.java   # compile
// java -cp bin Main         # lance

// javac -d bin src/model/*.java src/service/*.java src/ihm/*.java
// java -cp bin ihm.FenetrePrincipale