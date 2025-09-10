-- reset_podaci.sql
USE veterinarska_stanica;

-- OPREZ: Ovo briše sve podatke!
SET FOREIGN_KEY_CHECKS = 0;

-- Ako imaš dodatne tabele (npr. "omiljene", "kategorije", "recept_sastojak" itd.),
-- dodaj ih ovde po ispravnom redosledu (prvo child, pa parent).

TRUNCATE TABLE medicinski_zapisi;
TRUNCATE TABLE pregledi;
TRUNCATE TABLE poruke;
TRUNCATE TABLE ljubimci;
TRUNCATE TABLE usluge;
TRUNCATE TABLE korisnici;
TRUNCATE TABLE prijateljstva;
TRUNCATE TABLE usluge;
TRUNCATE TABLE zahtevi_prijateljstvo;

SET FOREIGN_KEY_CHECKS = 1;
