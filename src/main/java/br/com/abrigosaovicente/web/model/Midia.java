package br.com.abrigosaovicente.web.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "midia")
public class Midia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "A seção não pode estar em branco")
    private String secao;

    @Column(name = "url_caminho")
    @NotBlank(message = "O URL não pode estar em branco")
    private String urlCaminho;

    @Column(nullable = false)
    private Boolean ativo = true; 

}
