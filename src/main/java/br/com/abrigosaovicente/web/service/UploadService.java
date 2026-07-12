package br.com.abrigosaovicente.web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class UploadService {

    @Value("${app.upload.diretorio}")
    private String diretorioDestino;

    public String salvarArquivo(MultipartFile arquivo) {
        try {
            // Cria a pasta física se ela não existir no servidor
            Path pasta = Paths.get(diretorioDestino);
            if (!Files.exists(pasta)) {
                Files.createDirectories(pasta);
            }

            // Gera um nome único universal para a foto não sobrescrever outra
            String extensao = arquivo.getOriginalFilename().substring(arquivo.getOriginalFilename().lastIndexOf("."));
            String nomeFinal = UUID.randomUUID().toString() + extensao;
            Path caminhoCompleto = pasta.resolve(nomeFinal);

            // Transfere os bytes diretamente da rede para o disco
            Files.copy(arquivo.getInputStream(), caminhoCompleto, StandardCopyOption.REPLACE_EXISTING);

            // Retorna apenas a URL relativa que o navegador usará para ler a foto
            return "/uploads/" + nomeFinal;

        } catch (IOException e) {
            throw new RuntimeException("Falha técnica ao salvar a imagem", e);
        }
    }



    public void excluirArquivoFisico(String urlCaminho) {
        if (urlCaminho != null && urlCaminho.startsWith("/uploads/")) {
            try {
                String nomeArquivo = urlCaminho.replace("/uploads/", "");
                
                Path pasta = Paths.get(diretorioDestino);
                Path caminhoCompletoDoArquivo = pasta.resolve(nomeArquivo);
                
                Files.deleteIfExists(caminhoCompletoDoArquivo);
                
            } catch (IOException e) {
                throw new RuntimeException("Falha técnica ao apagar o arquivo físico do servidor", e);
            }
        }
    }

}
