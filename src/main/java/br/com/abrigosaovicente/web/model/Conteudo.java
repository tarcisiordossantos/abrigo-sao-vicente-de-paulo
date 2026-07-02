package br.com.abrigosaovicente.web.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "conteudo")
public class Conteudo {

    @Id
    private String chave;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "O texto não pode estar em branco")   
    private String texto;

}
