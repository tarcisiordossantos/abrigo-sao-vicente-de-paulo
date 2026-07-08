package br.com.abrigosaovicente.web.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.abrigosaovicente.web.controller.dto.ContatoForm;
import br.com.abrigosaovicente.web.model.Conteudo;
import br.com.abrigosaovicente.web.model.Midia;
import br.com.abrigosaovicente.web.repository.ConteudoRepository;
import br.com.abrigosaovicente.web.repository.MidiaRepository;
import br.com.abrigosaovicente.web.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class HomeController {
    private final ConteudoRepository conteudoRepository;
    private final MidiaRepository midiaRepository;
    private final EmailService emailService;

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
        model.addAttribute("contatoForm", new ContatoForm("", "", "", "", ""));

        return "contato";
    }

    @PostMapping("/contato")
    public String enviarEmail(@Valid @ModelAttribute ContatoForm dto, BindingResult result, Model model){
        
        if(result.hasErrors()) {

            List<Conteudo> conteudos = conteudoRepository.findAll();
            Map<String, String> textos = conteudos.stream()
                .collect(Collectors.toMap(conteudo -> conteudo.getChave(), conteudo -> conteudo.getTexto()));
            model.addAttribute("textos", textos);

            return "contato";
        }

        emailService.enviarEmail(dto);
        
        return "redirect:/contato?sucesso=true";
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
