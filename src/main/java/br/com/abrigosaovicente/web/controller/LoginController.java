package br.com.abrigosaovicente.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

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

        return "admin";
    }

    @GetMapping("/logout")
    public String deslogar(HttpSession session){

        session.invalidate();
        return "redirect:/login";
    }
}
