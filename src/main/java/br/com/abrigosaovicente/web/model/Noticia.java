package br.com.abrigosaovicente.web.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "noticia")
public class Noticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título não pode estar em branco")
    private String titulo;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "O texto da notícia não pode estar em branco")
    private String texto;

    @Column(name = "url_imagem")
    private String urlImagem;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();
}