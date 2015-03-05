CREATE TABLE ansatt (
	ansattNR INT NOT NULL,
	fornavn VARCHAR(255),
	etternavn VARCHAR(255),
	brukernavn VARCHAR(255) NOT NULL,
	passord VARCHAR(20),

	PRIMARY KEY (ansattNR)
	);

CREATE TABLE gruppe (
	gruppeID INT NOT NULL AUTO_INCREMENT,
	navn VARCHAR(255) NOT NULL,
	leder INT NOT NULL,

	PRIMARY KEY (gruppeID),
	FOREIGN KEY (leder) REFERENCES ansatt(ansattnr)
	);

CREATE TABLE gruppeAnsatt (
	gruppeID INT NOT NULL,
	ansattNR INT NOT NULL,

	PRIMARY KEY (gruppeID, ansattnr),
	FOREIGN KEY (gruppeID) REFERENCES gruppe(gruppeID),
	FOREIGN KEY (ansattNR) REFERENCES ansatt(ansattnr)
	);

CREATE TABLE møterom (
	møteromNR INT NOT NULL,
	navn VARCHAR(255),
	kapasitet TINYINT,

	PRIMARY KEY (møteromNR)
	);

CREATE TABLE avtale (
	avtaleID INT NOT NULL AUTO_INCREMENT,
	formål VARCHAR(255),
	starttid DATETIME,
	sluttid DATETIME,
	sted VARCHAR(255),
	møteromNR INT,
	ansvarlig INT,

	PRIMARY KEY (avtaleID),
	FOREIGN KEY (møteromNR) REFERENCES møterom(møteromNR),
	FOREIGN KEY (ansvarlig) REFERENCES ansatt(ansattNR),
	);

-- CREATE TABLE avtaleMøterom (
-- 	avtaleID INT NOT NULL,
-- 	møteromNR INT NOT NULL,

-- 	PRIMARY KEY (avtaleID, møteromNR),
-- 	FOREIGN KEY (avtaleID) REFERENCES avtale(avtaleID),
-- 	FOREIGN KEY (møteromNR) REFERENCES møterom(møteromNR)
-- 	);

-- Kobling mellom ansatte og hvilke avtaler de deltar på.

CREATE TABLE avtaleAnsatt (
	avtaleID INT NOT NULL,
	ansattNR INT NOT NULL,
	status VARCHAR(255) NOT NULL DEFAULT "Venter",
	alarm INT DEFAULT 15,
	synlig BIT DEFAULT 1,

	PRIMARY KEY (avtaleID, ansattNR),
	FOREIGN KEY (avtaleID) REFERENCES avtale(avtaleID),
	FOREIGN KEY (ansattNR) REFERENCES ansatt(ansattNR)
	);

CREATE TABLE avtaleGruppe (
	avtaleID INT NOT NULL,
	gruppeID INT NOT NULL,

	PRIMARY KEY (avtaleID, gruppeID),
	FOREIGN KEY (avtaleID) REFERENCES avtale(avtaleID),
	FOREIGN KEY (gruppeID) REFERENCES gruppe(gruppeID)
	);

CREATE TABLE varsel (
	varselID INT NOT NULL AUTO_INCREMENT,
	tekst VARCHAR(1023) NOT NULL,
	sett BIT NOT NULL DEFAULT 0,
	ansattNR INT NOT NULL,

	PRIMARY KEY (varselID),
	FOREIGN KEY (ansattNR) REFERENCES ansatt(ansattNR)
	);

INSERT INTO ansatt (
ansattNR,
fornavn,
etternavn,
brukernavn,
passord)
VALUES (
623900,
"Gordon",
"Freeman",
"gordonfreeman@firmax.no",
"headcrab");

INSERT INTO ansatt (
ansattNR,
fornavn,
etternavn,
brukernavn,
passord)
VALUES (
623901,
"Kim",
"Kardashian",
"kimmyk@firmax.no",
"kanye4lyf");

INSERT INTO ansatt (
ansattNR,
fornavn,
etternavn,
brukernavn,
passord)
VALUES (
623902,
"Mother",
"Theresa",
"mothertheresa@firmax.no",
"2819jbhaFF2");

INSERT INTO ansatt (
ansattNR,
fornavn,
etternavn,
brukernavn,
passord)
VALUES (
623903,
"Emma",
"Watson",
"hermoine@firmax.no",
"CaputDraconis");

INSERT INTO ansatt (
ansattNR,
fornavn,
etternavn,
brukernavn,
passord)
VALUES (
623904,
"Morgan",
"Freeman",
"morganfreeman@firmax.no",
"emperorPengu1ns");

INSERT INTO gruppe (
navn,
leder)
VALUES (
	"Black Mesa Task Force",
	623900
);

INSERT INTO gruppe (
navn,
leder)
VALUES (
	"Gyrl Power",
	623902
);

INSERT INTO gruppe (
navn,
leder)
VALUES (
	"Old Timers",
	623904
);

INSERT INTO gruppeAnsatt (
gruppeID,
ansattNR)
VALUES (
	1,
	623900
);

INSERT INTO gruppeAnsatt (
gruppeID,
ansattNR)
VALUES (
	1,
	623904
);

INSERT INTO gruppeAnsatt (
gruppeID,
ansattNR)
VALUES (
	2,
	623902
);

INSERT INTO gruppeAnsatt (
gruppeID,
ansattNR)
VALUES (
	2,
	623901
);

INSERT INTO gruppeAnsatt (
gruppeID,
ansattNR)
VALUES (
	2,
	623903
);

INSERT INTO gruppeAnsatt (
gruppeID,
ansattNR)
VALUES (
	3,
	623904
);

INSERT INTO gruppeAnsatt (
gruppeID,
ansattNR)
VALUES (
	3,
	623902
);

INSERT INTO møterom (
møteromNR,
navn,
kapasitet)
VALUES (
201,
"Kilimanjaro",
8);

INSERT INTO møterom (
møteromNR,
navn,
kapasitet)
VALUES (
202,
"Rosa",
5);

INSERT INTO møterom (
møteromNR,
navn,
kapasitet)
VALUES (
203,
"Everest",
12);

INSERT INTO avtale (
formål,
starttid,
sluttid,
sted,
møteromNR,
ansvarlig)
VALUES (
	"Plans for world domination",
	"1998-01-23 09:15:00",
	"1998-01-23 12:00:00",
	"Firma X",
	202,
	623900
);

INSERT INTO avtale (
formål,
starttid,
sluttid,
sted,
møteromNR,
ansvarlig)
VALUES (
	"Presentation with Mother Theresa",
	"1998-05-12 13:15:00",
	"1998-05-12 14:00:00",
	"Firma X",
	203,
	623902
);

INSERT INTO avtale (
formål,
starttid,
sluttid,
sted,
ansvarlig)
VALUES (
	"Bamsemumspatrulje",
	"2011-10-02 08:15:00",
	"2011-10-02 08:20:00",
	"Statoil",
	623901
)

INSERT INTO avtaleAnsatt (
ansattNR,
avtaleID)
VALUES (
623900,
1);

INSERT INTO avtaleAnsatt (
ansattNR,
avtaleID)
VALUES (
623902,
2);

INSERT INTO avtaleAnsatt (
ansattNR,
avtaleID)
VALUES (
623901,
3);

INSERT INTO avtaleAnsatt (
ansattNR,
avtaleID)
VALUES (
623904,
3);