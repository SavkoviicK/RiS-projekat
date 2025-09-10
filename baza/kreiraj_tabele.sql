USE veterinarska_stanica;

-- Tabela za korisnike
CREATE TABLE korisnici (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    lozinka VARCHAR(255) NOT NULL,
    ime VARCHAR(100),
    prezime VARCHAR(100),
    uloga ENUM('VLASNIK', 'VETERINAR', 'ADMIN') NOT NULL,
    aktivan BOOLEAN DEFAULT TRUE,
    datum_kreiranja TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela za ljubimce
CREATE TABLE ljubimci (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vlasnik_id BIGINT NOT NULL,
    ime VARCHAR(100) NOT NULL,
    vrsta VARCHAR(50),
    rasa VARCHAR(50),
    datum_rodjenja DATE,
    pol ENUM('M', 'Ž'),
    napomena TEXT,
    datum_kreiranja TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ljubimci_vlasnik FOREIGN KEY (vlasnik_id) REFERENCES korisnici(id) ON DELETE CASCADE
);

-- Tabela za usluge (vakcina, pregled, kontrola...)
CREATE TABLE usluge (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(100) NOT NULL,
    opis TEXT
);

-- Tabela za preglede (termini kod veterinara)
CREATE TABLE pregledi (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ljubimac_id BIGINT NOT NULL,
    veterinar_id BIGINT NOT NULL,
    usluga_id BIGINT NOT NULL,
    status ENUM('ZAKAZAN', 'POTVRĐEN', 'OBAVLJEN', 'OTKAZAN') DEFAULT 'ZAKAZAN',
    datum_pocetka DATETIME NOT NULL,
    datum_zavrsetka DATETIME,
    napomena TEXT,
    datum_kreiranja TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pregledi_ljubimac FOREIGN KEY (ljubimac_id) REFERENCES ljubimci(id) ON DELETE CASCADE,
    CONSTRAINT fk_pregledi_veterinar FOREIGN KEY (veterinar_id) REFERENCES korisnici(id),
    CONSTRAINT fk_pregledi_usluga FOREIGN KEY (usluga_id) REFERENCES usluge(id)
);

-- Tabela za medicinske zapise (dijagnoze, terapije)
CREATE TABLE medicinski_zapisi (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pregled_id BIGINT NOT NULL,
    dijagnoza TEXT,
    terapija TEXT,
    datum TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_medicinski_pregled FOREIGN KEY (pregled_id) REFERENCES pregledi(id) ON DELETE CASCADE
);

-- Tabela za poruke (komunikacija vlasnik - veterinar)
CREATE TABLE poruke (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    posiljalac_id BIGINT NOT NULL,
    primalac_id BIGINT NOT NULL,
    sadrzaj TEXT NOT NULL,
    datum_slanja TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_poruke_posiljalac FOREIGN KEY (posiljalac_id) REFERENCES korisnici(id) ON DELETE CASCADE,
    CONSTRAINT fk_poruke_primalac FOREIGN KEY (primalac_id) REFERENCES korisnici(id) ON DELETE CASCADE
);
