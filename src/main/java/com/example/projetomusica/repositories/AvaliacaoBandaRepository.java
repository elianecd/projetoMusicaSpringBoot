package com.example.projetomusica.repositories;

import com.example.projetomusica.models.AvaliacaoBanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoBandaRepository extends JpaRepository<AvaliacaoBanda, Long> {
    List<AvaliacaoBanda> findAllByIdBanda(Long idBanda);
}
