
-- 11 Parcijalni Ispit 2.1

-- Kreiranje baze podataka JavaAdv
CREATE DATABASE JavaAdv
GO

-- Postavljanje konteksta na novokreiranu bazu
USE JavaAdv
GO




-- 11 Parcijalni Ispit 2.2

-- Kreiranje baze podataka JavaAdv
CREATE DATABASE JavaAdvHibernate
GO

-- Postavljanje konteksta na novokreiranu bazu
USE JavaAdvHibernate
GO




-- SQL kod za Parcijalni Ispit 2.1 i Parcijalni Ispit 2.2  

CREATE TABLE Polaznik (
    IDPolaznik INT IDENTITY(1,1)                     -- IDENTITY(1,1) je Auto-increment od 1 osigurava automatsko povećanje ID-eva
        CONSTRAINT PK_Polaznik PRIMARY KEY,          -- imenovano ograničenje za primarni ključ
    Ime NVARCHAR(100) NOT NULL,                       -- Obavezno ime (NOT NULL znaci da je obavezno) 
    Prezime NVARCHAR(100) NOT NULL                   -- Obavezno prezime
);


CREATE TABLE ProgramObrazovanja (
    IDProgramObrazovanja INT IDENTITY(1,1)                     -- IDENTITY(1,1) je Auto-increment od 1 osigurava automatsko povećanje ID-eva
        CONSTRAINT PK_ProgramObrazovanja PRIMARY KEY,          -- imenovano ograničenje za primarni ključ
    Naziv NVARCHAR(100) NOT NULL,                       -- Obavezno  
    CSVET INT NOT NULL                               -- Obavezno 
);


-- Kreiranje tablice Upis s relacijom prema Polaznik i ProgramObrazovanja
CREATE TABLE Upis (
    IDUpis INT IDENTITY(1,1)                      -- Auto-increment ID
        CONSTRAINT PK_Upis PRIMARY KEY,           -- Primarni ključ
    PolaznikID INT NOT NULL,                         -- Obavezan vanjski ključ 
    ProgramObrazovanjaID INT NOT NULL,               -- Obavezan vanjski ključ

    -- Vanjski ključ
    CONSTRAINT FK_Upis_Polaznik 
        FOREIGN KEY (PolaznikID)                     -- Stupac koji se referencira
        REFERENCES Polaznik(IDPolaznik)              -- Povezivanje s tablicom Polaznik
        ON DELETE CASCADE                            -- Automatsko brisanje 
        ON UPDATE NO ACTION,                         -- Zabrana promjene IDPolaznik

    -- Vanjski ključ
    CONSTRAINT FK_Upis_ProgramObrazovanja 
        FOREIGN KEY (ProgramObrazovanjaID)                     -- Stupac koji se referencira
        REFERENCES ProgramObrazovanja(IDProgramObrazovanja)              -- Povezivanje s tablicom Polaznik
        ON DELETE CASCADE                            -- Automatsko brisanje 
        ON UPDATE NO ACTION,                         -- Zabrana promjene IDPolaznik
);


CREATE OR ALTER PROCEDURE DodajPolaznika
    @Ime NVARCHAR(100),
    @Prezime NVARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

		-- Provjera za prazne stringove i samo razmake
		IF LEN(TRIM(@Ime)) = 0 OR LEN(TRIM(@Prezime)) = 0
		BEGIN
			THROW 50001, 'Ime i prezime ne smiju biti prazni!', 1;
			RETURN;
		END

        -- Provjera da Ime i Prezime nisu NULL
        IF @Ime IS NULL OR @Prezime IS NULL
        BEGIN
            THROW 50009, 'Ime i prezime su obavezni.', 1;
        END

        -- Provjera je li isti polaznik već upisan
        IF EXISTS (
            SELECT 1 FROM Polaznik
            WHERE Ime = @Ime AND Prezime = @Prezime
        )
        BEGIN
            THROW 50010, 'Polaznik već postoji.', 1;
        END

        -- Umetanje polaznika
        INSERT INTO Polaznik (Ime, Prezime)
        VALUES (@Ime, @Prezime);

        COMMIT;

        -- Poruka vidljiva iz Jave
        SELECT 'Polaznik uspješno unesen.' AS Poruka;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK;

        -- Opcionalno: vraćanje greške i prema Javi
        DECLARE @ErrMsg NVARCHAR(4000) = ERROR_MESSAGE();
        THROW 50011, @ErrMsg, 1;
    END CATCH
