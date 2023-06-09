package br.com.backend.repositories.empresa;

import br.com.backend.models.entities.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<EmpresaEntity, Long> {

    @Query("SELECT e FROM EmpresaEntity e WHERE e.cpfCnpj = ?1")
    Optional<EmpresaEntity> buscaPorCpfCnpj(String cpfCnpj);

    @Query("SELECT e FROM EmpresaEntity e WHERE e.cpfCnpj = ?1 ")
    Optional<EmpresaEntity> buscaPorCpfCnpjIdentico(String cpfCnpj);

}
