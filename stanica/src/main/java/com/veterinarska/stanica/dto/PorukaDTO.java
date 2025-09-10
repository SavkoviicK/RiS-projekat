package com.veterinarska.stanica.dto;

import java.time.LocalDateTime;

public class PorukaDTO {
    private Long id;

    private Long posiljalacId;
    private String posiljalacEmail;

    private Long primalacId;
    private String primalacEmail;

    private String sadrzaj;
    private LocalDateTime vreme;

    public PorukaDTO() {}

    public PorukaDTO(Long id, Long posiljalacId, String posiljalacEmail,
                     Long primalacId, String primalacEmail,
                     String sadrzaj, LocalDateTime vreme) {
        this.id = id; this.posiljalacId = posiljalacId; this.posiljalacEmail = posiljalacEmail;
        this.primalacId = primalacId; this.primalacEmail = primalacEmail;
        this.sadrzaj = sadrzaj; this.vreme = vreme;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPosiljalacId() { return posiljalacId; }
    public void setPosiljalacId(Long posiljalacId) { this.posiljalacId = posiljalacId; }
    public String getPosiljalacEmail() { return posiljalacEmail; }
    public void setPosiljalacEmail(String posiljalacEmail) { this.posiljalacEmail = posiljalacEmail; }
    public Long getPrimalacId() { return primalacId; }
    public void setPrimalacId(Long primalacId) { this.primalacId = primalacId; }
    public String getPrimalacEmail() { return primalacEmail; }
    public void setPrimalacEmail(String primalacEmail) { this.primalacEmail = primalacEmail; }
    public String getSadrzaj() { return sadrzaj; }
    public void setSadrzaj(String sadrzaj) { this.sadrzaj = sadrzaj; }
    public LocalDateTime getVreme() { return vreme; }
    public void setVreme(LocalDateTime vreme) { this.vreme = vreme; }
}
