package br.com.backend.modules.notificacao.repository;

import br.com.backend.modules.notificacao.models.entity.NotificacaoEntity;
import br.com.backend.modules.notificacao.models.entity.id.NotificacaoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificacaoRepository extends JpaRepository<NotificacaoEntity, NotificacaoId> {

    @Query("SELECT n FROM NotificacaoEntity n " +
            "WHERE n.empresa.uuid = ?1 " +
            "AND n.lida = FALSE")
    List<NotificacaoEntity> buscaNotificacoesDaEmpresa(UUID uuidEmpresa);

}
