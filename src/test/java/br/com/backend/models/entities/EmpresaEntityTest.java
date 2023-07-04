package br.com.backend.models.entities;

import br.com.backend.models.entities.mock.EmpresaEntityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@SpringBootTest
@DisplayName("Entity: Empresa")
class EmpresaEntityTest {

    @Test
    @DisplayName("Deve testar dataBuilder")
    void deveTestarDataBuilder() {
        Assertions.assertEquals(
                "EmpresaEntity(id=1, dataCadastro=2023-02-03, horaCadastro=10:48, " +
                        "nomeEmpresa=Akadia Solutions, email=akadia@gmail.com, cpfCnpj=12345678000112, " +
                        "dataNascimento=1998-07-21, contaEmpresaAsaas=null, endereco=null, telefone=null, " +
                        "celular=null, acessoSistema=null, logoEmpresa=null)",
                EmpresaEntityBuilder.builder().build().toString()
        );
    }

    @Test
    @DisplayName("Deve testar @AllArgsConstructor")
    void deveTestarAllArgsConstructor() {
        EmpresaEntity empresaEntity = new EmpresaEntity(
                1L,
                LocalDate.of(2023, 2, 3).toString(),
                LocalTime.of(15, 27).toString(),
                "Organizações Tabajara",
                "tabajara@gmail.com",
                "12345678000150",
                "1998-07-21",
                0.0,
                null,
                null,
                null,
                null,
                null,
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );

        Assertions.assertEquals(
                "EmpresaEntity(id=1, dataCadastro=2023-02-03, horaCadastro=15:27, " +
                        "nomeEmpresa=Organizações Tabajara, email=tabajara@gmail.com, cpfCnpj=12345678000150, " +
                        "dataNascimento=1998-07-21, contaEmpresaAsaas=null, endereco=null, telefone=null, celular=null, " +
                        "acessoSistema=null, logoEmpresa=null)",
                empresaEntity.toString()
        );

    }

    @Test
    @DisplayName("Deve testar @Builder")
    void deveTestarBuilder() {
        EmpresaEntity empresaEntity = EmpresaEntity.builder()
                .id(1L)
                .dataCadastro(LocalDate.of(2023, 2, 27).toString())
                .horaCadastro(LocalTime.of(17, 40).toString())
                .nomeEmpresa("Organizações Tabajara")
                .email("tabajara@gmail.com")
                .cpfCnpj("12345678000150")
                .dataNascimento("1998-07-21")
                .endereco(null)
                .telefone(null)
                .acessoSistema(null)
                .logoEmpresa(null)
                .saldo(0.0)
                .modelosPlano(new ArrayList<>())
                .clientes(new ArrayList<>())
                .build();
        Assertions.assertEquals(
                "EmpresaEntity(id=1, dataCadastro=2023-02-27, horaCadastro=17:40, " +
                        "nomeEmpresa=Organizações Tabajara, email=tabajara@gmail.com, cpfCnpj=12345678000150, " +
                        "dataNascimento=1998-07-21, contaEmpresaAsaas=null, endereco=null, telefone=null, " +
                        "celular=null, acessoSistema=null, logoEmpresa=null)",
                empresaEntity.toString()
        );
    }

}