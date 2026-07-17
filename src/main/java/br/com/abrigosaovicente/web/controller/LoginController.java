package br.com.abrigosaovicente.web.controller;

import br.com.abrigosaovicente.web.repository.MidiaRepository;
import br.com.abrigosaovicente.web.repository.NoticiaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.abrigosaovicente.web.model.Administrador;
import br.com.abrigosaovicente.web.model.Conteudo;
import br.com.abrigosaovicente.web.model.Midia;
import br.com.abrigosaovicente.web.model.Noticia;
import br.com.abrigosaovicente.web.repository.AdministradorRepository;
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
    private final NoticiaRepository noticiaRepository;
    private final AdministradorRepository administradorRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();



    @GetMapping("/login")
    public String retornarPagina(){

        return "login";
    }



    @PostMapping("/login")
    public String logar(@RequestParam String email, @RequestParam String senha, Model model, HttpSession session){
        
        Optional<Administrador> adminOptional = administradorRepository.findByEmailAndAtivoTrue(email);

        if (adminOptional.isEmpty()) {
            return "redirect:/login?erro=true";
        }
        
        Administrador admin = adminOptional.get();

        if (!passwordEncoder.matches(senha, admin.getSenha())) {
            return "redirect:/login?erro=true";
        }

        session.setAttribute("emailUsuario", admin.getEmail());
        session.setAttribute("usuarioLogado", true);
        return "redirect:/admin";
    }



    @GetMapping("/admin")
    public String exibirPainel(HttpSession session, Model model) {

        if (session.getAttribute("usuarioLogado") == null){
            return "redirect:/login";
        }

        List<Conteudo> conteudos = conteudoRepository.findAll();
        List<Midia> midiasCarrossel = midiaRepository.findBySecaoAndAtivoTrue("carrossel");
        List<Midia> midiasGaleria = midiaRepository.findBySecaoAndAtivoTrue("galeria");
        Midia fotoQuemSomos = midiaRepository.findFirstBySecao("quem-somos").orElse(null);
        Midia fotoNossaHistoria = midiaRepository.findFirstBySecao("nossa-historia").orElse(null);
        Midia fotoDoacaoAlimentos = midiaRepository.findFirstBySecao("doacao-alimentos").orElse(null);
        Midia fotoRecursosFinanceiros = midiaRepository.findFirstBySecao("recursos-financeiros").orElse(null);
        Midia fotoTrabalhoVoluntario = midiaRepository.findFirstBySecao("trabalho-voluntario").orElse(null);
        Midia fotoDoarAgora = midiaRepository.findFirstBySecao("doar-agora").orElse(null);
        List<Noticia> noticias = noticiaRepository.findAll();

        Map<String, String> textos = conteudos.stream()
            .collect(Collectors.toMap(conteudo -> conteudo.getChave(), conteudo -> conteudo.getTexto()));

        model.addAttribute("textos", textos);
        model.addAttribute("midiasCarrossel", midiasCarrossel);
        model.addAttribute("midiasGaleria", midiasGaleria);
        model.addAttribute("fotoQuemSomos", fotoQuemSomos);
        model.addAttribute("fotoNossaHistoria", fotoNossaHistoria);
        model.addAttribute("fotoDoacaoAlimentos", fotoDoacaoAlimentos);
        model.addAttribute("fotoRecursosFinanceiros", fotoRecursosFinanceiros);
        model.addAttribute("fotoTrabalhoVoluntario", fotoTrabalhoVoluntario);
        model.addAttribute("fotoDoarAgora", fotoDoarAgora);
        model.addAttribute("noticias", noticias);

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



    @PostMapping("/admin/carrossel/upload")
    public String realizarUploadCarrossel(@RequestParam MultipartFile imagem, HttpSession session){

        if (session.getAttribute("usuarioLogado") == null){
            return "redirect:/login";
        }

        if (!imagem.isEmpty()){
            String urlDaImagem = uploadService.salvarArquivo(imagem);

            Midia novaMidia = new Midia();
            novaMidia.setUrlCaminho(urlDaImagem);
            novaMidia.setSecao("carrossel");

            midiaRepository.save(novaMidia);
        }

        return "redirect:/admin?sucesso=true";
    }
    
    @PostMapping("/admin/galeria/upload")
    public String realizarUploadGaleria(@RequestParam MultipartFile imagem, HttpSession session){

        if (session.getAttribute("usuarioLogado") == null){
            return "redirect:/login";
        }

        if (!imagem.isEmpty()){
            String urlDaImagem = uploadService.salvarArquivo(imagem);

            Midia novaMidia = new Midia();
            novaMidia.setUrlCaminho(urlDaImagem);
            novaMidia.setSecao("galeria");

            midiaRepository.save(novaMidia);
        }

        return "redirect:/admin?sucesso=true";
    }

    @PostMapping("/admin/substituir")
    public String substituirFotoUnica(@RequestParam String secao, @RequestParam MultipartFile imagem, HttpSession session) {
        
        if (session.getAttribute("usuarioLogado") == null){
            return "redirect:/login";
        }

        if (!imagem.isEmpty()) {
            Optional<Midia> midiaExistente = midiaRepository.findFirstBySecao(secao);
            
            String caminhoNovoArquivo = uploadService.salvarArquivo(imagem);
            
            if (midiaExistente.isPresent()) {
                Midia midiaAntiga = midiaExistente.get();
                
                uploadService.excluirArquivoFisico(midiaAntiga.getUrlCaminho());
                
                midiaAntiga.setUrlCaminho(caminhoNovoArquivo);
                midiaRepository.save(midiaAntiga);
                
            } else {
                Midia novaMidia = new Midia();
                novaMidia.setUrlCaminho(caminhoNovoArquivo);
                novaMidia.setSecao(secao);
                midiaRepository.save(novaMidia);
            }
        }
        
        return "redirect:/admin?sucesso=true";
    }

    @DeleteMapping("/admin/excluir/{id}")
    public String excluirImagem(@PathVariable Long id, HttpSession session){

        if (session.getAttribute("usuarioLogado") == null){
            return "redirect:/login";
        }


        Optional<Midia> midia = midiaRepository.findById(id);

        if (midia.isPresent()){
            Midia m = midia.get();

            uploadService.excluirArquivoFisico(m.getUrlCaminho());

            midiaRepository.deleteById(id);
        }

        return "redirect:/admin?sucesso=true";
    }



    @PostMapping("/admin/noticias/salvar")
    public String salvarNoticia(@RequestParam String titulo, 
                                @RequestParam String texto, 
                                @RequestParam MultipartFile imagem, 
                                HttpSession session) {
        
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        Noticia noticia = new Noticia();
        noticia.setTitulo(titulo);
        noticia.setTexto(texto);

        if (!imagem.isEmpty()) {
            String urlImagem = uploadService.salvarArquivo(imagem);
            noticia.setUrlImagem(urlImagem);
        }

        noticiaRepository.save(noticia);

        return "redirect:/admin?sucesso=true";
    }



    @DeleteMapping("/admin/noticias/excluir/{id}")
    public String excluirNoticia(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null){
            return "redirect:/login";
        }

        Optional<Noticia> noticiaOptional = noticiaRepository.findById(id);

        if (noticiaOptional.isPresent()) {
            Noticia noticia = noticiaOptional.get();
            
            if (noticia.getUrlImagem() != null) {
                uploadService.excluirArquivoFisico(noticia.getUrlImagem());
            }
            
            noticiaRepository.deleteById(id);
        }

        return "redirect:/admin?sucesso=true";
    }



    @PostMapping("/admin/alterar-senha")
    public String alterarSenha(@RequestParam String senhaAtual, 
                                @RequestParam String novaSenha,
                                @RequestParam String confirmacao,  
                                HttpSession session, 
                                Model model) {
        
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        if (!confirmacao.equals(novaSenha)){
            return "redirect:/admin?erroConfirmacao=true";
        }

        String emailLogado = (String) session.getAttribute("emailUsuario");

        Administrador admin = administradorRepository.findByEmailAndAtivoTrue(emailLogado)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senhaAtual, admin.getSenha())) {
            return "redirect:/admin?erroSenha=true";
        }

        admin.setSenha(passwordEncoder.encode(novaSenha));
        administradorRepository.save(admin);

        return "redirect:/admin?sucessoSenha=true";
    }



    @GetMapping("/logout")
    public String deslogar(HttpSession session){

        session.invalidate();
        return "redirect:/login";
    }
}
