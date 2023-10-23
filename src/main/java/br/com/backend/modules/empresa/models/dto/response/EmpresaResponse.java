package br.com.backend.modules.empresa.models.dto.response;

import br.com.backend.globals.models.endereco.dto.response.EnderecoResponse;
import br.com.backend.globals.models.telefone.response.TelefoneResponse;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Data
@Slf4j
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaResponse {
    private UUID uuid;
    private String dataCadastro;
    private String horaCadastro;
    private String nomeEmpresa;
    private String email;
    private String cpfCnpj;
    private String dataNascimento;
    private Double saldo;
    private EnderecoResponse endereco;
    private TelefoneResponse telefone;

    public EmpresaResponse constroiEmpresaResponse(EmpresaEntity empresaEntity) {
        log.info("Método de conversão de objeto do tipo EmpresaEntity para objeto do tipo EmpresaResponse acessado");

        log.info("Iniciando construção do objeto EmpresaResponse...");
        EmpresaResponse empresaResponse = EmpresaResponse.builder()
                .uuid(empresaEntity.getUuid())
                .dataCadastro(empresaEntity.getDataCadastro())
                .horaCadastro(empresaEntity.getHoraCadastro())
                .nomeEmpresa(empresaEntity.getNomeEmpresa())
                .email(empresaEntity.getEmail())
                .cpfCnpj(empresaEntity.getCpfCnpj())
                .dataNascimento(empresaEntity.getDataNascimento())
                .saldo(empresaEntity.getSaldo())
                .endereco(empresaEntity.getEndereco() == null
                        ? null
                        : new EnderecoResponse().constroiEnderecoResponse(empresaEntity.getEndereco()))
                .telefone(empresaEntity.getTelefone() == null
                        ? null
                        : new TelefoneResponse().constroiTelefoneResponse(empresaEntity.getTelefone()))
                .build();
        log.debug("Objeto ClienteResponse buildado com sucesso. Retornando...");
        return empresaResponse;
    }
}