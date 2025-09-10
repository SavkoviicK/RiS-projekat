package com.veterinarska.stanica.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pregledi")
public class Pregled {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ljubimac_id", nullable = false)
    private Ljubimac ljubimac;

    @ManyToOne
    @JoinColumn(name = "veterinar_id", nullable = false)
    private Korisnik veterinar;

    @ManyToOne
    @JoinColumn(name = "usluga_id", nullable = false)
    private Usluga usluga;

    @Enumerated(EnumType.STRING)
    @Column(length=15)
    private StatusPregleda status = StatusPregleda.ZAKAZAN;

    @Column(name="datum_pocetka", nullable=false)
    private LocalDateTime datumPocetka;

    @Column(name="datum_zavrsetka")
    private LocalDateTime datumZavrsetka;

    private String napomena;

    // getteri/setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Ljubimac getLjubimac() { return ljubimac; }
    public void setLjubimac(Ljubimac ljubimac) { this.ljubimac = ljubimac; }
    public Korisnik getVeterinar() { return veterinar; }
    public void setVeterinar(Korisnik veterinar) { this.veterinar = veterinar; }
    public Usluga getUsluga() { return usluga; }
    public void setUsluga(Usluga usluga) { this.usluga = usluga; }
    public StatusPregleda getStatus() { return status; }
    public void setStatus(StatusPregleda status) { this.status = status; }
    public LocalDateTime getDatumPocetka() { return datumPocetka; }
    public void setDatumPocetka(LocalDateTime datumPocetka) { this.datumPocetka = datumPocetka; }
    public LocalDateTime getDatumZavrsetka() { return datumZavrsetka; }
    public void setDatumZavrsetka(LocalDateTime datumZavrsetka) { this.datumZavrsetka = datumZavrsetka; }
    public String getNapomena() { return napomena; }
    public void setNapomena(String napomena) { this.napomena = napomena; }
}
