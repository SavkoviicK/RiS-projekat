package com.veterinarska.stanica.controller;

import com.veterinarska.stanica.dto.UslugaDTO;
import com.veterinarska.stanica.mapper.AppMapper;
import com.veterinarska.stanica.model.Usluga;
import com.veterinarska.stanica.repository.UslugaRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.veterinarska.stanica.mapper.AppMapper.toDTO;

@RestController
@RequestMapping("/api/usluge")
public class UslugaController {

    private final UslugaRepository repo;

    public UslugaController(UslugaRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/resolve")
    public Map<String, Long> resolve(@RequestParam String naziv) {
        if (naziv == null || naziv.isBlank()) {
            throw new IllegalArgumentException("naziv je obavezan");
        }
        var trimmed = naziv.trim();
        var usluga = repo.findByNazivIgnoreCase(trimmed)
                .orElseGet(() -> {
                    Usluga u = new Usluga();
                    u.setNaziv(trimmed);
                    u.setOpis("");
                    return repo.save(u);
                });
        return Map.of("id", usluga.getId());
    }

    // READ (sve usluge) -> DTO
    @GetMapping
    public List<UslugaDTO> svi() {
        return repo.findAll().stream()
                .map(AppMapper::toDTO)
                .toList();
    }

    // CREATE -> DTO
    @PostMapping
    public ResponseEntity<?> dodaj(@Valid @RequestBody Usluga u, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        var sacuvana = repo.save(u);
        return ResponseEntity.ok(toDTO(sacuvana));
    }

    // UPDATE -> DTO
    @PutMapping("/{id}")
    public ResponseEntity<?> izmeni(@PathVariable Long id,
                                    @Valid @RequestBody Usluga u,
                                    BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return repo.findById(id)
                .map(postojeca -> {
                    postojeca.setNaziv(u.getNaziv());
                    postojeca.setOpis(u.getOpis());
                    var sacuvana = repo.save(postojeca);
                    return ResponseEntity.ok(toDTO(sacuvana));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> obrisi(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
