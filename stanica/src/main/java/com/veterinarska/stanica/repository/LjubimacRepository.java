package com.veterinarska.stanica.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.veterinarska.stanica.model.Ljubimac;
import com.veterinarska.stanica.model.Korisnik;

public interface LjubimacRepository extends JpaRepository<Ljubimac, Long> {
    List<Ljubimac> findByVlasnikId(Long vlasnikId);
}