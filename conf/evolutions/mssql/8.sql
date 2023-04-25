# --- !Ups
ALTER TABLE Gatos
    ADD idDuenio NUMERIC(19),
    FOREIGN KEY (idDuenio) REFERENCES Duenios(id);

# --- !Downs