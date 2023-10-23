package br.com.backend.modules.plano.services.validator.date;

import br.com.backend.exceptions.custom.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class DateValidator {

    /**
     * Valida atributo dataInicio
     *
     * @param dataInicio - Para ser válido, o atributo dataInicio deve ser igual ou superior à data atual
     */
    public void deveValidarAtributoDataInicio(String dataInicio) {
        log.info("Iniciando validação do atributo dataInicio recebido: {}...", dataInicio);
        LocalDate dataInicioConvertido = LocalDate.parse(dataInicio, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (dataInicioConvertido.isBefore(LocalDate.now())) {
            log.error("Ocorreu um erro na validação do atributo dataInicio: a data é inferior à data atual");
            throw new InvalidRequestException("A data de início do plano não pode ser inferior à data atual");
        }

        log.info("Atributo dataInicio validado com sucesso");
    }

}
