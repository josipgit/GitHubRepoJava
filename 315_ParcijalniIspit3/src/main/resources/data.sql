-- data.sgl on location C:\Users\josip\IntelliJProjects\315_ParcijalniIspit3\src\main\resources\data.sql

-- Čišćenje podataka
DELETE FROM Upis;
DELETE FROM Polaznik;
DELETE FROM ProgramObrazovanja;

-- SAMO INSERT NAREDBE BEZ KREIRANJA TABLICA
INSERT INTO Polaznik (Ime, Prezime) VALUES ('Marko', 'Marković');
INSERT INTO Polaznik (Ime, Prezime) VALUES ('Ivana', 'Ivić');
INSERT INTO Polaznik (Ime, Prezime) VALUES ('Petar', 'Petrović');

INSERT INTO ProgramObrazovanja (Naziv, CSVET) VALUES ('Java programiranje', 100);
INSERT INTO ProgramObrazovanja (Naziv, CSVET) VALUES ('Web dizajn', 80);
INSERT INTO ProgramObrazovanja (Naziv, CSVET) VALUES ('Baze podataka', 90);

-- Upisi
INSERT INTO Upis (PolaznikID, ProgramObrazovanjaID) VALUES (1, 1);
INSERT INTO Upis (PolaznikID, ProgramObrazovanjaID) VALUES (1, 2);
INSERT INTO Upis (PolaznikID, ProgramObrazovanjaID) VALUES (2, 1);
INSERT INTO Upis (PolaznikID, ProgramObrazovanjaID) VALUES (3, 3);