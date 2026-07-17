package br.com.abrigosaovicente.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.abrigosaovicente.web.model.Noticia;

public interface NoticiaRepository extends JpaRepository<Noticia, Long> {

}
