package br.com.backend.modules.cliente.repository;

import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.globals.models.imagem.entity.ImagemEntity;
import br.com.backend.modules.cliente.models.entity.id.ClienteId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, ClienteId> {

    boolean existsByEmpresaUuidAndCpfCnpj(UUID uuidEmpresaSessao, String cpfCnpj);
    boolean existsByEmpresaUuidAndEmail(UUID uuidEmpresaSessao, String email);

    @Query("SELECT c FROM ClienteEntity c WHERE c.empresa.uuid = ?1 and c.exclusao IS NULL")
    Page<ClienteEntity> buscaPorClientes(Pageable pageable, UUID uuidEmpresaSessao);

    @Query("SELECT c FROM ClienteEntity c WHERE " +
            "c.empresa.uuid = ?1 and upper(c.nome) LIKE ?2% and c.exclusao IS NULL " +
            "or c.empresa.uuid = ?1 and upper(c.email) LIKE ?2% and c.exclusao IS NULL " +
            "or c.empresa.uuid = ?1 and upper(c.cpfCnpj) LIKE ?2% and c.exclusao IS NULL")
    Page<ClienteEntity> buscaPorClientesTypeAhead(Pageable pageable, UUID uuidEmpresaSessao, String busca);

    @Query("SELECT c FROM ClienteEntity c WHERE c.empresa.uuid = ?1 and c.uuid=?2 and c.exclusao IS NULL")
    Optional<ClienteEntity> buscaPorId(UUID uuidEmpresaSessao, UUID uuidCliente);

    @Query("SELECT c FROM ClienteEntity c WHERE c.empresa.uuid = ?1 and c.exclusao IS NULL")
    List<ClienteEntity> buscaTodos(UUID uuidEmpresaSessao);

    @Query("SELECT c.fotoPerfil FROM ClienteEntity c WHERE c.empresa.uuid = ?1 and c.uuid=?2 and c.exclusao IS NULL")
    Optional<ImagemEntity> buscaImagemPerfilPorId(UUID uuidEmpresaSessao, UUID uuidCliente);

}
