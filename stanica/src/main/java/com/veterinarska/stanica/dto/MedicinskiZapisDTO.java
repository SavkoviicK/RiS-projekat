package com.veterinarska.stanica.dto;

import java.time.LocalDate;

public class MedicinskiZapisDTO {

    private Long id;

    private Long ljubimacId;
    private String ljubimacIme;
    private String ljubimacPrikaz;

    private Long veterinarId;
    private String veterinarIme;

    private String vlasnikIme;
    private String vlasnikPrezime;

    private String dijagnoza;
    private String terapija;

    private LocalDate datum;

    public MedicinskiZapisDTO() {}

    public MedicinskiZapisDTO(
            Long id,
            Long ljubimacId,
            String ljubimacIme,
            Long veterinarId,
            String veterinarIme,
            String dijagnoza,
            String terapija,
            LocalDate datum
    ) {
        this.id = id;
        this.ljubimacId = ljubimacId;
        this.ljubimacIme = ljubimacIme;
        this.veterinarId = veterinarId;
        this.veterinarIme = veterinarIme;
        this.dijagnoza = dijagnoza;
        this.terapija = terapija;
        this.datum = datum;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLjubimacId() { return ljubimacId; }
    public void setLjubimacId(Long ljubimacId) { this.ljubimacId = ljubimacId; }

    public String getLjubimacIme() { return ljubimacIme; }
    public void setLjubimacIme(String ljubimacIme) { this.ljubimacIme = ljubimacIme; }

    public String getLjubimacPrikaz() { return ljubimacPrikaz; }
    public void setLjubimacPrikaz(String ljubimacPrikaz) { this.ljubimacPrikaz = ljubimacPrikaz; }

    public Long getVeterinarId() { return veterinarId; }
    public void setVeterinarId(Long veterinarId) { this.veterinarId = veterinarId; }

    public String getVeterinarIme() { return veterinarIme; }
    public void setVeterinarIme(String veterinarIme) { this.veterinarIme = veterinarIme; }

    public String getVlasnikIme() { return vlasnikIme; }
    public void setVlasnikIme(String vlasnikIme) { this.vlasnikIme = vlasnikIme; }

    public String getVlasnikPrezime() { return vlasnikPrezime; }
    public void setVlasnikPrezime(String vlasnikPrezime) { this.vlasnikPrezime = vlasnikPrezime; }

    public String getDijagnoza() { return dijagnoza; }
    public void setDijagnoza(String dijagnoza) { this.dijagnoza = dijagnoza; }

    public String getTerapija() { return terapija; }
    public void setTerapija(String terapija) { this.terapija = terapija; }

    public LocalDate getDatum() { return datum; }
    public void setDatum(LocalDate datum) { this.datum = datum; }
}
