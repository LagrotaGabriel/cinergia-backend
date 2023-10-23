package br.com.backend.modules.plano.proxy.utils;

import br.com.backend.modules.plano.models.enums.PeriodicidadeEnum;
import br.com.backend.modules.plano.proxy.global.request.enums.CycleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlanoAsaasUtil {

    public CycleEnum transformaPeriodicidadeEnumEmCycleEnum(PeriodicidadeEnum periodicidadeEnum) {
        switch (periodicidadeEnum) {
            case SEMANAL: return CycleEnum.WEEKLY;
            case MENSAL: return CycleEnum.MONTHLY;
            case SEMESTRAL: return CycleEnum.SEMIANNUALY;
            default: return CycleEnum.YEARLY;
        }
    }

}
