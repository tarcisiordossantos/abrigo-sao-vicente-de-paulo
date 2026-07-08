package br.com.abrigosaovicente.web.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.abrigosaovicente.web.controller.dto.ContatoForm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public void enviarEmail(ContatoForm dto){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo("abrigogestor@hotmail.com");
        mailMessage.setReplyTo(dto.email());
        mailMessage.setSubject("Site Abrigo - " + dto.assunto());
        mailMessage.setText("Nome: " + dto.nome() 
                        + "\nTelefone: " + dto.telefone() 
                        + "\n\nMensagem:\n" + dto.mensagem());
        
        mailSender.send(mailMessage);
    }
}
