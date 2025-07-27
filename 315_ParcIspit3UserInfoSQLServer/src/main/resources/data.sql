---- SAMO INSERT NAREDBE BEZ KREIRANJA TABLICA
--INSERT INTO Polaznik_Table (Ime, Prezime, Username, Password) VALUES ('Marko', 'Markovic', 'usermarko', 'passmarko');
--INSERT INTO Polaznik_Table (Ime, Prezime, Username, Password) VALUES ('Ivana', 'Ivic','userivana','passivana');
--INSERT INTO Polaznik_Table (Ime, Prezime, Username, Password) VALUES ('Petar', 'Petrovic','userpetar','passpetar');
--
--INSERT INTO ProgramObrazovanja_Table (Naziv, CSVET) VALUES ('Java programiranje', 100);
--INSERT INTO ProgramObrazovanja_Table (Naziv, CSVET) VALUES ('Web dizajn', 80);
--INSERT INTO ProgramObrazovanja_Table (Naziv, CSVET) VALUES ('Baze podataka', 90);
--
--INSERT INTO Upis_Table (PolaznikID, ProgramObrazovanjaID) VALUES (1, 1);
--INSERT INTO Upis_Table (PolaznikID, ProgramObrazovanjaID) VALUES (1, 2);
--INSERT INTO Upis_Table (PolaznikID, ProgramObrazovanjaID) VALUES (2, 1);
--INSERT INTO Upis_Table (PolaznikID, ProgramObrazovanjaID) VALUES (3, 3);
--
--SET IDENTITY_INSERT UserInfo_Table ON;
--INSERT INTO UserInfo_Table (IDUserInfo, Username, Password)
--VALUES
--    (1, 'user', '$2a$12$h0HcS2QDb/7zPASbLa2GoOTSRP6CWK0oX7pCK.dPjkM6L5N4pNovi'), -- password = user
--    (2, 'admin', '$2a$12$INo0nbj40sQrTB7b28KJput/bNltGmFyCfRsUhvy73qcXo5/XdsTG'); -- password = admin
----    ('user', '$2a$12$h0HcS2QDb/7zPASbLa2GoOTSRP6CWK0oX7pCK.dPjkM6L5N4pNovi'), -- password = user
----    ('admin', '$2a$12$INo0nbj40sQrTB7b28KJput/bNltGmFyCfRsUhvy73qcXo5/XdsTG'); -- password = admin
--SET IDENTITY_INSERT UserInfo_Table OFF;
--
--INSERT INTO UserRole_Table (IDUserRole, UserRoleName)
--VALUES
--    (1, 'ROLE_ADMIN'),
--    (2, 'ROLE_USER');
--
--INSERT INTO User_Roles (UserInfoID, UserRoleID)
--VALUES
--    (1, 2),  --    1-user,  2-ROLE_USER
--    (2, 1);  --    2-admin, 1-ROLE_ADMIN




-- SAMO INSERT NAREDBE BEZ KREIRANJA TABLICA

-- ===== POLAZNICI =====
INSERT INTO Polaznik_Table (Ime, Prezime, Username, Password)
VALUES ('Marko', 'Markovic', 'usermarko', 'passmarko');
INSERT INTO Polaznik_Table (Ime, Prezime, Username, Password)
VALUES ('Ivana', 'Ivic','userivana','passivana');
INSERT INTO Polaznik_Table (Ime, Prezime, Username, Password)
VALUES ('Petar', 'Petrovic','userpetar','passpetar');

-- ===== PROGRAMI =====
INSERT INTO ProgramObrazovanja_Table (Naziv, CSVET)
VALUES ('Java programiranje', 100);
INSERT INTO ProgramObrazovanja_Table (Naziv, CSVET)
VALUES ('Web dizajn', 80);
INSERT INTO ProgramObrazovanja_Table (Naziv, CSVET)
VALUES ('Baze podataka', 90);

-- ===== UPISI =====
INSERT INTO Upis_Table (PolaznikID, ProgramObrazovanjaID) VALUES (1, 1);
INSERT INTO Upis_Table (PolaznikID, ProgramObrazovanjaID) VALUES (1, 2);
INSERT INTO Upis_Table (PolaznikID, ProgramObrazovanjaID) VALUES (2, 1);
INSERT INTO Upis_Table (PolaznikID, ProgramObrazovanjaID) VALUES (3, 3);

-- ===== USER INFO =====
-- Potrebno jer IDUserInfo je IDENTITY
SET IDENTITY_INSERT UserInfo_Table ON;
INSERT INTO UserInfo_Table (IDUserInfo, Username, Password)
VALUES
    (1, 'user', '$2a$12$h0HcS2QDb/7zPASbLa2GoOTSRP6CWK0oX7pCK.dPjkM6L5N4pNovi'), -- password = user
    (2, 'admin', '$2a$12$INo0nbj40sQrTB7b28KJput/bNltGmFyCfRsUhvy73qcXo5/XdsTG'); -- password = admin
SET IDENTITY_INSERT UserInfo_Table OFF;

-- ===== USER ROLES =====
-- IDUserRole je također IDENTITY => koristi IDENTITY_INSERT
SET IDENTITY_INSERT UserRole_Table ON;
INSERT INTO UserRole_Table (IDUserRole, UserRoleName)
VALUES
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER');
SET IDENTITY_INSERT UserRole_Table OFF;

-- ===== VEZA KORISNIKA I ULOGA =====
INSERT INTO User_Roles (UserInfoID, UserRoleID)
VALUES
    (1, 2),  -- 1-user, 2-ROLE_USER
    (2, 1);  -- 2-admin, 1-ROLE_ADMIN
