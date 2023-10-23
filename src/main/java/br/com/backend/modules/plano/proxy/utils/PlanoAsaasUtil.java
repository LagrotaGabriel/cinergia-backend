package br.com.backend.modules.plano.proxy.utils;

import br.com.backend.modules.plano.models.enums.PeriodicidadeEnum;
import br.com.backend.modules.plano.proxy.global.request.enums.CycleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlanoAsaasUtil {

    public CycleEnum transformaPeriodicidadeEnumEmCycleEnum(PeriodicidadeEnum periodicidadeEnum) {
        return switch (periodicidadeEnum) {
            case SEMANAL -> CycleEnum.WEEKLY;
            case MENSAL -> CycleEnum.MONTHLY;
            case SEMESTRAL -> CycleEnum.SEMIANNUALY;
            default -> CycleEnum.YEARLY;
        };
    }

}
