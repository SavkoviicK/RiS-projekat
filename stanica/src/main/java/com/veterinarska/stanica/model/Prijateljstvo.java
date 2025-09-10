package com.veterinarska.stanica.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "prijateljstva",
    uniqueConstraints = @UniqueConstraint(columnNames = {"korisnik1_id","korisnik2_id"})
)
public class Prijateljstvo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "korisnik1_id")
    private Korisnik korisnik1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "korisnik2_id")
    private Korisnik korisnik2;

    @Column(nullable = false)
    private Instant kreirano = Instant.now();

    public Long getId() { return id; }

    public Korisnik getKorisnik1() { return korisnik1; }
    public void setKorisnik1(Korisnik korisnik1) { this.korisnik1 = korisnik1; }

    public Korisnik getKorisnik2() { return korisnik2; }
    public void setKorisnik2(Korisnik korisnik2) { this.korisnik2 = korisnik2; }

    public Instant getKreirano() { return kreirano; }
    public void setKreirano(Instant kreirano) { this.kreirano = kreirano; }
}
