package br.com.abrigosaovicente.web.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContatoForm(
    @NotBlank
    String nome,
    @NotBlank
    @Email
    String email,
    String telefone,
    @NotBlank
    String assunto,
    @NotBlank
    String mensagem
) {

}
