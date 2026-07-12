package br.com.abrigosaovicente.web.controller;

import br.com.abrigosaovicente.web.repository.MidiaRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.abrigosaovicente.web.model.Conteudo;
import br.com.abrigosaovicente.web.model.Midia;
import br.com.abrigosaovicente.web.repository.ConteudoRepository;
import br.com.abrigosaovicente.web.service.UploadService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class LoginController {
    private final MidiaRepository midiaRepository;
    private final ConteudoRepository conteudoRepository;
    private final UploadService uploadService;



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
        List<Midia> midias = midiaRepository.findAll();

        Map<String, String> textos = conteudos.stream()
            .collect(Collectors.toMap(conteudo -> conteudo.getChave(), conteudo -> conteudo.getTexto()));

        model.addAttribute("textos", textos);
        model.addAttribute("midias", midias);

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



    @PostMapping("/admin/galeria/upload")
    public String realizarUpload(@RequestParam MultipartFile imagem){

        if (!imagem.isEmpty()){
            String urlDaImagem = uploadService.salvarArquivo(imagem);

            Midia novaMidia = new Midia();
            novaMidia.setUrlCaminho(urlDaImagem);
            novaMidia.setSecao("galeria");

            midiaRepository.save(novaMidia);
        }

        return "redirect:/admin?sucesso=true";
    }

    @DeleteMapping("/admin/excluir/{id}")
    public String excluirImagem(@PathVariable Long id){

        Optional<Midia> midia = midiaRepository.findById(id);

        if (midia.isPresent()){
            Midia m = midia.get();

            uploadService.excluirArquivoFisico(m.getUrlCaminho());

            midiaRepository.deleteById(id);
        }

        return "redirect:/admin?sucesso=true";
    }



    @GetMapping("/logout")
    public String deslogar(HttpSession session){

        session.invalidate();
        return "redirect:/login";
    }
}
