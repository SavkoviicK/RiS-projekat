package com.veterinarska.stanica.repository;

import com.veterinarska.stanica.model.Korisnik;
import com.veterinarska.stanica.model.Prijateljstvo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public interface PrijateljstvoRepository extends JpaRepository<Prijateljstvo, Long> {

    @Query("""
        select (count(p) > 0) from Prijateljstvo p
        where (p.korisnik1.id = :a and p.korisnik2.id = :b)
           or (p.korisnik1.id = :b and p.korisnik2.id = :a)
    """)
    boolean jesuPrijatelji(@Param("a") long a, @Param("b") long b);

    @Query("select p.korisnik2 from Prijateljstvo p where p.korisnik1.id = :userId")
    List<Korisnik> prijateljiGdeJeOnPrvi(@Param("userId") long userId);

    @Query("select p.korisnik1 from Prijateljstvo p where p.korisnik2.id = :userId")
    List<Korisnik> prijateljiGdeJeOnDrugi(@Param("userId") long userId);

   
    default List<Korisnik> prijatelji(long userId) {
        Set<Korisnik> set = new LinkedHashSet<>();
        set.addAll(prijateljiGdeJeOnPrvi(userId));
        set.addAll(prijateljiGdeJeOnDrugi(userId));
        // (opciono) ukloni samog korisnika ako bi se kojim slucajem pojavio
        set.removeIf(k -> k != null && k.getId() != null && k.getId() == userId);
        return new ArrayList<>(set);
    }
}
