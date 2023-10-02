package br.com.backend.modules.notificacao.services;

import br.com.backend.modules.notificacao.models.dto.NotificacaoResponse;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.notificacao.models.entity.NotificacaoEntity;
import br.com.backend.modules.notificacao.repository.NotificacaoRepository;
import br.com.backend.modules.notificacao.services.adapter.NotificacaoTypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class NotificacaoService {

    @Autowired
    NotificacaoRepository notificacaoRepository;

    @Autowired
    NotificacaoTypeConverter notificacaoTypeConverter;

    public List<NotificacaoResponse> implementaBuscaDeNotificacoesEmpresa(EmpresaEntity empresa) {
        log.debug("Método de implementação de busca de notificações da empresa acessado");

        log.debug("Iniciando query de busca pelas notificações no repositório...");
        List<NotificacaoEntity> notificacaoEntities =
                notificacaoRepository.buscaNotificacoesDaEmpresa(empresa.getId());

        log.debug("Iniciando acesso ao método de conversão de NotificacaoEntity para NotificacaoResponse...");
        List<NotificacaoResponse> notificacaoResponses =
                notificacaoTypeConverter.converteNotificacaoEntityParaNotificacaoResponse(notificacaoEntities);

        log.info("Consulta das notificações da empresa realizada com sucesso");
        return notificacaoResponses;
    }

    @Transactional
    public void realizaSetagemDasNotificacoesDaEmpresaComoLidas(EmpresaEntity empresa) {
        log.debug("Método de setagem de notificações da empresa como lidas acessado");

        log.debug("Iniciando query de busca pelas notificações no repositório...");
        List<NotificacaoEntity> notificacaoEntities =
                notificacaoRepository.buscaNotificacoesDaEmpresa(empresa.getId());

        log.debug("Setando todas as notificações como lidas...");
        notificacaoEntities.forEach(notificacao -> notificacao.setLida(true));

        log.debug("Persistindo notificações atualizadas no banco de dados...");
        notificacaoRepository.saveAll(notificacaoEntities);

        log.info("Atualização das notificações realizada com sucesso");
    }

}
