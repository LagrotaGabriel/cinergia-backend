package br.com.backend.modules.cliente.repository.impl;

import br.com.backend.exceptions.custom.ObjectNotFoundException;
import br.com.backend.globals.models.imagem.entity.ImagemEntity;
import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.modules.cliente.models.entity.id.ClienteId;
import br.com.backend.modules.cliente.repository.ClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClienteRepositoryImpl {

    @Autowired
    ClienteRepository repository;

    public ClienteEntity implementaPersistencia(ClienteEntity cliente) {
        log.info("Método de serviço que implementa persistência do cliente acessado");
        return repository.save(cliente);
    }

    public ClienteEntity implementaBuscaPorUUID(ClienteId clienteId) {
        log.info("Método que implementa busca de cliente por uuid acessado");

        Optional<ClienteEntity> clienteOptional = repository.buscaPorId(clienteId.getEmpresa(), clienteId.getUuid());

        ClienteEntity clienteEntity;
        if (clienteOptional.isPresent()) {
            clienteEntity = clienteOptional.get();
            log.info("Cliente encontrado");
        } else {
            log.warn("Nenhum cliente foi encontrado com o id: {}", clienteId);
            throw new ObjectNotFoundException("Nenhum cliente foi encontrado com o id informado");
        }
        log.info("Retornando o cliente encontrado...");
        return clienteEntity;
    }

    public List<ClienteEntity> implementaBuscaPorTodos(UUID uuidEmpresaSessao) {
        log.info("Método que implementa busca por todos os clientes acessado");
        return repository.buscaTodos(uuidEmpresaSessao);
    }

    public List<ClienteEntity> implementaBuscaPorIdEmMassa(UUID uuidEmpresaSessao, List<UUID> uuidClientes) {
        log.info("Método que implementa busca de cliente por id em massa acessado. Ids: {}", uuidClientes.toString());

        log.info("Criando lista de objetos do tipo ClienteId...");
        List<ClienteId> clienteIds = new ArrayList<>();
        uuidClientes.forEach(uuidCliente -> clienteIds.add(new ClienteId(uuidEmpresaSessao, uuidCliente)));
        log.info("Lista criada com sucesso. Iniciando busca por clientes por lista de ids...");

        List<ClienteEntity> clientes =
                (repository.findAllById(clienteIds)).stream().filter(Objects::nonNull).collect(Collectors.toList());
        log.info("Busca realizada com sucesso");

        if (!clientes.isEmpty()) {
            log.info("{} Clientes encontrados", clientes.size());
        } else {
            log.warn("Nenhum cliente foi encontrado");
            throw new ObjectNotFoundException("Nenhum cliente foi encontrado com os uuid informados");
        }
        log.info("Retornando os clientes encontrados...");
        return clientes;
    }

    public ImagemEntity implementaBuscaDeImagemDePerfilPorId(ClienteId clienteId) {
        log.info("Método que implementa busca de imagem de perfil de cliente por uuid acessado. Id: {}", clienteId);

        Optional<ImagemEntity> imagemEntityOptional =
                repository.buscaImagemPerfilPorId(clienteId.getEmpresa(), clienteId.getUuid());

        ImagemEntity imagemEntity;
        if (imagemEntityOptional.isPresent()) {
            imagemEntity = imagemEntityOptional.get();
            log.info("Imagem de perfil encontrada: {}", imagemEntity.getNome());
        } else {
            log.warn("Nenhuma imagem de perfil foi encontrada com o uuidCliente {}", clienteId);
            throw new ObjectNotFoundException("Nenhuma imagem de perfil foi encontrada no registro do cliente");
        }
        log.info("Retornando a imagem de perfil encontrada...");
        return imagemEntity;
    }

}
