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