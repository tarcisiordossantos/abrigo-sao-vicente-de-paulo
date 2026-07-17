package br.com.abrigosaovicente.web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.abrigosaovicente.web.model.Administrador;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByEmailAndAtivoTrue(String email);
}
