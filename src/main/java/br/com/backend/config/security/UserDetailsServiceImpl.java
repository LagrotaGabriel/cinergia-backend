package br.com.backend.config.security;

import br.com.backend.models.entities.EmpresaEntity;
import br.com.backend.repositories.empresa.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public UserDetails loadUserByUsername(String cpfCnpj) throws UsernameNotFoundException {
        Optional<EmpresaEntity> empresa = empresaRepository.buscaPorCpfCnpj(cpfCnpj);
        if (empresa.isPresent()) {
            return new UserSS(empresa.get().getId(),
                    empresa.get().getCpfCnpj(),
                    empresa.get().getAcessoSistema().getSenha(),
                    empresa.get().getAcessoSistema().getPerfis());
        }
        throw new UsernameNotFoundException("Usuário não encontrado");
    }
}
