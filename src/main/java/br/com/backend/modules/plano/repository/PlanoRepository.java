package br.com.backend.modules.plano.repository;

import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.plano.models.entity.id.PlanoId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanoRepository extends JpaRepository<PlanoEntity, PlanoId> {

    Optional<PlanoEntity> findByAsaasId(String asaasId);

    @Query("SELECT p FROM PlanoEntity p " +
            "WHERE p.empresa.uuid = ?1 " +
            "AND (?2 IS NULL OR p.descricao LIKE ?2%)")
    Page<PlanoEntity> buscaPaginadaPorPlanos(Pageable pageable,
                                             UUID uuidEmpresa,
                                             String busca);

    @Query("SELECT p FROM PlanoEntity p " +
            "WHERE p.cliente.uuid = ?2 " +
            "AND p.empresa.uuid = ?1")
    Page<PlanoEntity> buscaPaginadaPorPlanosDoCliente(Pageable pageable, UUID uuidEmpresa, UUID uuidCliente);

    @Query("SELECT p FROM PlanoEntity p " +
            "WHERE p.cliente.uuid = ?1")
    List<PlanoEntity> buscaPorPlanosDoCliente(UUID uuidCliente);

    @Query("SELECT p FROM PlanoEntity p " +
            "WHERE p.dataAgendamentoRemocao <= ?1 " +
            "AND p.statusPlano != 'REMOVIDO'")
    List<PlanoEntity> buscaPlanosComAgendamentosDeRemocaoPendentes(String dataAgendamentoRemocao);

    @Query("SELECT COALESCE(COUNT(p), 0) FROM PlanoEntity p " +
            "WHERE p.empresa.uuid = ?1 " +
            "AND p.statusPlano = 'ATIVO'")
    Integer somaQuantidadeDeAssinaturasAtivasDaEmpresa(UUID uuidEmpresa);

    @Query("SELECT COALESCE(COUNT(p), 0) FROM PlanoEntity p " +
            "WHERE p.empresa.uuid = ?1 " +
            "AND p.statusPlano = 'INATIVO'")
    Integer somaQuantidadeDeAssinaturasInativasDaEmpresa(UUID uuidEmpresa);
}
