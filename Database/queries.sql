SELECT ansatt.ansattNR, fornavn, etternavn, navn, leder
FROM ansatt
JOIN gruppeAnsatt
ON ansatt.ansattNR = gruppeAnsatt.ansattNR
JOIN gruppe
ON gruppe.gruppeID = gruppeAnsatt.gruppeID
WHERE gruppe.gruppeID = 1
;

SELECT fornavn, etternavn
FROM ansatt
JOIN avtaleAnsatt
ON avtaleAnsatt.ansattNR = ansatt.ansattNR
JOIN avtale
ON avtale.avtaleID = avtaleAnsatt.avtaleID
WHERE avtale.avtaleID = 3
;