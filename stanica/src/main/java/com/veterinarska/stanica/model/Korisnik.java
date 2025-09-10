package com.veterinarska.stanica.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.*;

@Entity
@Table(name = "korisnici")
public class Korisnik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Email je obavezan!")
    @Email(message="Neispravan format email-a!")
    @Column(nullable=false, unique=true, length=100)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message="Lozinka je obavezna!")
    @Column(name="lozinka", nullable=false, length=255)
    private String lozinka;

    @Column(length=100)
    private String ime;

    @Column(length=100)
    private String prezime;

    @Enumerated(EnumType.STRING)
    @Column(name="uloga", nullable=false, length=15)
    private Uloga uloga;

    @Column(name="aktivan")
    private Boolean aktivan = true;

    @Column(name="datum_kreiranja")
    private LocalDateTime datumKreiranja = LocalDateTime.now();

    // getteri/setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getLozinka() { return lozinka; }
    public void setLozinka(String lozinka) { this.lozinka = lozinka; }
    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }
    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }
    public Uloga getUloga() { return uloga; }
    public void setUloga(Uloga uloga) { this.uloga = uloga; }
    public Boolean getAktivan() { return aktivan; }
    public void setAktivan(Boolean aktivan) { this.aktivan = aktivan; }
    public LocalDateTime getDatumKreiranja() { return datumKreiranja; }
    public void setDatumKreiranja(LocalDateTime datumKreiranja) { this.datumKreiranja = datumKreiranja; }
    
}
