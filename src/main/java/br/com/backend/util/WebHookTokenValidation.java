package br.com.backend.util;

import br.com.backend.exceptions.custom.UnauthorizedAccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WebHookTokenValidation {

    //TODO TROCAR ESTA LÓGICA PELO FILTER DO SPRING BOOT
    @Value("${TOKEN_WEBHOOK_ASAAS}")
    String tokenWebhook;

    public void realizaValidacaoToken(String token) {
        log.debug("Método de validação de token acessado");

        log.debug("Iniciando validação de token...");
        if (token == null || token.equals("")) {
            log.warn("O token recebido é nulo ou vazio: {}", token);
            throw new UnauthorizedAccessException("Nenhum token de acesso foi recebido");
        }
        if (!token.equals(tokenWebhook)) {
            log.warn("O token recebido não é compatível com o esperado: {}", token);
            throw new UnauthorizedAccessException("O token de acesso recebido está incorreto");
        }
    }
}
