package br.com.abrigosaovicente.web.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.abrigosaovicente.web.model.Conteudo;
import br.com.abrigosaovicente.web.repository.ConteudoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class LoginController {
    private final ConteudoRepository conteudoRepository;


    
    @GetMapping("/login")
    public String retornarPagina(){

        return "login";
    }



    @PostMapping("/login")
    public String logar(@RequestParam String senha, Model model, HttpSession session){
        
        if (!"abrigo123".equals(senha)){
            return "redirect:/login?erro=true";
        }

        session.setAttribute("usuarioLogado", true);
        return "redirect:/admin";
    }



    @GetMapping("/admin")
    public String exibirPainel(HttpSession session, Model model) {

        if (session.getAttribute("usuarioLogado") == null){
            return "redirect:/login";
        }

        List<Conteudo> conteudos = conteudoRepository.findAll();

        Map<String, String> textos = conteudos.stream()
            .collect(Collectors.toMap(conteudo -> conteudo.getChave(), conteudo -> conteudo.getTexto()));

        model.addAttribute("textos", textos);

        return "admin";
    }

    

    @PostMapping("/admin")
    public String editar(@RequestParam String chave, @RequestParam String texto, HttpSession session){

        if (session.getAttribute("usuarioLogado") == null){
            return "redirect:/login";
        }

        Conteudo conteudo = conteudoRepository.findById(chave)
            .orElseThrow(() -> new EntityNotFoundException("Conteúdo não encontrado no banco de dados"));

        conteudo.setTexto(texto);
        conteudoRepository.save(conteudo);

        return "redirect:/admin?sucesso=true";

    }



    @GetMapping("/logout")
    public String deslogar(HttpSession session){

        session.invalidate();
        return "redirect:/login";
    }
}