END;
GO


CREATE OR ALTER PROCEDURE DodajProgramObrazovanja
    @Naziv NVARCHAR(100),
    @CSVET INT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

		-- Provjera za prazne stringove i samo razmake
		IF LEN(TRIM(@Naziv)) = 0
		BEGIN
			THROW 50001, 'Naziv i CSVET ne smiju biti prazni!', 1;
			RETURN;
		END

        -- Provjera je li već postoji program s tim nazivom
        IF EXISTS (SELECT 1 FROM ProgramObrazovanja WHERE Naziv = @Naziv)
        BEGIN
            -- Zamjena za RAISERROR
            THROW 50001, 'Program obrazovanja s tim nazivom već postoji.', 1;
            -- THROW automatski prekida izvršavanje, ne treba RETURN
        END

        -- Unos novog programa
        INSERT INTO ProgramObrazovanja (Naziv, CSVET)
        VALUES (@Naziv, @CSVET);

        COMMIT;
        -- PRINT se neće vidjeti u JDBC, možete koristiti OUTPUT parametar umjesto toga
        SELECT 'Program obrazovanja uspješno unesen.' AS Poruka;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK;

        -- Ponovno bacanje greške koja će biti uhvaćena u JDBC
        THROW;
    END CATCH
END;
GO


CREATE PROCEDURE UpisiPolaznikaNaProgram
    @PolaznikID INT,
    @ProgramObrazovanjaID INT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        IF NOT EXISTS (SELECT 1 FROM Polaznik WHERE IDPolaznik = @PolaznikID)
        BEGIN
            THROW 50001, 'Polaznik s tim ID-om ne postoji.', 1;
        END

        IF NOT EXISTS (SELECT 1 FROM ProgramObrazovanja WHERE IDProgramObrazovanja = @ProgramObrazovanjaID)
        BEGIN
            THROW 50002, 'Program obrazovanja s tim ID-om ne postoji.', 1;
        END

        IF EXISTS (
            SELECT 1 FROM Upis
            WHERE PolaznikID = @PolaznikID AND ProgramObrazovanjaID = @ProgramObrazovanjaID
        )
        BEGIN
            THROW 50003, 'Polaznik je već upisan na ovaj program.', 1;
        END

        INSERT INTO Upis (PolaznikID, ProgramObrazovanjaID)
        VALUES (@PolaznikID, @ProgramObrazovanjaID);

        COMMIT;
        SELECT 'Polaznik uspješno upisan na program.' AS Poruka;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK;

        THROW;  -- vrlo važno!
    END CATCH
END;
GO

GO


