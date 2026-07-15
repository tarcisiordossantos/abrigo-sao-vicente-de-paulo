package br.com.abrigosaovicente.web.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.abrigosaovicente.web.model.Midia;

public interface MidiaRepository extends JpaRepository<Midia, Long> {
    List<Midia> findBySecaoAndAtivoTrue(String secao);
    Optional<Midia> findBySecao(String secao);
}
