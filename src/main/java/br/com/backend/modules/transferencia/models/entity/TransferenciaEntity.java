package br.com.backend.modules.transferencia.models.entity;

import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.transferencia.models.dto.request.TransferenciaRequest;
import br.com.backend.modules.transferencia.models.entity.id.TransferenciaId;
import br.com.backend.modules.transferencia.models.enums.StatusTransferenciaEnum;
import br.com.backend.modules.transferencia.models.enums.TipoChavePixEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TransferenciaId.class)
@Table(name = "tb_sbs_transferencia")
public class TransferenciaEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária da transferência - UUID")
    @Column(name = "cod_transferencia_trf", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    @Id
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("Chave primária da notificação - ID da empresa ao qual a transferência faz parte")
    @JoinColumn(name = "cod_empresa_trf", referencedColumnName = "cod_empresa_emp", nullable = false, updatable = false)
    private EmpresaEntity empresa;

    @Comment("Código de identificação da transferência na integradora ASAAS")
    @Column(name = "cod_asaas_trf", updatable = false, nullable = false, unique = true)
    private String asaasId;

    @Comment("Data em que o cadastro da transferência foi realizado")
    @Column(name = "dt_datacadastro_trf", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro da transferência foi realizado")
    @Column(name = "hr_horacadastro_trf", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Descrição da transferência")
    @Column(name = "str_descricao_trf", nullable = false, updatable = false, length = 70)
    private String descricao;

    @Comment("Saldo atual da transferência")
    @Column(name = "mon_valor_trf", nullable = false, updatable = false, scale = 2)
    private Double valor;

    @Comment("Chave PIX")
    @Column(name = "str_chavepix_trf", nullable = false, updatable = false, length = 70)
    private String chavePix;

    @Enumerated(EnumType.STRING)
    @Comment("Tipo de chave pix da transferência: " +
            "0 - Cpf, " +
            "1 - Cnpj, " +
            "2 - E-mail, " +
            "3 - Telefone, " +
            "4 - Chave aleatória")
    @Column(name = "enm_tipochavepix_trf", nullable = false, updatable = false)
    private TipoChavePixEnum tipoChavePix;

    @Enumerated(EnumType.STRING)
    @Comment("Status da transferência PIX: " +
            "0 - Pendente, " +
            "1 - Sucesso, " +
            "2 - Saldo insuficiente, " +
            "3 - Chave pix inexistente, " +
            "4 - Falha")
    @Column(name = "enm_status_trf", nullable = false)
    private StatusTransferenciaEnum statusTransferencia;

    public TransferenciaEntity constroiTransferenciaEntityParaCriacao(EmpresaEntity empresaSessao,
                                                                      String idTransferenciaAsaas,
                                                                      TransferenciaRequest transferenciaRequest) {
        return TransferenciaEntity.builder()
                .empresa(empresaSessao)
                .asaasId(idTransferenciaAsaas)
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .valor(transferenciaRequest.getValor())
                .chavePix(transferenciaRequest.getChavePix())
                .tipoChavePix(transferenciaRequest.getTipoChavePix())
                .statusTransferencia(StatusTransferenciaEnum.PENDENTE)
                .descricao(transferenciaRequest.getDescricao())
                .build();
    }
}
