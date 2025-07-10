-- schema.sql on location C:\Users\josip\IntelliJProjects\315_ParcijalniIspit3\src\main\resources\schema.sql

DROP TABLE IF EXISTS Upis;
DROP TABLE IF EXISTS ProgramObrazovanja;
DROP TABLE IF EXISTS Polaznik;

-- Tablica Polaznik
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Polaznik')
BEGIN
    CREATE TABLE Polaznik (
        IDPolaznik INT IDENTITY(1,1)
            CONSTRAINT PK_Polaznik PRIMARY KEY,
        Ime NVARCHAR(100) NOT NULL,
        Prezime NVARCHAR(100) NOT NULL
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