CREATE PROCEDURE PrebaciPolaznikaNaDrugiProgram
    @PolaznikID INT,
    @StariProgramID INT,
    @NoviProgramID INT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        -- Provjera postoji li polaznik
        IF NOT EXISTS (SELECT 1 FROM Polaznik WHERE IDPolaznik = @PolaznikID)
        BEGIN
            THROW 50020, 'Polaznik s tim ID-om ne postoji.', 1;
        END

        -- Provjera postoje li oba programa
        IF NOT EXISTS (SELECT 1 FROM ProgramObrazovanja WHERE IDProgramObrazovanja = @StariProgramID)
        BEGIN
            THROW 50021, 'Stari program obrazovanja ne postoji.', 1;
        END

        IF NOT EXISTS (SELECT 1 FROM ProgramObrazovanja WHERE IDProgramObrazovanja = @NoviProgramID)
        BEGIN
            THROW 50022, 'Novi program obrazovanja ne postoji.', 1;
        END

        -- Provjera je li polaznik upisan na stari program
        IF NOT EXISTS (
            SELECT 1 FROM Upis 
            WHERE PolaznikID = @PolaznikID AND ProgramObrazovanjaID = @StariProgramID
        )
        BEGIN
            THROW 50023, 'Polaznik nije upisan na stari program.', 1;
        END

        -- Provjera da već nije upisan na novi program
        IF EXISTS (
            SELECT 1 FROM Upis 
            WHERE PolaznikID = @PolaznikID AND ProgramObrazovanjaID = @NoviProgramID
        )
        BEGIN
            THROW 50024, 'Polaznik je već upisan na novi program.', 1;
        END

        -- Ažuriranje upisa
        UPDATE Upis
        SET ProgramObrazovanjaID = @NoviProgramID
        WHERE PolaznikID = @PolaznikID AND ProgramObrazovanjaID = @StariProgramID;

        COMMIT;

        -- Poruka koju Java može dohvatiti
        SELECT 'Polaznik je prebačen na novi program.' AS Poruka;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK;

        -- Prosljeđivanje greške prema van
        DECLARE @ErrMsg NVARCHAR(4000) = ERROR_MESSAGE();
        THROW 50025, @ErrMsg, 1;
    END CATCH
END;
GO


CREATE PROCEDURE DohvatiSvePolaznike
AS
BEGIN
	SELECT * FROM Polaznik;
END;
EXEC DohvatiSvePolaznike;


CREATE PROCEDURE DohvatiSveProgrameObrazovanja
AS
BEGIN
	SELECT * FROM ProgramObrazovanja;
END;
EXEC DohvatiSveProgrameObrazovanja;


CREATE PROCEDURE DohvatiSveUpise
AS
BEGIN
	SELECT * FROM Upis;
END;
EXEC DohvatiSveUpise;

-----------------------------------------------------------------------------------

-- ISPIS PODATAKA U TABLICAMA UKOLIKO POSTOJE  
SELECT * FROM Polaznik 
SELECT * FROM ProgramObrazovanja  
SELECT * FROM Upis

-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------

-- ISPIS (PRINT) SVIH TABLICA S PRETHODNOM PROVJEROM DA LI POSTOJE:

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'Polaznik_Table')
BEGIN
    SELECT 
        --'Polaznik' AS Polaznik_Tablica,
        IDPolaznik,
        Ime,
        Prezime,
        Username,
        Password
    FROM Polaznik_Table;
END
ELSE
BEGIN
    PRINT 'Tablica Polaznik_Table ne postoji u bazi.';
END

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'ProgramObrazovanja_Table')
BEGIN
    SELECT IDProgramObrazovanja,Naziv,CSVET FROM ProgramObrazovanja_Table;
END
ELSE
BEGIN
    PRINT 'Tablica ProgramObrazovanja_Table ne postoji u bazi.';
END

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'Upis_Table')
BEGIN
    SELECT * FROM Upis_Table;
END
ELSE
BEGIN
    PRINT 'Tablica Upis_Table ne postoji u bazi.';
END

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'UserInfo_Table')
BEGIN
    SELECT * FROM UserInfo_Table;
END
ELSE
BEGIN
    PRINT 'Tablica UserInfo_Table ne postoji u bazi.';
END

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'UserRole_Table')
BEGIN
    SELECT * FROM UserRole_Table;
END
ELSE
BEGIN
    PRINT 'Tablica UserRole_Table ne postoji u bazi.';
END

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'User_Roles')
BEGIN
    SELECT * FROM User_Roles;
END
ELSE
BEGIN
    PRINT 'Tablica User_Roles ne postoji u bazi.';
END

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'RefreshToken_Table')
BEGIN
    SELECT 
        --'RefreshToken' AS RefreshToken_Tablica,
        IDRefreshToken,
        token,
        expiryDate,
        UserInfoID
    FROM RefreshToken_Table;
END
ELSE
BEGIN
    PRINT 'Tablica RefreshToken_Table ne postoji u bazi.';
END

