package br.com.backend.modules.cliente.services.utils;

import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.cliente.models.dto.request.ClienteRequest;
import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class ClienteUtils {

    ClienteUtils() {}

    public static String realizaTratamentoDeSenhaAtualizacao(ClienteEntity clienteEncontrado,
                                                             ClienteRequest clienteRequest) {
        if (clienteEncontrado.getAcessoSistema() == null)
            return new BCryptPasswordEncoder().encode(clienteRequest.getAcessoSistema().getSenha());
        else {
            if (clienteEncontrado.getAcessoSistema().getSenha().equals(clienteRequest.getAcessoSistema().getSenha())) {
                return clienteEncontrado.getAcessoSistema().getSenha();
            } else {
                return new BCryptPasswordEncoder().encode(clienteRequest.getAcessoSistema().getSenha());
            }
        }
    }

    public static void validaSeClienteEstaExcluido(ClienteEntity cliente, String mensagemCasoEstejaExcluido) {
        log.info("Método de validação de cliente excluído acessado");
        if (cliente.getExclusao() != null) {
            log.info("Cliente de id {}: Validação de cliente já excluído falhou. Não é possível realizar operações " +
                    "em um cliente que já foi excluído.", cliente.getUuid());
            throw new InvalidRequestException(mensagemCasoEstejaExcluido);
        }
        log.info("O cliente de id {} não está excluído", cliente.getUuid());
    }

}
