package br.com.abrigosaovicente.web.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.abrigosaovicente.web.controller.dto.ContatoForm;
import br.com.abrigosaovicente.web.model.Conteudo;
import br.com.abrigosaovicente.web.model.Midia;
import br.com.abrigosaovicente.web.model.Noticia;
import br.com.abrigosaovicente.web.repository.ConteudoRepository;
import br.com.abrigosaovicente.web.repository.MidiaRepository;
import br.com.abrigosaovicente.web.repository.NoticiaRepository;
import br.com.abrigosaovicente.web.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class HomeController {
    private final ConteudoRepository conteudoRepository;
    private final MidiaRepository midiaRepository;
    private final EmailService emailService;
    private final NoticiaRepository noticiaRepository;

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
        List<Midia> galeria = midiaRepository.findBySecaoAndAtivoTrue("galeria");
        Midia fotoQuemSomos = midiaRepository.findFirstBySecao("quem-somos").orElse(null);
        Midia fotoNossaHistoria = midiaRepository.findFirstBySecao("nossa-historia").orElse(null);

        Map<String, String> textos = conteudos.stream()
            .collect(Collectors.toMap(conteudo -> conteudo.getChave(), conteudo -> conteudo.getTexto()));

        model.addAttribute("galeria", galeria);
        model.addAttribute("textos", textos);
        model.addAttribute("fotoQuemSomos", fotoQuemSomos);
        model.addAttribute("fotoNossaHistoria", fotoNossaHistoria);

        return "sobre";
    }



    @GetMapping("/ajudar")
    public String buscarComoAjudar(Model model){
        List<Conteudo> conteudos = conteudoRepository.findAll();
        Midia fotoDoacaoAlimentos = midiaRepository.findFirstBySecao("doacao-alimentos").orElse(null);
        Midia fotoRecursosFinanceiros = midiaRepository.findFirstBySecao("recursos-financeiros").orElse(null);
        Midia fotoTrabalhoVoluntario = midiaRepository.findFirstBySecao("trabalho-voluntario").orElse(null);

        Map<String, String> textos = conteudos.stream()
            .collect(Collectors.toMap(conteudo -> conteudo.getChave(), conteudo -> conteudo.getTexto()));

        model.addAttribute("textos", textos);
        model.addAttribute("fotoDoacaoAlimentos", fotoDoacaoAlimentos);
        model.addAttribute("fotoRecursosFinanceiros", fotoRecursosFinanceiros);
        model.addAttribute("fotoTrabalhoVoluntario", fotoTrabalhoVoluntario);

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
        Midia fotoDoarAgora = midiaRepository.findFirstBySecao("doar-agora").orElse(null);

        Map<String, String> textos = conteudos.stream()
            .collect(Collectors.toMap(conteudo -> conteudo.getChave(), conteudo -> conteudo.getTexto()));

        model.addAttribute("textos", textos);
        model.addAttribute("fotoDoarAgora", fotoDoarAgora);

        return "doar";
    }



    @GetMapping("/noticias")
    public String listarNoticias(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("dataCriacao").descending());
        
        Page<Noticia> paginaNoticias = noticiaRepository.findAll(pageable);

        model.addAttribute("noticias", paginaNoticias.getContent());
        model.addAttribute("paginaAtual", page);
        model.addAttribute("totalPaginas", paginaNoticias.getTotalPages());

        List<Conteudo> conteudos = conteudoRepository.findAll();
        Map<String, String> textos = conteudos.stream()
            .collect(Collectors.toMap(conteudo -> conteudo.getChave(), conteudo -> conteudo.getTexto()));
        model.addAttribute("textos", textos);

        model.addAttribute("textos", textos);

        return "noticias";
    }
    
}
