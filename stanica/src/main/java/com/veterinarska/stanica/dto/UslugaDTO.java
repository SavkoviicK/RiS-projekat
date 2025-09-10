package com.veterinarska.stanica.dto;

public class UslugaDTO {
    private Long id;
    private String naziv;
    private String opis;

    public UslugaDTO() {}
    public UslugaDTO(Long id, String naziv, String opis) {
        this.id = id; this.naziv = naziv; this.opis = opis;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }
    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }
}