--IF EXISTS (SELECT * FROM sys.tables WHERE name = 'RefreshToken_Table')
--BEGIN
--    SELECT 
--        'RefreshToken' AS RefreshToken_Tablica,
--        RT.*
--    FROM RefreshToken_Table RT;
--END
--ELSE
--BEGIN
--    PRINT 'Tablica RefreshToken_Table ne postoji u bazi.';
--END

-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------

-- BRISANJE (DROP) SVIH TABLICA SA PRETHODNOM PROVJEROM DA LI POSTOJE:

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'Upis_Table')
BEGIN
    DROP TABLE Upis_Table;
    PRINT 'Tablica Upis_Table je obrisana.';
END
ELSE
BEGIN
    PRINT 'Tablica Upis_Table ne postoji u bazi.';
END

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'RefreshToken_Table')
BEGIN
    DROP TABLE RefreshToken_Table;
    PRINT 'Tablica RefreshToken_Table je obrisana.';
END
ELSE
BEGIN
    PRINT 'Tablica RefreshToken_Table ne postoji u bazi.';
END

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'User_Roles')
BEGIN
    DROP TABLE User_Roles;
    PRINT 'Tablica User_Roles je obrisana.';
END
ELSE
BEGIN
    PRINT 'Tablica User_Roles ne postoji u bazi.';
END

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'UserInfo_Table')
BEGIN
    DROP TABLE UserInfo_Table;
    PRINT 'Tablica UserInfo_Table je obrisana.';
END
ELSE
BEGIN
    PRINT 'Tablica UserInfo_Table ne postoji u bazi.';
END

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'UserRole_Table')
BEGIN
    DROP TABLE UserRole_Table;
    PRINT 'Tablica UserRole_Table je obrisana.';
END
ELSE
BEGIN
    PRINT 'Tablica UserRole_Table ne postoji u bazi.';
END

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'Polaznik_Table')
BEGIN
    DROP TABLE Polaznik_Table;
    PRINT 'Tablica Polaznik_Table je obrisana.';
END
ELSE
BEGIN
    PRINT 'Tablica Polaznik_Table ne postoji u bazi.';
END

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'ProgramObrazovanja_Table')
BEGIN
    DROP TABLE ProgramObrazovanja_Table;
    PRINT 'Tablica ProgramObrazovanja_Table je obrisana.';
END
ELSE
BEGIN
    PRINT 'Tablica ProgramObrazovanja_Table ne postoji u bazi.';
END

-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------

-- GENERIRANJE NAREDBI ZA DROP-ANJE FOREIGN CONSTRAINT KEY-A OD TABLICE POLAZNIK_TABLE  
-- ZATIM SPREMANJE GENERIRANE KOMANDE U VARIJABLU  
-- I ONDA POKRETANJE TE VARIJABLE KAO KOMANDE ZA BRISANJE:
-- Prvo deklariramo varijablu za spremanje generiranih naredbi:
DECLARE @DropConstraintsSQL NVARCHAR(MAX) = '';

-- Zatim generiraj DROP CONSTRAINT naredbe i spremi ih u varijablu (BEZ 'AS Drop Command'): 
SELECT 
    @DropConstraintsSQL = @DropConstraintsSQL + 
    'ALTER TABLE ' + SCHEMA_NAME(schema_id) + '.' + OBJECT_NAME(parent_object_id) + 
    ' DROP CONSTRAINT ' + name + ';' + CHAR(10) -- CHAR(10) dodaje novi red radi preglednosti
FROM 
    sys.foreign_keys
WHERE 
    OBJECT_NAME(referenced_object_id) = 'Polaznik_Table';

-- Ispiši generirane naredbe (za provjeru):  
PRINT @DropConstraintsSQL;

-- Izvrši generirane naredbe (samo ako varijabla nije prazna):  
IF LEN(@DropConstraintsSQL) > 0
    EXEC sp_executesql @DropConstraintsSQL;
ELSE
    PRINT 'Nema foreign key-eva za brisanje tablice Polaznik_Table.';

--------------------------------

-- GENERIRANJE NAREDBI ZA DROP-ANJE FOREIGN CONSTRAINT KEY-A OD TABLICE POLAZNIK_TABLE  
SELECT
    'ALTER TABLE ' + SCHEMA_NAME(schema_id) + '.' + OBJECT_NAME(parent_object_id) + 
    ' DROP CONSTRAINT ' + name + ';' AS 'Drop Command'
