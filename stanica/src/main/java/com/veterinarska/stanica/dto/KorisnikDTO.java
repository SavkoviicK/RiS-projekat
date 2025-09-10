package com.veterinarska.stanica.dto;

import com.veterinarska.stanica.model.Uloga;

public class KorisnikDTO {
    private Long id;
    private String email;
    private String ime;
    private String prezime;
    private Uloga uloga;

    public KorisnikDTO() {}
    public KorisnikDTO(Long id, String email, String ime, String prezime, Uloga uloga) {
        this.id = id; this.email = email; this.ime = ime; this.prezime = prezime; this.uloga = uloga;
    }

    // getteri/setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }
    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }
    public Uloga getUloga() { return uloga; }
    public void setUloga(Uloga uloga) { this.uloga = uloga; }
}
