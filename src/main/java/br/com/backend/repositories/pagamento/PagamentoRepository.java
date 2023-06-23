package br.com.backend.repositories.pagamento;

import br.com.backend.models.entities.PagamentoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<PagamentoEntity, Long> {

    @Query("SELECT p FROM PagamentoEntity p WHERE " +
            "p.idEmpresaResponsavel = ?1 and p.idPlanoResponsavel = ?2")
    Page<PagamentoEntity> buscaPorPagamentosDoPlano(Pageable pageable, Long idEmpresa, Long idPlano);

    @Query("SELECT p FROM PagamentoEntity p WHERE " +
            "p.idEmpresaResponsavel = ?1 and p.idClienteResponsavel = ?2")
    Page<PagamentoEntity> buscaPorPagamentosDoCliente(Pageable pageable, Long idEmpresa, Long idCliente);

    @Query("SELECT p FROM PagamentoEntity p WHERE p.idEmpresaResponsavel = ?1")
    Page<PagamentoEntity> buscaPorPagamentos(Pageable pageable, Long id);

    @Query("SELECT p FROM PagamentoEntity p WHERE upper(p.descricao) LIKE ?1% and p.idEmpresaResponsavel = ?2")
    Page<PagamentoEntity> buscaPorPagamentosTypeAhead(Pageable pageable, String busca, Long id);

    Optional<PagamentoEntity> findByIdAsaas(String asaasId);
}