FROM 
    sys.foreign_keys
WHERE 
    OBJECT_NAME(referenced_object_id) = 'Polaznik_Table';

--------------------------------

-- GENERIRANJE NAREDBI ZA DROP-ANJE FOREIGN CONSTRAINT KEY-A OD TABLICE PROGRAMOBRAZOVANJA_TABLE  
-- ZATIM SPREMANJE GENERIRANE KOMANDE U VARIJABLU  
-- I ONDA POKRETANJE TE VARIJABLE KAO KOMANDE ZA BRISANJE:
-- Prvo deklariramo varijablu za spremanje generiranih naredbi:
DECLARE @DropConstraintsSQL NVARCHAR(MAX) = '';

-- Zatim generiraj DROP CONSTRAINT naredbe i spremi ih u varijablu (BEZ 'AS Drop Command'): 
SELECT 
    @DropConstraintsSQL = @DropConstraintsSQL + 
    'ALTER TABLE ' + SCHEMA_NAME(schema_id) + '.' + OBJECT_NAME(parent_object_id) + 
    ' DROP CONSTRAINT ' + name + ';' + CHAR(10) -- CHAR(10) dodaje novi red radi preglednosti
FROM 
    sys.foreign_keys
WHERE 
    OBJECT_NAME(referenced_object_id) = 'ProgramObrazovanja_Table';

-- Ispiši generirane naredbe (za provjeru):  
PRINT @DropConstraintsSQL;

-- Izvrši generirane naredbe (samo ako varijabla nije prazna):  
IF LEN(@DropConstraintsSQL) > 0
    EXEC sp_executesql @DropConstraintsSQL;
ELSE
    PRINT 'Nema foreign key-eva za brisanje tablice ProgramObrazovanja_Table.';

--------------------------------

-- GENERIRANJE NAREDBI ZA DROP-ANJE FOREIGN CONSTRAINT KEY-A OD TABLICE PROGRAMOBRAZOVANJA_TABLE  
SELECT
    'ALTER TABLE ' + SCHEMA_NAME(schema_id) + '.' + OBJECT_NAME(parent_object_id) + 
    ' DROP CONSTRAINT ' + name + ';' AS 'Drop Command'
FROM 
    sys.foreign_keys
WHERE 
    OBJECT_NAME(referenced_object_id) = 'ProgramObrazovanja_Table';

-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------

IF OBJECT_ID('Polaznik', 'U') IS NOT NULL
    SELECT * FROM Polaznik;
ELSE
    PRINT 'Tablica ne postoji';

---------------------------------

-- brisanje podataka u tablicama tako da ostanu prazne tablice 
DELETE FROM Polaznik 
DELETE FROM ProgramObrazovanja  
DELETE FROM Upis

-- brisanje tablica potpuno ako postoje (radi na SQL serveru 2016 nadalje)
DROP TABLE IF EXISTS Upis;
DROP TABLE IF EXISTS ProgramObrazovanja;
DROP TABLE IF EXISTS Polaznik;

DELETE FROM Polaznik WHERE IDPolaznik=14;
DELETE FROM ProgramObrazovanja WHERE IDProgramObrazovanja=8;

-----------------------------------------------------------------------------------

DROP PROCEDURE DohvatiSvePolaznike;
DROP PROCEDURE DohvatiSveProgrameObrazovanja;
DROP PROCEDURE DohvatiSveUpise;
DROP PROCEDURE UpisiPolaznikaNaProgram;
DROP PROCEDURE DodajProgramObrazovanja;
DROP PROCEDURE DodajPolaznika;
DROP PROCEDURE PrebaciPolaznikaNaDrugiProgram;

-----------------------------------------------------------------------------------

-- 1. Osnovna komanda za listanje svih procedura u bazi, ispis procedura: 
SELECT name, create_date, modify_date
FROM sys.procedures
ORDER BY create_date DESC;

-----------------------------------------------------------------------------------

