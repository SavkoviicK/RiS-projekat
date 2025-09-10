package com.veterinarska.stanica.controller;

import com.veterinarska.stanica.dto.LjubimacDTO;
import com.veterinarska.stanica.mapper.AppMapper;
import com.veterinarska.stanica.model.Ljubimac;
import com.veterinarska.stanica.repository.KorisnikRepository;
import com.veterinarska.stanica.repository.LjubimacRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static com.veterinarska.stanica.mapper.AppMapper.toDTO;

@RestController
@RequestMapping("/api/ljubimci")
public class LjubimacController {

    private final LjubimacRepository repo;
    private final KorisnikRepository korisnikRepo;

    public LjubimacController(LjubimacRepository repo, KorisnikRepository korisnikRepo) {
        this.repo = repo;
        this.korisnikRepo = korisnikRepo;
    }

    private boolean hasRole(String role){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(ga -> ("ROLE_" + role).equals(ga.getAuthority()));
    }

    // CREATE (VLASNIK)
    @PostMapping
    public ResponseEntity<?> dodaj(@Valid @RequestBody Ljubimac l, BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().body("Neispravan unos.");

        // samo prijavljeni korisnik
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        var vlasnik = korisnikRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Vlasnik nije pronađen: " + email));

        l.setVlasnik(vlasnik);
        var sacuvan = repo.save(l);
        return ResponseEntity.ok(AppMapper.toDTO(sacuvan));
    }

    // READ – vraca ljubimce prijavljenog vlasnika
    @GetMapping
    public List<LjubimacDTO> moji() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        var vlasnik = korisnikRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen: " + email));

        return repo.findByVlasnikId(vlasnik.getId())
                .stream()
                .map(AppMapper::toDTO)
                .toList();
    }

    // UPDATE (vlasnik svojih ljubimaca)
    @PutMapping("/{id}")
    public ResponseEntity<?> izmeni(@PathVariable Long id,
                                    @Valid @RequestBody Ljubimac l,
                                    BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().body("Neispravan unos.");

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return repo.findById(id)
                .map(postojeci -> {
                    // dozvola: admin ili vlasnik tog ljubimca
                    boolean isAdmin = hasRole("ADMIN");
                    boolean isOwner = postojeci.getVlasnik() != null
                            && email.equalsIgnoreCase(postojeci.getVlasnik().getEmail());
                    if (!(isAdmin || isOwner)) {
                        return ResponseEntity.status(403).body("Nemate dozvolu za izmenu ovog ljubimca.");
                    }

                    postojeci.setIme(l.getIme());
                    postojeci.setVrsta(l.getVrsta());
                    postojeci.setRasa(l.getRasa());
                    postojeci.setPol(l.getPol());
                    postojeci.setNapomena(l.getNapomena());
                    var sacuvan = repo.save(postojeci);
                    return ResponseEntity.ok(toDTO(sacuvan));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE (admin ili vlasnik tog ljubimca)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> obrisi(@PathVariable Long id) {
        var opt = repo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        var l = opt.get();

        boolean isAdmin = hasRole("ADMIN");
        boolean isOwner = l.getVlasnik() != null
                && email.equalsIgnoreCase(l.getVlasnik().getEmail());
        if (!(isAdmin || isOwner)) {
            return ResponseEntity.status(403).body("Nemate dozvolu za brisanje ovog ljubimca.");
        }

        try {
            repo.deleteById(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(409)
                    .body("Ljubimac se ne može obrisati jer postoje povezani pregledi/zapisi.");
        }
    }
}
