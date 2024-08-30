package com.example.projetomusica.repositories;

import com.example.projetomusica.models.Banda;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface BandaRepository extends JpaRepository<Banda, Long> {
    Page<Banda> findAll(Pageable pageable);
    Banda findByNome(String nome);
}
