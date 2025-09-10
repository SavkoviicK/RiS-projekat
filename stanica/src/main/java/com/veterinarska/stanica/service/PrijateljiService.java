package com.veterinarska.stanica.service;

import com.veterinarska.stanica.model.Korisnik;
import com.veterinarska.stanica.model.Prijateljstvo;
import com.veterinarska.stanica.model.ZahtevZaPrijateljstvo;
import com.veterinarska.stanica.repository.KorisnikRepository;
import com.veterinarska.stanica.repository.PrijateljstvoRepository;
import com.veterinarska.stanica.repository.ZahtevZaPrijateljstvoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PrijateljiService {

    private final KorisnikRepository korisnikRepo;
    private final PrijateljstvoRepository prijateljstvoRepo;
    private final ZahtevZaPrijateljstvoRepository zahtevRepo;

    public PrijateljiService(KorisnikRepository korisnikRepo,
                             PrijateljstvoRepository prijateljstvoRepo,
                             ZahtevZaPrijateljstvoRepository zahtevRepo) {
        this.korisnikRepo = korisnikRepo;
        this.prijateljstvoRepo = prijateljstvoRepo;
        this.zahtevRepo = zahtevRepo;
    }

    @Transactional(readOnly = true)
    public Korisnik me(String email) {
        return korisnikRepo.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));
    }

    @Transactional(readOnly = true)
    public List<Korisnik> listaPrijatelja(long myId) {
        return prijateljstvoRepo.prijatelji(myId);
    }

    @Transactional(readOnly = true)
    public List<ZahtevZaPrijateljstvo> zahteviNaCekanju(long myId) {
        Korisnik ja = korisnikRepo.findById(myId).orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));
        return zahtevRepo.findByPrimalacAndStatus(ja, ZahtevZaPrijateljstvo.Status.NA_CEKANJU);
    }

    @Transactional
    public void posaljiZahtev(long posiljalacId, long primalacId) {
        if (posiljalacId == primalacId) throw new IllegalArgumentException("Ne možete poslati zahtev sami sebi.");
        Korisnik posiljalac = korisnikRepo.findById(posiljalacId).orElseThrow(() -> new IllegalArgumentException("Pošiljalac ne postoji"));
        Korisnik primalac = korisnikRepo.findById(primalacId).orElseThrow(() -> new IllegalArgumentException("Primalac ne postoji"));
        if (prijateljstvoRepo.jesuPrijatelji(posiljalacId, primalacId)) throw new IllegalStateException("Već ste prijatelji.");
        boolean postojiNapred = zahtevRepo.existsByPosiljalacAndPrimalacAndStatus(posiljalac, primalac, ZahtevZaPrijateljstvo.Status.NA_CEKANJU);
        boolean postojiNazad  = zahtevRepo.existsByPosiljalacAndPrimalacAndStatus(primalac, posiljalac, ZahtevZaPrijateljstvo.Status.NA_CEKANJU);
        if (postojiNapred || postojiNazad) throw new IllegalStateException("Zahtev već postoji.");
        ZahtevZaPrijateljstvo z = new ZahtevZaPrijateljstvo();
        z.setPosiljalac(posiljalac);
        z.setPrimalac(primalac);
        z.setStatus(ZahtevZaPrijateljstvo.Status.NA_CEKANJU);
        zahtevRepo.save(z);
    }

    @Transactional
    public String odgovorNaZahtev(long zahtevId, long currentUserId, boolean prihvati) {
        ZahtevZaPrijateljstvo z = zahtevRepo.findById(zahtevId).orElseThrow(() -> new IllegalArgumentException("Zahtev ne postoji"));
        if (!Objects.equals(z.getPrimalac().getId(), currentUserId)) throw new IllegalStateException("Nedozvoljena akcija.");
        if (z.getStatus() != ZahtevZaPrijateljstvo.Status.NA_CEKANJU) throw new IllegalStateException("Zahtev je već obrađen.");
        if (prihvati) {
            z.setStatus(ZahtevZaPrijateljstvo.Status.PRIHVACEN);
            zahtevRepo.save(z);
            if (!prijateljstvoRepo.jesuPrijatelji(z.getPosiljalac().getId(), z.getPrimalac().getId())) {
                Prijateljstvo p = new Prijateljstvo();
                p.setKorisnik1(z.getPosiljalac());
                p.setKorisnik2(z.getPrimalac());
                prijateljstvoRepo.save(p);
            }
            return "Zahtev prihvaćen.";
        } else {
            z.setStatus(ZahtevZaPrijateljstvo.Status.ODBIJEN);
            zahtevRepo.save(z);
            return "Zahtev odbijen.";
        }
    }

    @Transactional(readOnly = true)
    public List<Korisnik> kandidatiZaPrijateljstvo(long myId) {
        Korisnik ja = korisnikRepo.findById(myId).orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));
        Set<Long> prijatelji = listaPrijatelja(myId).stream().map(Korisnik::getId).collect(Collectors.toSet());
        List<ZahtevZaPrijateljstvo> pendMe = zahtevRepo.findByPrimalacAndStatus(ja, ZahtevZaPrijateljstvo.Status.NA_CEKANJU);
        List<ZahtevZaPrijateljstvo> pendFromMe = zahtevRepo.findByPosiljalac(ja).stream()
                .filter(z -> z.getStatus() == ZahtevZaPrijateljstvo.Status.NA_CEKANJU).toList();
        Set<Long> pendingIds = new HashSet<>();
        for (ZahtevZaPrijateljstvo z : pendMe) pendingIds.add(z.getPosiljalac().getId());
        for (ZahtevZaPrijateljstvo z : pendFromMe) pendingIds.add(z.getPrimalac().getId());
        return korisnikRepo.findAll().stream()
                .filter(k -> k.getId() != null && !Objects.equals(k.getId(), myId))
                .filter(k -> !prijatelji.contains(k.getId()))
                .filter(k -> !pendingIds.contains(k.getId()))
                .toList();
    }
}
