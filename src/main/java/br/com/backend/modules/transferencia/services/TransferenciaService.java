package br.com.backend.modules.transferencia.services;

import br.com.backend.modules.transferencia.models.dto.request.TransferenciaRequest;
import br.com.backend.modules.transferencia.models.dto.response.TransferenciaResponse;
import br.com.backend.modules.transferencia.models.dto.response.page.TransferenciaPageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface TransferenciaService {

    @Transactional
    TransferenciaResponse criaTransferencia(UUID uuidEmpresaSessao, TransferenciaRequest transferenciaRequest);

    TransferenciaPageResponse obtemTransferenciasPaginadas(UUID uuidEmpresaSessao, Pageable pageable);
}
