package br.com.backend.services.plano;

import br.com.backend.models.dto.plano.request.PlanoRequest;
import br.com.backend.models.dto.plano.response.PlanoPageResponse;
import br.com.backend.models.dto.plano.response.PlanoResponse;
import br.com.backend.models.entities.ClienteEntity;
import br.com.backend.models.entities.PlanoEntity;
import br.com.backend.models.entities.empresa.EmpresaEntity;
import br.com.backend.models.enums.StatusPlanoEnum;
import br.com.backend.proxy.plano.response.CriaPlanoAsaasResponse;
import br.com.backend.repositories.cliente.impl.ClienteRepositoryImpl;
import br.com.backend.repositories.plano.PlanoRepository;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@Slf4j
@Service
public class PlanoService {

    @Autowired
    ClienteRepositoryImpl clienteRepositoryImpl;

    @Autowired
    PlanoRepository planoRepository;

    @Autowired
    PlanoTypeConverter planoTypeConverter;

    public PlanoResponse criaNovoPlano(EmpresaEntity empresaLogada, Long idCliente, PlanoRequest planoRequest) {

        log.debug("Método de serviço de criação de novo plano acessado");

        log.debug("Iniciando acesso ao método de busca de cliente por id com o id {} na empresa de id {}...",
                idCliente, empresaLogada.getId());
        ClienteEntity clienteEntity = clienteRepositoryImpl.implementaBuscaPorId(idCliente, empresaLogada.getId());

        log.debug("Iniciando criação do objeto PlanoEntity...");
        PlanoEntity planoEntity = PlanoEntity.builder()
                .idEmpresaResponsavel(empresaLogada.getId())
                .idClienteResponsavel(idCliente)
                .idAsaas(Constantes.formasPagamentoAsaas.contains(planoRequest.getFormaPagamento())
                        ? realizaCriacaoDePlanoDeAssinaturaNaIntegradoraAsaas(planoRequest).getId()
                        : null)
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .dataInicio(planoRequest.getDataInicio())
                .descricao(planoRequest.getDescricao())
                .valor(planoRequest.getValor())
                .formaPagamento(planoRequest.getFormaPagamento())
                .statusPlano(StatusPlanoEnum.INATIVO)
                .periodicidade(planoRequest.getPeriodicidade())
                .notificacoes(planoRequest.getNotificacoes())
                .cartao(null)
                .pagamentos(new ArrayList<>())
                .build();
        log.debug("Objeto planoEntity criado com sucesso");

        log.debug("Acoplando plano ao cliente encontrado...");
        clienteEntity.getPlanos().add(planoEntity);

        log.debug("Iniciando acesso ao método de implementação da persistência do cliente com o novo plano acoplado...");
        ClienteEntity clientePersistido = clienteRepositoryImpl.implementaPersistencia(clienteEntity);

        log.debug("Iniciando obtenção do plano criado...");
        PlanoEntity planoPersistido = clientePersistido.getPlanos().get(clientePersistido.getPlanos().size() - 1);

        log.debug("Convertendo planoEntity criado para planoResponse...");
        PlanoResponse planoResponse = planoTypeConverter.convertePlanoEntityParaPlanoResponse(planoPersistido);

        log.info("Plano criado com sucesso");
        return planoResponse;
    }

    private CriaPlanoAsaasResponse realizaCriacaoDePlanoDeAssinaturaNaIntegradoraAsaas(PlanoRequest planoRequest) {
        return null;
    }

    public PlanoPageResponse realizaBuscaPaginadaPorPlanosDoCliente(EmpresaEntity empresaLogada,
                                                                    Pageable pageable,
                                                                    Long idCliente) {
        log.debug("Método de serviço de obtenção paginada de planos do cliente acessado");

        log.debug("Acessando repositório de busca de planos do cliente");
        Page<PlanoEntity> planoPage = planoRepository.buscaPorPlanosDoCliente(pageable, empresaLogada.getId(), idCliente);

        log.debug("Busca de planos por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        PlanoPageResponse planoPageResponse = planoTypeConverter.converteListaDePlanosEntityParaPlanosResponse(planoPage);
        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca paginada de planos foi realizada com sucesso");
        return planoPageResponse;
    }

    public PlanoPageResponse realizaBuscaPaginadaPorPlanos(EmpresaEntity empresaLogada,
                                                           Pageable pageable,
                                                           String campoBusca) {
        log.debug("Método de serviço de obtenção paginada de planos acessado. Campo de busca: {}",
                campoBusca != null ? campoBusca : "Nulo");

        log.debug("Acessando repositório de busca de planos");
        Page<PlanoEntity> planoPage = campoBusca != null && !campoBusca.isEmpty()
                ? planoRepository.buscaPorPlanosTypeAhead(pageable, campoBusca, empresaLogada.getId())
                : planoRepository.buscaPorPlanos(pageable, empresaLogada.getId());

        log.debug("Busca de planos por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        PlanoPageResponse planoPageResponse = planoTypeConverter.converteListaDePlanosEntityParaPlanosResponse(planoPage);
        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca paginada de planos foi realizada com sucesso");
        return planoPageResponse;
    }
}
