package br.com.backend.modules.transferencia.models.entity;

import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.transferencia.models.entity.id.TransferenciaId;
import br.com.backend.modules.transferencia.models.enums.StatusTransferenciaEnum;
import br.com.backend.modules.transferencia.models.enums.TipoChavePixEnum;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TransferenciaId.class)
@Table(name = "TB_SBS_TRANSFERENCIA")
public class TransferenciaEntity {

    @Id
    @Comment("Chave primária da transferência - UUID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_NOTIFICACAO_TRF", nullable = false, updatable = false, length = 36)
    private UUID uuid;

    @Id
    @ManyToOne
    @EqualsAndHashCode.Include
    @Comment("Chave primária da notificação - ID da empresa ao qual a transferência faz parte")
    @JoinColumn(name = "COD_EMPRESA_TRF", nullable = false, updatable = false)
    private EmpresaEntity empresa;

    @Comment("Código de identificação da transferência na integradora ASAAS")
    @Column(name = "COD_ASAAS_TRF", updatable = false, nullable = false, unique = true)
    private String asaasId;

    @Comment("Data em que o cadastro da transferência foi realizado")
    @Column(name = "DT_DATACADASTRO_TRF", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro da transferência foi realizado")
    @Column(name = "HR_HORACADASTRO_TRF", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Descrição da transferência")
    @Column(name = "STR_DESCRICAO_TRF", nullable = false, updatable = false, length = 70)
    private String descricao;

    @Comment("Saldo atual da transferência")
    @Column(name = "MON_VALOR_TRF", nullable = false, updatable = false, scale = 2)
    private Double valor;

    @Comment("Chave PIX")
    @Column(name = "STR_CHAVEPIX_TRF", nullable = false, updatable = false, length = 70)
    private String chavePix;

    @Enumerated(EnumType.STRING)
    @Comment("Tipo de chave pix da transferência: " +
            "0 - Cpf, " +
            "1 - Cnpj, " +
            "2 - E-mail, " +
            "3 - Telefone, " +
            "4 - Chave aleatória")
    @Column(name = "ENM_TIPOCHAVEPIX_TRF", nullable = false, updatable = false)
    private TipoChavePixEnum tipoChavePix;

    @Enumerated(EnumType.STRING)
    @Comment("Status da transferência PIX: " +
            "0 - Pendente, " +
            "1 - Sucesso, " +
            "2 - Saldo insuficiente, " +
            "3 - Chave pix inexistente, " +
            "4 - Falha")
    @Column(name = "ENM_STATUS_TRF", nullable = false)
    private StatusTransferenciaEnum statusTransferencia;
}