-- @@SERVERNAME je SQL Server sistemska varijabla (globalna promenljiva)
-- vraća naziv lokalne SQL Server instance na koju ste trenutno spojeni 
SELECT @@SERVERNAME AS SQLServerName; 


-- @@VERSION je sistemska varijabla (system function) koja vraća detaljne informacije 
-- o verziji SQL Servera, build broju, operativnom sustavu i drugim metapodacima.
SELECT @@VERSION AS SQLServerDetails; 

-----------------------------------------------------------------------------------

-- PRONALAŽENJE SVIH FOREIGN KEY CONSTRAINTA KOJI REFERENCIRAJU TABLICU POLAZNIK
-- OSNOVNE INFORMACIJE  
SELECT
    fk.name AS 'Constraint Name',
    OBJECT_NAME(fk.parent_object_id) AS 'Child Table',
    COL_NAME(fkc.parent_object_id, fkc.parent_column_id) AS 'Child Column',
    OBJECT_NAME(fk.referenced_object_id) AS 'Parent Table',
    COL_NAME(fkc.referenced_object_id, fkc.referenced_column_id) AS 'Parent Column'
FROM 
    sys.foreign_keys fk
    INNER JOIN sys.foreign_key_columns fkc ON fk.object_id = fkc.constraint_object_id
WHERE 
    OBJECT_NAME(fk.referenced_object_id) = 'ProgramObrazovanja_Table';

--------------------------------

-- PRONALAŽENJE SVIH FOREIGN KEY CONSTRAINTA KOJI REFERENCIRAJU TABLICU POLAZNIK
-- DETALJNIJE INFORMACIJE   
SELECT
    fk.name AS 'Foreign Key Name',
    SCHEMA_NAME(child.schema_id) + '.' + OBJECT_NAME(fk.parent_object_id) AS 'Child Table',
    child_col.name AS 'Child Column',
    SCHEMA_NAME(parent.schema_id) + '.' + OBJECT_NAME(fk.referenced_object_id) AS 'Parent Table',
    parent_col.name AS 'Parent Column',
    fk.delete_referential_action_desc AS 'On Delete',
    fk.update_referential_action_desc AS 'On Update',
    fk.is_disabled AS 'Is Disabled'
FROM 
    sys.foreign_keys fk
    INNER JOIN sys.foreign_key_columns fkc ON fk.object_id = fkc.constraint_object_id
    INNER JOIN sys.tables child ON fk.parent_object_id = child.object_id
    INNER JOIN sys.columns child_col ON fkc.parent_object_id = child_col.object_id 
                                   AND fkc.parent_column_id = child_col.column_id
    INNER JOIN sys.tables parent ON fk.referenced_object_id = parent.object_id
    INNER JOIN sys.columns parent_col ON fkc.referenced_object_id = parent_col.object_id 
                                     AND fkc.referenced_column_id = parent_col.column_id
WHERE 
    OBJECT_NAME(fk.referenced_object_id) = 'Polaznik';

-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------

-- BRISANJE AKO IMA PODATAKA U TABLICAMA TAKO DA OSTANU PRAZNE TABLICE 
IF OBJECT_ID('dbo.Upis', 'U') IS NOT NULL
    DELETE FROM Upis;
IF OBJECT_ID('dbo.Polaznik', 'U') IS NOT NULL
    DELETE FROM Polaznik;
IF OBJECT_ID('dbo.ProgramObrazovanja', 'U') IS NOT NULL
    DELETE FROM ProgramObrazovanja;

-----------------------------------------------------------------------------------

-- BRISANJE TABLICA UKOLIKO ONE POSTOJE  
-- OVO RADI NA STARIJIM VERZIJAMA SQL SERVERA (2014 I STARIJE)  
-- GDJE DROP TABLE IF EXISTS NIJE PODRŽAN.
IF OBJECT_ID('dbo.Upis', 'U') IS NOT NULL
    DROP TABLE Upis;
IF OBJECT_ID('dbo.Polaznik', 'U') IS NOT NULL
    DROP TABLE Polaznik;
