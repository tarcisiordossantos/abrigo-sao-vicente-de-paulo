package br.com.abrigosaovicente.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.abrigosaovicente.web.model.Conteudo;

public interface ConteudoRepository extends JpaRepository<Conteudo, String> {

}
