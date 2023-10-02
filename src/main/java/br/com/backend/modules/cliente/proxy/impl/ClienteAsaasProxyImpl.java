package br.com.backend.modules.cliente.proxy.impl;

import br.com.backend.exceptions.InvalidRequestException;
import br.com.backend.globals.models.endereco.entity.EnderecoEntity;
import br.com.backend.globals.models.telefone.entity.TelefoneEntity;
import br.com.backend.modules.cliente.models.dto.request.ClienteRequest;
import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.modules.cliente.proxy.ClienteAsaasProxy;
import br.com.backend.modules.cliente.proxy.models.request.CriaClienteAsaasRequest;
import br.com.backend.modules.cliente.proxy.models.response.CriaClienteAsaasResponse;
import br.com.backend.modules.cliente.proxy.models.response.RemoveClienteAsaasResponse;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ClienteAsaasProxyImpl {

    @Autowired
    ClienteAsaasProxy clienteAsaasProxy;

    public String realizaCriacaoClienteAsaas(ClienteRequest clienteRequest, Long idEmpresaLogada) {

        log.debug("Método de serviço responsável pela criação de cliente na integradora ASAAS acessado");

        String telefone = obtemTelefoneFixoCliente(clienteRequest.getTelefones());
        String celular = obtemTelefoneCelularCliente(clienteRequest.getTelefones());
        EnderecoEntity endereco = clienteRequest.getEndereco();

        log.debug("Iniciando construção do objeto criaClienteAsaasRequest...");
        CriaClienteAsaasRequest criaClienteAsaasRequest = CriaClienteAsaasRequest.builder()
                .name(clienteRequest.getNome())
                .cpfCnpj(clienteRequest.getCpfCnpj())
                .email(clienteRequest.getEmail())
                .phone(telefone)
                .mobilePhone(celular)
                .address(endereco != null ? endereco.getLogradouro() : null)
                .addressNumber(endereco != null ? endereco.getNumero().toString() : null)
                .complement(endereco != null ? endereco.getComplemento() : null)
                .province(endereco != null ? endereco.getBairro() : null)
                .postalCode(endereco != null ? endereco.getCodigoPostal() : null)
                .externalReference(idEmpresaLogada + ';' + clienteRequest.getCpfCnpj())
                .notificationDisabled(true)
                .additionalEmails(null)
                .municipalInscription(null)
                .stateInscription(null)
                .groupName(idEmpresaLogada.toString())
                .build();

        ResponseEntity<CriaClienteAsaasResponse> responseAsaas;

        try {
            log.debug("Realizando envio de requisição de criação de cliente para a integradora ASAAS...");
            responseAsaas =
                    clienteAsaasProxy.cadastraNovoCliente(criaClienteAsaasRequest, System.getenv(Constantes.TOKEN_ASAAS));
        } catch (Exception e) {
            log.error(Constantes.ERRO_CRIACAO_CLIENTE_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(Constantes.ERRO_CRIACAO_CLIENTE_ASAAS
                    + e.getMessage());
        }

        if (responseAsaas == null) {
            log.error("O valor retornado pela integradora na criação do cliente é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseAsaas.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de criação da cliente na integradora de pagamentos: {}",
                    responseAsaas.getBody());
            throw new InvalidRequestException(Constantes.ERRO_CRIACAO_CLIENTE_ASAAS
                    + responseAsaas.getBody());
        }
        log.debug("Criação de cliente ASAAS realizada com sucesso");

        CriaClienteAsaasResponse clienteAsaasResponse = responseAsaas.getBody();

        if (clienteAsaasResponse == null) {
            log.error("O valor retornado pela integradora na criação do cliente é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        log.debug("Retornando id do cliente gerado: {}", clienteAsaasResponse.getId());
        return clienteAsaasResponse.getId();
    }

    public void realizaRemocaoDoClienteNaIntegradoraAsaas(ClienteEntity cliente) {

        log.debug("Método de serviço responsável pela remoção de cliente na integradora ASAAS acessado");
        ResponseEntity<RemoveClienteAsaasResponse> responseAsaas;

        try {
            log.debug("Realizando envio de requisição de remoção cliente para a integradora ASAAS...");
            responseAsaas =
                    clienteAsaasProxy.removerCliente(cliente.getAsaasId(), System.getenv(Constantes.TOKEN_ASAAS));
        } catch (Exception e) {
            log.error(Constantes.ERRO_REMOCAO_CLIENTE_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(Constantes.ERRO_REMOCAO_CLIENTE_ASAAS
                    + e.getMessage());
        }

        if (responseAsaas == null) {
            log.error("O valor retornado pela integradora na remoção do cliente é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseAsaas.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de remoção do cliente na integradora de pagamentos: {}",
                    responseAsaas.getBody());
            throw new InvalidRequestException(Constantes.ERRO_REMOCAO_CLIENTE_ASAAS
                    + responseAsaas.getBody());
        }
        log.debug("Remoção de cliente ASAAS realizada com sucesso");

        RemoveClienteAsaasResponse removeClienteAsaasResponse = responseAsaas.getBody();

        if (removeClienteAsaasResponse == null) {
            log.error("O valor retornado pela integradora na remoção do cliente é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

    }

    public void realizaAtualizacaoClienteAsaas(String asaasId, ClienteRequest clienteRequest, Long idEmpresaLogada) {

        log.debug("Método de serviço responsável pela atualização de cliente na integradora ASAAS acessado");

        String telefone = obtemTelefoneFixoCliente(clienteRequest.getTelefones());
        String celular = obtemTelefoneCelularCliente(clienteRequest.getTelefones());
        EnderecoEntity endereco = clienteRequest.getEndereco();

        log.debug("Iniciando construção do objeto criaClienteAsaasRequest...");
        CriaClienteAsaasRequest criaClienteAsaasRequest = CriaClienteAsaasRequest.builder()
                .name(clienteRequest.getNome())
                .cpfCnpj(clienteRequest.getCpfCnpj())
                .email(clienteRequest.getEmail())
                .phone(telefone)
                .mobilePhone(celular)
                .address(endereco != null ? endereco.getLogradouro() : null)
                .addressNumber(endereco != null ? endereco.getNumero().toString() : null)
                .complement(endereco != null ? endereco.getComplemento() : null)
                .province(endereco != null ? endereco.getBairro() : null)
                .postalCode(endereco != null ? endereco.getCodigoPostal() : null)
                .externalReference(idEmpresaLogada + ';' + clienteRequest.getCpfCnpj())
                .notificationDisabled(true)
                .additionalEmails(null)
                .municipalInscription(null)
                .stateInscription(null)
                .groupName(idEmpresaLogada.toString())
                .build();

        ResponseEntity<CriaClienteAsaasResponse> responseAsaas;

        try {
            log.debug("Realizando envio de requisição de atualização de cliente para a integradora ASAAS...");
            responseAsaas =
                    clienteAsaasProxy.atualizaCliente(asaasId, criaClienteAsaasRequest, System.getenv(Constantes.TOKEN_ASAAS));
        } catch (Exception e) {
            log.error(Constantes.ERRO_ATUALIZACAO_CLIENTE_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(Constantes.ERRO_ATUALIZACAO_CLIENTE_ASAAS
                    + e.getMessage());
        }

        if (responseAsaas == null) {
            log.error("O valor retornado pela integradora na atualização do cliente é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseAsaas.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de atualização do cliente na integradora de pagamentos: {}",
                    responseAsaas.getBody());
            throw new InvalidRequestException(Constantes.ERRO_ATUALIZACAO_CLIENTE_ASAAS
                    + responseAsaas.getBody());
        }
        log.debug("Atualização de cliente ASAAS realizada com sucesso");

        CriaClienteAsaasResponse clienteAsaasResponse = responseAsaas.getBody();

        if (clienteAsaasResponse == null) {
            log.error("O valor retornado pela integradora na atualização do cliente é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }
    }

    private String obtemTelefoneFixoCliente(List<TelefoneEntity> telefones) {

        if (telefones == null) return null;
        else {
            if (telefones.isEmpty()) return null;
        }

        TelefoneEntity telefone = telefones.get(0);

        return (telefone.getPrefixo() + telefone.getNumero());
    }

    private String obtemTelefoneCelularCliente(List<TelefoneEntity> telefones) {
        if (telefones == null) return null;
        else {
            if (telefones.isEmpty()) return null;
        }

        for (TelefoneEntity telefone : telefones) {
            if (telefone.getNumero().length() == 9)
                return (telefone.getPrefixo() + telefone.getNumero());
        }

        return null;
    }
}
