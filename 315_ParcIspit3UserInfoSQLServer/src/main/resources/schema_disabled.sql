-- schema.sql on location C:\Users\josip\IntelliJProjects\315_ParcijalniIspit3\src\main\resources\schema.sql

DROP TABLE IF EXISTS Upis; -- prvo brises Upis jer on ima foreign key (FK) kljuc od ProgramObrazovanja i Polaznik
DROP TABLE IF EXISTS ProgramObrazovanja; -- ne mozes ga prvog izbrisat jer tablica Upis ima njegov FK
DROP TABLE IF EXISTS Polaznik; -- ne mozes ga prvog izbrisat jer tablica Upis ima njegov FK

-- Tablica Polaznik
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Polaznik')
BEGIN
    CREATE TABLE Polaznik (
        IDPolaznik INT IDENTITY(1,1)
            CONSTRAINT PK_Polaznik PRIMARY KEY,
        Ime NVARCHAR(100) NOT NULL,
        Prezime NVARCHAR(100) NOT NULL,
        Username NVARCHAR(50) NOT NULL UNIQUE,
        Password NVARCHAR(255) NOT NULL
    )
END;

-- Tablica ProgramObrazovanja
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'ProgramObrazovanja')
BEGIN
    CREATE TABLE ProgramObrazovanja (
        IDProgramObrazovanja INT IDENTITY(1,1)
            CONSTRAINT PK_ProgramObrazovanja PRIMARY KEY,
        Naziv NVARCHAR(100) NOT NULL,
        CSVET INT NOT NULL
    )
END;

-- Tablica Upis
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Upis')
BEGIN
CREATE TABLE Upis (
    IDUpis INT PRIMARY KEY IDENTITY,
    PolaznikID INT NOT NULL,
    ProgramObrazovanjaID INT NOT NULL,
    FOREIGN KEY (PolaznikID) REFERENCES Polaznik(IDPolaznik),
    FOREIGN KEY (ProgramObrazovanjaID) REFERENCES ProgramObrazovanja(IDProgramObrazovanja)
)
END;