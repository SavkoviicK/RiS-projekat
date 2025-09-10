package com.veterinarska.stanica.repository;

import com.veterinarska.stanica.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PorukaRepository extends JpaRepository<Poruka, Long> {
	
	List<Poruka> findByPosiljalacAndPrimalac(Korisnik posiljalac, Korisnik primalac);
    List<Poruka> findByPrimalac(Korisnik primalac);
    
    @Query("""
      from Poruka p
      where (p.posiljalac  = :a and p.primalac = :b)
         or (p.posiljalac  = :b and p.primalac = :a)
      order by p.vreme asc
    """)
    List<Poruka> konverzacija(Korisnik a, Korisnik b);
}
