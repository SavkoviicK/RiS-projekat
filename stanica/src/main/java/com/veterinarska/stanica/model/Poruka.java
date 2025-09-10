package com.veterinarska.stanica.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "poruke")
public class Poruka {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "posiljalac_id", nullable = false)
    private Korisnik posiljalac;

    @ManyToOne(optional = false)
    @JoinColumn(name = "primalac_id", nullable = false)
    private Korisnik primalac;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String sadrzaj;

    @Column(name = "vreme", nullable = false)
    private LocalDateTime vreme;

    @Column(nullable = false)
    private boolean procitana = false;   

    @PrePersist
    public void prePersist() {
        if (vreme == null) vreme = LocalDateTime.now();
    }

    /* --- get/set --- */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Korisnik getPosiljalac() { return posiljalac; }
    public void setPosiljalac(Korisnik posiljalac) { this.posiljalac = posiljalac; }

    public Korisnik getPrimalac() { return primalac; }
    public void setPrimalac(Korisnik primalac) { this.primalac = primalac; }

    public String getSadrzaj() { return sadrzaj; }
    public void setSadrzaj(String sadrzaj) { this.sadrzaj = sadrzaj; }

    public LocalDateTime getVreme() { return vreme; }
    public void setVreme(LocalDateTime vreme) { this.vreme = vreme; }

    public boolean isProcitana() { return procitana; }
    public void setProcitana(boolean procitana) { this.procitana = procitana; }

    
    @Transient
    public Date getVremeAsDate() {
        return java.sql.Timestamp.valueOf(this.vreme);
    }
}
