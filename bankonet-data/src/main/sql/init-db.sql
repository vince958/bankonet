INSERT INTO clients (login, password, nom, prenom, civilite) VALUES ('Test1', 'Test1', 'Test1', 'Test1', 'MONSIEUR');
INSERT INTO clients (login, password, nom, prenom, civilite) VALUES ('Test2', 'Test2', 'Test2', 'Test2', 'MADAME');
INSERT INTO clients (login, password, nom, prenom, civilite) VALUES ('Test3', 'Test3', 'Test3', 'Test3', 'MONSIEUR');
INSERT INTO clients (login, password, nom, prenom, civilite) VALUES ('Test4', 'Test4', 'Test4', 'Test4', 'MADAME');
INSERT INTO clients (login, password, nom, prenom, civilite) VALUES ('Test5', 'Test5', 'Test5', 'Test5', 'MADEMOISELLE');

INSERT INTO clients_comptes (login, intitule) VALUES ('Test1', 'CC1');
INSERT INTO clients_comptes (login, intitule) VALUES ('Test2', 'CC2');
INSERT INTO clients_comptes (login, intitule) VALUES ('Test3', 'CC3');
INSERT INTO clients_comptes (login, intitule) VALUES ('Test4', 'CC4');
INSERT INTO clients_comptes (login, intitule) VALUES ('Test5', 'CC5');

INSERT INTO comptes (intitule, numero, type, libelle, solde, decouvert, taux) VALUES ('CC1', '1', 'COURANT', 'Test1_libelle', 0, 500, 2.5);
INSERT INTO comptes (intitule, numero, type, libelle, solde, decouvert, taux) VALUES ('CC2', '2', 'COURANT', 'Test2_libelle', 0, 500, 2.5);
INSERT INTO comptes (intitule, numero, type, libelle, solde, decouvert, taux) VALUES ('CC3', '3', 'COURANT', 'Test3_libelle', 0, 500, 2.5);
INSERT INTO comptes (intitule, numero, type, libelle, solde, decouvert, taux) VALUES ('CC4', '4', 'COURANT', 'Test4_libelle', 0, 500, 2.5);
INSERT INTO comptes (intitule, numero, type, libelle, solde, decouvert, taux) VALUES ('CC5', '5', 'COURANT', 'Test5_libelle', 0, 500, 2.5);