package com.mballem.demo_park_api.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

// Especificando que esta é uma classe de configuração
@Configuration
public class SpringTimeZoneConfig {

    //Faz com que o metodo seja executado automaticamente, logo depois que a classe é inicializada
    @PostConstruct
    // Configurando o time zone usado na API
    public void timeZoneConfig(){
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }

}
