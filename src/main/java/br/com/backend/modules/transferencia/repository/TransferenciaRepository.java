package br.com.backend.modules.transferencia.repository;

import br.com.backend.modules.transferencia.models.entity.TransferenciaEntity;
import br.com.backend.modules.transferencia.models.entity.id.TransferenciaId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransferenciaRepository extends JpaRepository<TransferenciaEntity, TransferenciaId> {

    @Query("SELECT t FROM TransferenciaEntity t " +
            "WHERE t.empresa.uuid = ?1")
    Page<TransferenciaEntity> buscaPorTransferenciasPaginadas(Pageable pageable, UUID uuid);

    Optional<TransferenciaEntity> findByAsaasId(String asaasId);

}
