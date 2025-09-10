package com.veterinarska.stanica.controller;

import com.veterinarska.stanica.dto.PorukaDTO;
import com.veterinarska.stanica.mapper.AppMapper;
import com.veterinarska.stanica.model.Poruka;
import com.veterinarska.stanica.model.Korisnik;
import com.veterinarska.stanica.repository.KorisnikRepository;
import com.veterinarska.stanica.repository.PorukaRepository;
import com.veterinarska.stanica.service.PorukaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/poruke")
public class PorukaController {

    private final PorukaService porukaService;
    private final KorisnikRepository korisnikRepo;
    private final PorukaRepository porukaRepo;

    public PorukaController(PorukaService porukaService,
                            KorisnikRepository korisnikRepo,
                            PorukaRepository porukaRepo) {
        this.porukaService = porukaService;
        this.korisnikRepo = korisnikRepo;
        this.porukaRepo = porukaRepo;
    }

    public record PorukaReq(Long primalacId, String sadrzaj) {}

    @PostMapping
    public ResponseEntity<?> posalji(@RequestBody PorukaReq req, Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body("Niste prijavljeni.");
        }
        if (req == null || req.primalacId() == null) {
            return ResponseEntity.badRequest().body("Nedostaje primalacId.");
        }
        if (req.sadrzaj() == null || req.sadrzaj().isBlank()) {
            return ResponseEntity.badRequest().body("Nedostaje sadrzaj.");
        }

        try {
            Korisnik posiljalac = korisnikRepo.findByEmail(auth.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Pošiljalac ne postoji"));
            porukaService.posalji(posiljalac.getId(), req.primalacId(), req.sadrzaj());
            return ResponseEntity.ok("Poruka je poslata.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Greška: " + e.getClass().getSimpleName());
        }
    }

    @GetMapping
    public ResponseEntity<?> moje(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body("Niste prijavljeni.");
        }
        Korisnik ja = korisnikRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Korisnik ne postoji"));

        List<PorukaDTO> out = porukaRepo.findAll().stream()
                .filter(p -> p.getPosiljalac().getId().equals(ja.getId())
                          || p.getPrimalac().getId().equals(ja.getId()))
                .map(AppMapper::toDTO)
                .toList();
        return ResponseEntity.ok(out);
    }

    @GetMapping("/razgovor")
    public ResponseEntity<?> razgovor(@RequestParam("sa") Long prijateljId, Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body("Niste prijavljeni.");
        }
        if (prijateljId == null) {
            return ResponseEntity.badRequest().body("Nedostaje parametar ?sa=");
        }
        try {
            Korisnik ja = korisnikRepo.findByEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Korisnik ne postoji"));
            List<Poruka> poruke = porukaService.konverzacija(ja.getId(), prijateljId);
            return ResponseEntity.ok(poruke.stream().map(AppMapper::toDTO).toList());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Greška: " + e.getClass().getSimpleName());
        }
    }
}
