package br.com.backend.models.entities;

import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.models.entities.mock.ClienteEntityBuilder;
import br.com.backend.modules.cliente.models.enums.StatusClienteEnum;
import br.com.backend.modules.cliente.models.enums.TipoPessoaEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Entity: ClienteEntity")
class ClienteEntityTest {

    @Test
    @DisplayName("Deve testar dataBuilder")
    void deveTestarDataBuilder() {
        Assertions.assertEquals(
                "ClienteEntity(id=1, idEmpresaResponsavel=null, asaasId=null, dataCadastro=2023-02-03, " +
                        "horaCadastro=10:40, nome=Fulano, email=fulano@gmail.com, cpfCnpj=null, observacoes=null, " +
                        "statusCliente=null, dataNascimento=2023-02-03, tipoPessoa=null, acessoSistema=null, " +
                        "exclusao=null, endereco=null, fotoPerfil=null)",
                ClienteEntityBuilder.builder().build().toString()
        );

    }

    @Test
    @DisplayName("Deve testar @AllArgsConstructor")
    void deveTestarAllArgsConstructor() {
        ClienteEntity clienteEntity = new ClienteEntity(
                1L,
                1L,
                "cus_123456",
                LocalDate.of(2023, 2, 27).toString(),
                LocalTime.of(17, 40).toString(),
                "Gabriel Lagrota",
                "gabrielafonso@mail.com.br",
                "12345678910",
                "observação",
                StatusClienteEnum.COMUM,
                "1998-07-21",
                TipoPessoaEnum.FISICA,
                null,
                null,
                null,
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );
        Assertions.assertEquals(
                "ClienteEntity(id=1, idEmpresaResponsavel=1, asaasId=cus_123456, dataCadastro=2023-02-27, " +
                        "horaCadastro=17:40, nome=Gabriel Lagrota, email=gabrielafonso@mail.com.br, " +
                        "cpfCnpj=12345678910, observacoes=observação, statusCliente=COMUM, dataNascimento=1998-07-21, " +
                        "tipoPessoa=FISICA, acessoSistema=null, exclusao=null, endereco=null, fotoPerfil=null)",
                clienteEntity.toString()
        );

    }

    @Test
    @DisplayName("Deve testar @Builder")
    void deveTestarBuilder() {
        ClienteEntity clienteEntity = ClienteEntity.builder()
                .id(1L)
                .dataCadastro(LocalDate.of(2023, 2, 27).toString())
                .horaCadastro(LocalTime.of(17, 40).toString())
                .nome("Gabriel Lagrota")
                .email("gabrielafonso@mail.com.br")
                .dataNascimento("1998-07-21")
                .exclusao(null)
                .telefones(null)
                .endereco(null)
                .planos(new ArrayList<>())
                .build();
        Assertions.assertEquals(
                "ClienteEntity(id=1, idEmpresaResponsavel=null, asaasId=null, dataCadastro=2023-02-27, " +
                        "horaCadastro=17:40, nome=Gabriel Lagrota, email=gabrielafonso@mail.com.br, cpfCnpj=null, " +
                        "observacoes=null, statusCliente=null, dataNascimento=1998-07-21, tipoPessoa=null, " +
                        "acessoSistema=null, exclusao=null, endereco=null, fotoPerfil=null)",
                clienteEntity.toString()
        );
    }

}
