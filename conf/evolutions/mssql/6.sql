# --- !Ups
CREATE TABLE Gatos(
     id NUMERIC(19) IDENTITY(1,1) NOT NULL primary key,
     nombre VARCHAR(255),
     raza VARCHAR(255),
     colorOjos VARCHAR(255),
     colorPelo VARCHAR(255),
     cantPatas INT,
     muerde BIT
);

INSERT INTO Gatos(nombre, raza, colorOjos, colorPelo, cantPatas, muerde)
VALUES ('Luna','Atigrada','Verde','gris',4,1);
VALUES ('Nestor','Atigrado','Verde','Gris',4,0);
VALUES ('Pumba','Caracal','Negros','Amarillo',4,1);

# --- !Downs
DROP TABLE Gatos;