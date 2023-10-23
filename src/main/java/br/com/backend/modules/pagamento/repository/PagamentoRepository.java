package br.com.backend.modules.pagamento.repository;

import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.pagamento.models.entity.id.PagamentoId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PagamentoRepository extends JpaRepository<PagamentoEntity, PagamentoId> {

    @Query("SELECT p FROM PagamentoEntity p WHERE " +
            "p.empresa.uuid = ?1 and p.plano.uuid = ?2")
    Page<PagamentoEntity> buscaPorPagamentosDoPlano(Pageable pageable, UUID uuidEmpresa, UUID uuidPlano);

    @Query("SELECT p FROM PagamentoEntity p WHERE " +
            "p.empresa.uuid = ?1 and p.plano.cliente.uuid = ?2")
    Page<PagamentoEntity> buscaPorPagamentosDoCliente(Pageable pageable, UUID uuidEmpresa, UUID uuidCliente);

    @Query("SELECT p FROM PagamentoEntity p WHERE p.empresa.uuid = ?1 and p.statusPagamento = 'APROVADO'")
    Page<PagamentoEntity> buscaPorPagamentosRealizados(Pageable pageable, UUID uuidEmpresa);

    @Query("SELECT p FROM PagamentoEntity p " +
            "WHERE p.empresa.uuid = ?1 " +
            "AND (?2 IS NULL OR p.descricao LIKE ?2%)")
    Page<PagamentoEntity> buscaPaginadaPorPagamentos(Pageable pageable,
                                                     UUID uuidEmpresa,
                                                     String busca);

    @Query("SELECT p FROM PagamentoEntity p WHERE p.empresa.uuid = ?1")
    List<PagamentoEntity> buscaTodosPagamentosEmpresa(UUID uuidEmpresa);

    Optional<PagamentoEntity> findByAsaasId(String asaasId);

    @Query("SELECT COALESCE(SUM(p.valorBruto), 0) FROM PagamentoEntity p " +
            "WHERE p.plano.uuid = ?1 " +
            "AND p.plano.statusPlano <> 'REMOVIDO' " +
            "AND p.statusPagamento <> 'CANCELADO'")
    Double calculaTotalCobrancasDoPlano(UUID uuidPlano);

    @Query("SELECT COALESCE(SUM(p.valorBruto), 0) FROM PagamentoEntity p " +
            "WHERE p.plano.uuid = ?1 " +
            "AND p.plano.statusPlano <> 'REMOVIDO' " +
            "AND p.statusPagamento = 'PENDENTE'")
    Double calculaTotalCobrancasPendentesDoPlano(UUID uuidPlano);

    @Query("SELECT COALESCE(SUM(p.valorBruto), 0) FROM PagamentoEntity p " +
            "WHERE p.plano.uuid = ?1 " +
            "AND p.plano.statusPlano <> 'REMOVIDO' " +
            "AND p.statusPagamento = 'APROVADO'")
    Double calculaTotalCobrancasAprovadasDoPlano(UUID uuidPlano);

    @Query("SELECT COALESCE(SUM(p.valorBruto), 0) FROM PagamentoEntity p " +
            "WHERE p.plano.uuid = ?1 " +
            "AND p.plano.statusPlano <> 'REMOVIDO' " +
            "AND p.statusPagamento = 'ATRASADO'")
    Double calculaTotalCobrancasAtrasadasDoPlano(UUID uuidPlano);

    @Query("SELECT COALESCE(COUNT(pgt), 0) FROM PagamentoEntity pgt " +
            "WHERE pgt.plano.uuid = ?1 " +
            "AND pgt.plano.statusPlano <> 'REMOVIDO' " +
            "AND pgt.statusPagamento <> 'CANCELADO'")
    Integer somaQuantidadeDeCobrancasDoPlano(UUID uuidPlano);

    @Query("SELECT COALESCE(COUNT(p), 0) FROM PagamentoEntity p " +
            "WHERE p.plano.uuid = ?1 " +
            "AND p.plano.statusPlano <> 'REMOVIDO' " +
            "AND p.statusPagamento = 'ATRASADO'")
    Integer somaQuantidadeDeCobrancasAtrasadasDoPlano(UUID uuidPlano);

    @Query("SELECT pgt FROM PagamentoEntity pgt " +
            "WHERE pgt.dataVencimento < ?1 " +
            "AND (pgt.statusPagamento != 'ATRASADO' AND pgt.statusPagamento != 'CANCELADO')")
    List<PagamentoEntity> buscaPagamentosAtrasadosComStatusDiferenteDeAtrasadoECancelado(String hoje);
}
