package br.com.backend.repositories.transferencia;

import br.com.backend.models.entities.TransferenciaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferenciaRepository extends JpaRepository<TransferenciaEntity, Long> {

    @Query("SELECT t FROM TransferenciaEntity t WHERE t.idEmpresaResponsavel = ?1")
    Page<TransferenciaEntity> buscaPorTransferencias(Pageable pageable, Long id);

}
