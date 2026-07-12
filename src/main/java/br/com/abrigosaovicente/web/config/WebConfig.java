package br.com.abrigosaovicente.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.diretorio}")
    private String diretorioDestino;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapeia o acesso web para a pasta física externa
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + diretorioDestino + "/");
    }
}
