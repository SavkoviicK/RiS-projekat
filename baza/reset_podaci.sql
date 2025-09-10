USE veterinarska_stanica;

-- prvo isključi FK provere
SET FOREIGN_KEY_CHECKS = 0;

-- praznimo i resetujemo redom
DELETE FROM medicinski_zapisi;
ALTER TABLE medicinski_zapisi AUTO_INCREMENT = 1;

DELETE FROM pregledi;
ALTER TABLE pregledi AUTO_INCREMENT = 1;

DELETE FROM ljubimci;
ALTER TABLE ljubimci AUTO_INCREMENT = 1;

DELETE FROM usluge;
ALTER TABLE usluge AUTO_INCREMENT = 1;

DELETE FROM korisnici;
ALTER TABLE korisnici AUTO_INCREMENT = 1;

-- vrati FK provere
SET FOREIGN_KEY_CHECKS = 1;
