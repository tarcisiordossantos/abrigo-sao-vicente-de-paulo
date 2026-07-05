package br.com.abrigosaovicente.web.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.abrigosaovicente.web.model.Conteudo;
import br.com.abrigosaovicente.web.model.Midia;
import br.com.abrigosaovicente.web.repository.ConteudoRepository;
import br.com.abrigosaovicente.web.repository.MidiaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class HomeController {
    private final ConteudoRepository conteudoRepository;
    private final MidiaRepository midiaRepository;

    @GetMapping("/")
    public String buscar(Model model){
        List<Conteudo> conteudos = conteudoRepository.findAll();
        List<Midia> imagensCarrossel = midiaRepository.findBySecaoAndAtivoTrue("carrossel");

        Map<String, String> textos = conteudos.stream()
            .collect(Collectors.toMap(conteudo -> conteudo.getChave(), conteudo -> conteudo.getTexto()));

        model.addAttribute("imagensCarrossel", imagensCarrossel );
        model.addAttribute("textos", textos);

        return "index";
    }


     
    @GetMapping("/sobre")
    public String buscarSobreNos(Model model){
        List<Conteudo> conteudos = conteudoRepository.findAll();
        List<Midia> imagensSobre = midiaRepository.findBySecaoAndAtivoTrue("carrossel");

        Map<String, String> textos = conteudos.stream()
            .collect(Collectors.toMap(conteudo -> conteudo.getChave(), conteudo -> conteudo.getTexto()));

        model.addAttribute("imagensSobre", imagensSobre);
        model.addAttribute("textos", textos);

        return "sobre";
    }



    @GetMapping("/ajudar")
    public String buscarComoAjudar(Model model){
        List<Conteudo> conteudos = conteudoRepository.findAll();
        //List<Midia> imagensSobre = midiaRepository.findBySecaoAndAtivoTrue("carrossel");

        Map<String, String> textos = conteudos.stream()
            .collect(Collectors.toMap(conteudo -> conteudo.getChave(), conteudo -> conteudo.getTexto()));

        //model.addAttribute("imagensSobre", imagensSobre);
        model.addAttribute("textos", textos);

        return "ajudar";
    }



    @GetMapping("/contato")
    public String buscarContato(Model model){
        List<Conteudo> conteudos = conteudoRepository.findAll();

        Map<String, String> textos = conteudos.stream()
            .collect(Collectors.toMap(conteudo -> conteudo.getChave(), conteudo -> conteudo.getTexto()));

        model.addAttribute("textos", textos);

        return "contato";
    }



    @GetMapping("/doar")
    public String buscarDoar(Model model){
        List<Conteudo> conteudos = conteudoRepository.findAll();
        //List<Midia> imagensSobre = midiaRepository.findBySecaoAndAtivoTrue("carrossel");

        Map<String, String> textos = conteudos.stream()
            .collect(Collectors.toMap(conteudo -> conteudo.getChave(), conteudo -> conteudo.getTexto()));

        //model.addAttribute("imagensSobre", imagensSobre);
        model.addAttribute("textos", textos);

        return "doar";
    }
    
}
