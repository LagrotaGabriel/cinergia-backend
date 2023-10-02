package br.com.backend.modules.plano.repository;

import br.com.backend.modules.plano.models.entity.PlanoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanoRepository extends JpaRepository<PlanoEntity, Long> {

    @Query("SELECT SUM(p) FROM PlanoEntity p WHERE p.idEmpresaResponsavel = ?1 and p.statusPlano = 'INATIVO'")
    Integer somaQtdAssinaturasInativas(Long idEmpresa);

    @Query("SELECT SUM(p) FROM PlanoEntity p WHERE p.idEmpresaResponsavel = ?1 and p.statusPlano = 'ATIVO'")
    Integer somaQtdAssinaturasAtivas(Long idEmpresa);
    @Query("SELECT p FROM PlanoEntity p WHERE p.idEmpresaResponsavel = ?1")
    Page<PlanoEntity> buscaPorPlanos(Pageable pageable, Long id);

    @Query("SELECT p FROM PlanoEntity p WHERE " +
            "upper(p.descricao) LIKE ?1% and p.idEmpresaResponsavel = ?2")
    Page<PlanoEntity> buscaPorPlanosTypeAhead(Pageable pageable, String busca, Long id);

    @Query("SELECT p FROM PlanoEntity p WHERE " +
            "p.idClienteResponsavel = ?2 and p.idEmpresaResponsavel = ?1")
    Page<PlanoEntity> buscaPorPlanosDoCliente(Pageable pageable, Long id, Long idCliente);

    @Query("SELECT p FROM PlanoEntity p WHERE p.id=?1 and p.idEmpresaResponsavel = ?2")
    Optional<PlanoEntity> buscaPorId(Long idPlano, Long idEmpresa);

    Optional<PlanoEntity> findByIdAsaas(String asaasId);
}
