package br.com.backend.config.security.user;

import br.com.backend.modules.empresa.repository.EmpresaRepository;
import br.com.backend.modules.empresa.repository.views.EmpresaSessaoView;
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
        Optional<EmpresaSessaoView> empresa = empresaRepository.buscaEmpresaSessaoAtual(cpfCnpj);
        if (empresa.isPresent()) {
            return new UserSS(
                    empresa.get().getUuid(),
                    empresa.get().getCpfCnpj(),
                    empresa.get().getAcessoSistema().getSenha(),
                    empresa.get().getAcessoSistema().getPerfis());
        }
        throw new UsernameNotFoundException("Usuário não encontrado");
    }
}
