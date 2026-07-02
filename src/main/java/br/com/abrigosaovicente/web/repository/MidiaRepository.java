package br.com.abrigosaovicente.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.abrigosaovicente.web.model.Midia;

public interface MidiaRepository extends JpaRepository<Midia, Long> {
    List<Midia> finBySecaoAndAtivoTrue(String secao);
}
