package br.com.backend.modules.cliente.repository.impl;

import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.globals.models.imagem.entity.ImagemEntity;
import br.com.backend.modules.cliente.repository.ClienteRepository;
import br.com.backend.exceptions.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ClienteRepositoryImpl {

    @Autowired
    ClienteRepository repository;

    @Transactional
    public ClienteEntity implementaPersistencia(ClienteEntity cliente) {
        log.debug("Método de serviço que implementa persistência do cliente acessado");
        return repository.save(cliente);
    }

    public Optional<ClienteEntity> implementaBuscaPorCpfCnpjIdentico(String cpfCnpj, Long id) {
        log.debug("Método que implementa busca de cliente por CPF/CNPJ acessado. CPF/CNPJ: {}", cpfCnpj);
        return repository.buscaPorCpfCnpjIdenticoNaEmpresaDaSessaoAtual(cpfCnpj, id);
    }

    public ClienteEntity implementaBuscaPorId(Long id, Long idEmpresa) {
        log.debug("Método que implementa busca de cliente por id acessado. Id: {}", id);

        Optional<ClienteEntity> clienteOptional = repository.buscaPorId(id, idEmpresa);

        ClienteEntity clienteEntity;
        if (clienteOptional.isPresent()) {
            clienteEntity = clienteOptional.get();
            log.debug("Cliente encontrado: {}", clienteEntity);
        } else {
            log.warn("Nenhum cliente foi encontrado com o id {}", id);
            throw new ObjectNotFoundException("Nenhum cliente foi encontrado com o id informado");
        }
        log.debug("Retornando o cliente encontrado...");
        return clienteEntity;
    }

    @Transactional
    public void implementaPersistenciaEmMassa(List<ClienteEntity> clientes) {
        log.debug("Método de serviço que implementa persistência em massa do cliente acessado");
        repository.saveAll((clientes));
    }

    public List<ClienteEntity> implementaBuscaPorTodos(Long idEmpresa) {
        log.debug("Método que implementa busca por todos os clientes acessado");
        return repository.buscaTodos(idEmpresa);
    }

    public List<ClienteEntity> implementaBuscaPorIdEmMassa(List<Long> ids) {
        log.debug("Método que implementa busca de cliente por id em massa acessado. Ids: {}", ids.toString());

        List<ClienteEntity> clientes = repository.findAllById(ids);

        if (!clientes.isEmpty()) {
            log.debug("{} Clientes encontrados", clientes.size());
        } else {
            log.warn("Nenhum cliente foi encontrado");
            throw new ObjectNotFoundException("Nenhum cliente foi encontrado com os ids informados");
        }
        log.debug("Retornando os clientes encontrados...");
        return clientes;
    }

    public ImagemEntity implementaBuscaDeImagemDePerfilPorId(Long id, Long idEmpresa) {
        log.debug("Método que implementa busca de imagem de perfil de cliente por id acessado. Id: {}", id);

        Optional<ImagemEntity> imagemEntityOptional = repository.buscaImagemPerfilPorId(id, idEmpresa);

        ImagemEntity imagemEntity;
        if (imagemEntityOptional.isPresent()) {
            imagemEntity = imagemEntityOptional.get();
            log.debug("Imagem de perfil encontrada: {}", imagemEntity.getNome());
        } else {
            log.warn("Nenhuma imagem de perfil foi encontrada com o id {}", id);
            throw new ObjectNotFoundException("Nenhuma imagem de perfil foi encontrada com o id informado");
        }
        log.debug("Retornando a imagem de perfil encontrada...");
        return imagemEntity;
    }

    public ClienteEntity implementaBuscaPorCodigoClienteAsaas(String codigoClienteAsaas) {
        log.debug("Método que implementa busca por cliente Asaas pelo seu código de cliente acessado");

        Optional<ClienteEntity> clienteOptional =
                repository.findByAsaasId(codigoClienteAsaas);

        ClienteEntity clienteSistema;
        if (clienteOptional.isPresent()) {
            clienteSistema = clienteOptional.get();
            log.debug("Cliente encontrado: {}", clienteSistema);
        } else {
            log.warn("Nenhum cliente foi encontrado com o código Asaas informado: {}", codigoClienteAsaas);
            throw new ObjectNotFoundException("Nenhum cliente foi encontrado com o codigo Asaas informado");
        }

        log.debug("Retornando cliente...");
        return clienteSistema;
    }

}
