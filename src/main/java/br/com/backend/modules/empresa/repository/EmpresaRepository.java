package br.com.backend.modules.empresa.repository;

import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.empresa.repository.views.EmpresaSessaoView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpresaRepository extends JpaRepository<EmpresaEntity, UUID> {

    @Query("SELECT e FROM EmpresaEntity e WHERE e.cpfCnpj = ?1")
    Optional<EmpresaEntity> buscaPorCpfCnpj(String cpfCnpj);

    @Query("SELECT new br.com.backend.modules.empresa.repository.views.EmpresaSessaoView(" +
            "e.uuid, e.cpfCnpj, e.acessoSistema) FROM EmpresaEntity e WHERE e.cpfCnpj = ?1")
    Optional<EmpresaSessaoView> buscaEmpresaSessaoAtual(String cpfCnpj);

    boolean existsByCpfCnpj(String cpfCnpj);

    boolean existsByEmail(String email);

}
