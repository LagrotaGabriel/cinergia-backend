package br.com.backend.modules.empresa.repository.views;

import br.com.backend.globals.models.acesso.entity.AcessoSistemaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaSessaoView {
    UUID uuid;
    String cpfCnpj;
    AcessoSistemaEntity acessoSistema;
}
