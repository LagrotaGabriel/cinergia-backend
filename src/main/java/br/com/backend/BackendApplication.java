package br.com.backend;

import br.com.backend.config.local.TunellerConfig;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@EnableAsync
@EnableScheduling
@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Gerenciamento de assinaturas",
        version = "1.0",
        description = "Este projeto tem como objetivo fornecer um ecosistema completo voltado para a gestão de assinaturas")
)
public class BackendApplication implements CommandLineRunner {

    @Value("${spring.profiles.active}")
    String activeProfile;

    @Autowired
    TunellerConfig tunellerConfig;

    // TODO FIND ALL PARA GERAR RELATÓRIO TALVEZ NÃO SEJA UMA BOA PRÁTICA. PESQUISAR ALGUMA TÉCNICA PARA MELHORAR O DESEMPENHO
    // TODO CRIAR MÉTODO GLOBAL DE VALIDAÇÃO DAS RESPOSTAS DOS PROXY DO PROJETO
    // TODO O KAFKA CAIRIA COMO UMA LUVA NO PROJETO. VERIFICAR CUSTOS DE DINHEIRO E TEMPO DE APRENDIZADO
    // TODO INVOCAR SERVIÇO DE E-MAIL DE BOAS VINDAS PARA CRIAÇÃO DE EMPRESA
    // TODO INVOCAR SERVIÇO DE E-MAIL DE BOAS VINDAS PARA CRIAÇÃO DE CLIENTE
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        if (activeProfile.equals("dev")) tunellerConfig.iniciaExposicaoDeRedeLocalParaIntegradoras();
    }

}