IF OBJECT_ID('dbo.ProgramObrazovanja', 'U') IS NOT NULL
    DROP TABLE ProgramObrazovanja;

-----------------------------------------------------------------------------------

-- EVO KOMPLETNOG SKUPA SQL NAREDBI ZA IZLISTAVANJE SVIH OBJEKATA U BAZI PODATAKA: 
-- UKLJUČUJUĆI TABLICE, FOREIGN KEY-EVE, INDEKSE, PROCEDURE, DEKLARACIJE I TRIGERE: 

------ 1) Tablice: 
SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE';

------ 2) Foreign key-evi: 
SELECT 
    f.name AS foreign_key_name,
    OBJECT_NAME(f.parent_object_id) AS table_name,
    COL_NAME(fc.parent_object_id, fc.parent_column_id) AS column_name,
    OBJECT_NAME(f.referenced_object_id) AS referenced_table,
    COL_NAME(fc.referenced_object_id, fc.referenced_column_id) AS referenced_column
FROM 
    sys.foreign_keys AS f
    INNER JOIN sys.foreign_key_columns AS fc ON f.object_id = fc.constraint_object_id;

------ 3) Indeksi: 
SELECT 
    t.name AS table_name,
    i.name AS index_name,
    i.type_desc,
    c.name AS column_name
FROM 
    sys.tables t
    INNER JOIN sys.indexes i ON t.object_id = i.object_id
    INNER JOIN sys.index_columns ic ON i.object_id = ic.object_id AND i.index_id = ic.index_id
    INNER JOIN sys.columns c ON ic.object_id = c.object_id AND ic.column_id = c.column_id
WHERE 
    i.type_desc <> 'HEAP'
ORDER BY 
    t.name, i.name;

------ 4) Stored procedure
SELECT name FROM sys.procedures;

------ 5) Triggeri: 
SELECT 
    name AS trigger_name,
    OBJECT_NAME(parent_id) AS table_name
FROM sys.triggers
WHERE is_ms_shipped = 0;

------ Sve u jednom pregledu: 
-- EXEC sp_help; -- Za pojedinačne objekte

-----------------------------------------------------------------------------------

-- BRISANJE SVIH USER PROCEDURA (ZNACI NE SISTEMSKIH): 
-- GENERIRAJ DROP NAREDBE ZA SVE KORISNIČKE PROCEDURE
DECLARE @sql NVARCHAR(MAX) = N'';

SELECT @sql += N'DROP PROCEDURE ' 
    + QUOTENAME(SCHEMA_NAME(p.schema_id)) 
    + '.' + QUOTENAME(p.name) + ';' + CHAR(13)
FROM sys.procedures p
WHERE p.is_ms_shipped = 0  -- isključi Microsoftove sistemske procedure
AND p.name NOT LIKE 'sp[_]%' ESCAPE '[' -- izbjegava procedure koje počinju sa sp_
AND p.name NOT LIKE 'xp[_]%' ESCAPE '[' -- izbjegava proširene procedure
AND p.name NOT LIKE 'fn[_]%' ESCAPE '['; -- izbjegava sistemske funkcije

-- Prikazi generirane naredbe (za provjeru)
PRINT @sql;

-- Izvrši naredbe (otkomentiraj nakon provjere)
EXEC sp_executesql @sql;

--------------------------------

-- ISPIS USER PROCEDURA:   
SELECT 
    SCHEMA_NAME(schema_id) AS SchemaName,
    name AS ProcedureName,
    type_desc AS ProcedureType,
    create_date,
    modify_date
FROM sys.procedures
WHERE is_ms_shipped = 0  -- Samo korisničke procedure
ORDER BY SchemaName, ProcedureName;

--------------------------------

-- ISPIS SISTEMSKIH PROCEDURA  
SELECT 
    SCHEMA_NAME(schema_id) AS SchemaName,
    name AS ProcedureName,
    type_desc AS ProcedureType,
    create_date,
    modify_date
FROM sys.procedures
WHERE is_ms_shipped = 1  -- Samo sistemske procedure
ORDER BY SchemaName, ProcedureName;









