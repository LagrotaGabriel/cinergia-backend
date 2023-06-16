package br.com.backend.repositories.plano;

import br.com.backend.models.entities.PlanoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanoRepository extends JpaRepository<PlanoEntity, Long> {

    @Query("SELECT p FROM PlanoEntity p WHERE p.idEmpresaResponsavel = ?1")
    Page<PlanoEntity> buscaPorPlanos(Pageable pageable, Long id);

    @Query("SELECT p FROM PlanoEntity p WHERE " +
            "upper(p.descricao) LIKE ?1% and p.idEmpresaResponsavel = ?2")
    Page<PlanoEntity> buscaPorPlanosTypeAhead(Pageable pageable, String busca, Long id);

}
