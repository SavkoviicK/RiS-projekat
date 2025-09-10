package com.veterinarska.stanica.dto;

import java.time.LocalDateTime;

public class PregledDTO {
    private Long id;

    private Long ljubimacId;
    private String ljubimacIme;

    private Long vlasnikId;
    private String vlasnikEmail;

    private Long veterinarId;
    private String veterinarIme;

    private Long uslugaId;
    private String uslugaNaziv;

    private LocalDateTime datumPocetka;
    private LocalDateTime datumZavrsetka;

    private String status;
    private String napomena;

    public PregledDTO() {}

    public PregledDTO(Long id, Long ljubimacId, String ljubimacIme,
                      Long vlasnikId, String vlasnikEmail,
                      Long veterinarId, String veterinarIme,
                      Long uslugaId, String uslugaNaziv,
                      LocalDateTime datumPocetka, LocalDateTime datumZavrsetka,
                      String status, String napomena) {
        this.id = id;
        this.ljubimacId = ljubimacId; this.ljubimacIme = ljubimacIme;
        this.vlasnikId = vlasnikId; this.vlasnikEmail = vlasnikEmail;
        this.veterinarId = veterinarId; this.veterinarIme = veterinarIme;
        this.uslugaId = uslugaId; this.uslugaNaziv = uslugaNaziv;
        this.datumPocetka = datumPocetka; this.datumZavrsetka = datumZavrsetka;
        this.status = status; this.napomena = napomena;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLjubimacId() { return ljubimacId; }
    public void setLjubimacId(Long ljubimacId) { this.ljubimacId = ljubimacId; }

    public String getLjubimacIme() { return ljubimacIme; }
    public void setLjubimacIme(String ljubimacIme) { this.ljubimacIme = ljubimacIme; }

    public Long getVlasnikId() { return vlasnikId; }
    public void setVlasnikId(Long vlasnikId) { this.vlasnikId = vlasnikId; }

    public String getVlasnikEmail() { return vlasnikEmail; }
    public void setVlasnikEmail(String vlasnikEmail) { this.vlasnikEmail = vlasnikEmail; }

    public Long getVeterinarId() { return veterinarId; }
    public void setVeterinarId(Long veterinarId) { this.veterinarId = veterinarId; }

    public String getVeterinarIme() { return veterinarIme; }
    public void setVeterinarIme(String veterinarIme) { this.veterinarIme = veterinarIme; }

    public Long getUslugaId() { return uslugaId; }
    public void setUslugaId(Long uslugaId) { this.uslugaId = uslugaId; }

    public String getUslugaNaziv() { return uslugaNaziv; }
    public void setUslugaNaziv(String uslugaNaziv) { this.uslugaNaziv = uslugaNaziv; }

    public LocalDateTime getDatumPocetka() { return datumPocetka; }
    public void setDatumPocetka(LocalDateTime datumPocetka) { this.datumPocetka = datumPocetka; }

    public LocalDateTime getDatumZavrsetka() { return datumZavrsetka; }
    public void setDatumZavrsetka(LocalDateTime datumZavrsetka) { this.datumZavrsetka = datumZavrsetka; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNapomena() { return napomena; }
    public void setNapomena(String napomena) { this.napomena = napomena; }

}
