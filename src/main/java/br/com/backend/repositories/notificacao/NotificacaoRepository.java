package br.com.backend.repositories.notificacao;

import br.com.backend.models.entities.NotificacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<NotificacaoEntity, Long> {

    @Query("SELECT n FROM NotificacaoEntity n WHERE n.idEmpresaResponsavel = ?1 and n.lida = FALSE")
    List<NotificacaoEntity> buscaNotificacoesDaEmpresa(Long idEmpresa);

}
