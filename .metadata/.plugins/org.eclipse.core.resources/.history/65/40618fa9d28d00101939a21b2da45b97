package com.veterinarska.stanica.controller;

import com.veterinarska.stanica.dto.KorisnikDTO;
import com.veterinarska.stanica.dto.ZahtevZaPrijateljstvoDTO;
import com.veterinarska.stanica.mapper.AppMapper;
import com.veterinarska.stanica.model.Korisnik;
import com.veterinarska.stanica.model.ZahtevZaPrijateljstvo;
import com.veterinarska.stanica.service.PrijateljiService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prijatelji")
public class PrijateljiController {

    private final PrijateljiService service;

    public PrijateljiController(PrijateljiService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> lista(Authentication auth) {
        if (auth == null || auth.getName() == null) return ResponseEntity.status(401).body("Niste prijavljeni.");
        Korisnik me = service.me(auth.getName());
        List<KorisnikDTO> out = service.listaPrijatelja(me.getId()).stream().map(AppMapper::toDTO).toList();
        return ResponseEntity.ok(out);
    }

    @GetMapping("/na-cekaju")
    public ResponseEntity<?> naCekanju(Authentication auth) {
        if (auth == null || auth.getName() == null) return ResponseEntity.status(401).body("Niste prijavljeni.");
        Korisnik me = service.me(auth.getName());
        List<ZahtevZaPrijateljstvoDTO> out = service.zahteviNaCekanju(me.getId()).stream().map(AppMapper::toDTO).toList();
        return ResponseEntity.ok(out);
    }

    @PostMapping("/zahtev")
    public ResponseEntity<?> posaljiZahtev(@RequestParam("primaocId") Long primaocId, Authentication auth) {
        if (auth == null || auth.getName() == null) return ResponseEntity.status(401).body("Niste prijavljeni.");
        if (primaocId == null) return ResponseEntity.badRequest().body("Nedostaje primaocId.");
        Korisnik me = service.me(auth.getName());
        try {
            service.posaljiZahtev(me.getId(), primaocId);
            return ResponseEntity.ok("Zahtev poslat.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/odgovor")
    public ResponseEntity<?> odgovor(@RequestParam("zahtevId") Long zahtevId,
                                     @RequestParam("prihvati") boolean prihvati,
                                     Authentication auth) {
        if (auth == null || auth.getName() == null) return ResponseEntity.status(401).body("Niste prijavljeni.");
        if (zahtevId == null) return ResponseEntity.badRequest().body("Nedostaje zahtevId.");
        Korisnik me = service.me(auth.getName());
        try {
            String msg = service.odgovorNaZahtev(zahtevId, me.getId(), prihvati);
            return ResponseEntity.ok(msg);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/kandidati")
    public ResponseEntity<?> kandidati(Authentication auth) {
        if (auth == null || auth.getName() == null) return ResponseEntity.status(401).body("Niste prijavljeni.");
        Korisnik me = service.me(auth.getName());
        List<KorisnikDTO> out = service.kandidatiZaPrijateljstvo(me.getId()).stream().map(AppMapper::toDTO).toList();
        return ResponseEntity.ok(out);
    }
}
