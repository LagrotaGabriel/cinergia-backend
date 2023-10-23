package br.com.backend.modules.notificacao.services;

import br.com.backend.modules.notificacao.models.dto.NotificacaoResponse;

import java.util.List;
import java.util.UUID;

public interface NotificacaoService {

    List<NotificacaoResponse> implementaBuscaDeNotificacoesEmpresa(UUID uuidEmpresa);

    void realizaSetagemDasNotificacoesDaEmpresaComoLidas(UUID uuidEmpresa);

}
