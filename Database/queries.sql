-- A collection of some useful quieries

-- Finds all employees in group ?
SELECT ansatt.ansattNR, fornavn, etternavn, navn, leder
FROM ansatt
JOIN gruppeAnsatt
ON ansatt.ansattNR = gruppeAnsatt.ansattNR
JOIN gruppe
ON gruppe.gruppeID = gruppeAnsatt.gruppeID
WHERE gruppe.gruppeID =  --?
;

-- Finds all employees invited to appointment ?
SELECT ansatt.ansattNR, fornavn, etternavn
FROM ansatt
JOIN avtaleAnsatt
ON avtaleAnsatt.ansattNR = ansatt.ansattNR
JOIN avtale
ON avtale.avtaleID = avtaleAnsatt.avtaleID
WHERE avtale.avtaleID = --?
;

-- Finds the user with employeeNo ? and all associated information
SELECT * FROM ansatt
WHERE ansattNR = 623903
;

-- Finds the user with username ? and all associated information
SELECT * FROM ansatt
WHERE brukernavn = "hermoine@firmax.no"
;

-- Inserts a new appointment
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

-- Deletes appointment ?
DELETE FROM avtale
WHERE avtaleID = --?
;

-- Finds the latest addition to tables with auto_increment IDs
SELECT * FROM avtale
WHERE avtaleID = (
	SELECT MAX(avtaleID) from avtale
	)
;