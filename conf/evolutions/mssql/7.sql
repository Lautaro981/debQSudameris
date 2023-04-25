# --- !Ups
CREATE TABLE Duenios(
                      id NUMERIC(19) IDENTITY(1,1) NOT NULL primary key,
                      nombre VARCHAR(255),
                      edad INT,
                      email VARCHAR(255),
                        );

INSERT INTO Duenios(nombre, edad, email) VALUES ('Rigoberto',31,'rigobert91@gmail');

# --- !Downs
DROP TABLE Duenios;