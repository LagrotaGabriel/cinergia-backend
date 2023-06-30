package br.com.backend.services.transferencia;

import br.com.backend.models.dto.transferencia.response.TransferenciaPageResponse;
import br.com.backend.models.entities.EmpresaEntity;
import br.com.backend.models.entities.TransferenciaEntity;
import br.com.backend.repositories.transferencia.TransferenciaRepository;
import br.com.backend.repositories.transferencia.impl.TransferenciaRepositoryImpl;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransferenciaService {

    @Autowired
    TransferenciaRepository transferenciaRepository;

    @Autowired
    TransferenciaTypeConverter transferenciaTypeConverter;

    @Autowired
    TransferenciaRepositoryImpl transferenciaRepositoryImpl;

    public TransferenciaPageResponse obtemTransferenciasEmpresa(EmpresaEntity empresa, Pageable pageable) {

        log.debug("Método de serviço de obtenção paginada de transferências da empresa acessado");

        log.debug("Acessando repositório de busca de transferências da empresa");
        Page<TransferenciaEntity> transferenciaPage = transferenciaRepository.buscaPorTransferencias(pageable, empresa.getId());

        log.debug("Busca de transferências por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        TransferenciaPageResponse transferenciaPageResponse = transferenciaTypeConverter
                .converteListaDeTransferenciasEntityParaTransferenciasResponse(transferenciaPage);
        log.debug(Constantes.CONVERSAO_DE_TIPAGEM_COM_SUCESSO);

        log.info("A busca paginada de transferências foi realizada com sucesso");
        return transferenciaPageResponse;

    }

}
