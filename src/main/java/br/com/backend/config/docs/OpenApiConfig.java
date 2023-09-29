package br.com.backend.config.docs;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${default.api.path}")
    private String defaultApiPath;

    @Bean
    public GroupedOpenApi clienteGroup() {
        String[] paths = {defaultApiPath + "/cliente/**"};
        return GroupedOpenApi.builder()
                .group("clientes")
                .displayName("Clientes")
                .pathsToExclude(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi empresaGroup() {
        String[] paths = {defaultApiPath + "/empresa/**"};
        return GroupedOpenApi.builder()
                .group("empresas")
                .displayName("Empresas")
                .pathsToExclude(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi notificacaoGroup() {
        String[] paths = {defaultApiPath + "/notificacao/**"};
        return GroupedOpenApi.builder()
                .group("notificacoes")
                .displayName("Notificacoes")
                .pathsToExclude(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi pagamentoGroup() {
        String[] paths = {defaultApiPath + "/pagamento/**"};
        return GroupedOpenApi.builder()
                .group("pagamentos")
                .displayName("Pagamentos")
                .pathsToExclude(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi planoGroup() {
        String[] paths = {defaultApiPath + "/plano/**"};
        return GroupedOpenApi.builder()
                .group("planos")
                .displayName("Planos")
                .pathsToExclude(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi transferenciaGroup() {
        String[] paths = {defaultApiPath + "/transferencia/**"};
        return GroupedOpenApi.builder()
                .group("transferencias")
                .displayName("Transferencias")
                .pathsToExclude(paths)
                .build();
    }

}
