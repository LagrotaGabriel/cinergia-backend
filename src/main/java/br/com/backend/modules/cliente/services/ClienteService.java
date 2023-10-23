package br.com.backend.modules.cliente.services;

import br.com.backend.modules.cliente.models.dto.request.ClienteRequest;
import br.com.backend.modules.cliente.models.dto.response.ClienteResponse;
import br.com.backend.modules.cliente.models.dto.response.page.ClientePageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface ClienteService {

    @Transactional
    ClienteResponse criaNovoCliente(UUID uuidEmpresaSessao,
                                    ClienteRequest clienteRequest);

    ClientePageResponse realizaBuscaPaginadaPorClientes(UUID uuidEmpresaSessao,
                                                        Pageable pageable,
                                                        String campoBusca);

    ClienteResponse realizaBuscaDeClientePorId(UUID uuidEmpresaSessao,
                                               UUID uuidCliente);

    byte[] obtemImagemPerfilCliente(UUID uuidEmpresaSessao,
                                    UUID uuidCliente);

    ClienteResponse atualizaCliente(UUID uuidEmpresaSessao,
                                    UUID uuidCliente,
                                    ClienteRequest clienteRequest);

    ClienteResponse atualizaImagemPerfilCliente(UUID uuidEmpresaSessao,
                                                UUID uuidCliente,
                                                MultipartFile fotoPerfil) throws IOException;

    @Transactional
    ClienteResponse removeCliente(UUID uuidEmpresaSessao,
                                  UUID uuidCliente);
}
