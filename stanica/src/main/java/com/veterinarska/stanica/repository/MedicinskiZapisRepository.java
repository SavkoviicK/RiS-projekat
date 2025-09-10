package com.veterinarska.stanica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.veterinarska.stanica.model.MedicinskiZapis;

public interface MedicinskiZapisRepository extends JpaRepository<MedicinskiZapis, Long> {
}
