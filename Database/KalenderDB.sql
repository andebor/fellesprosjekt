CREATE TABLE ansatt (
	ansattNR INT NOT NULL AUTO_INCREMENT,
	fornavn VARCHAR(255),
	etternavn VARCHAR(255),
	brukernavn VARCHAR(255) NOT NULL UNIQUE,
	passord VARCHAR(100),
    salt BLOB,

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
