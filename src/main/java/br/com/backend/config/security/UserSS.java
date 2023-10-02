package br.com.backend.config.security;

import br.com.backend.globals.models.acesso.enums.PerfilEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UserSS implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String cpfCnpj;
    private final String senha;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserSS(Long id, String cpfCnpj, String senha, Set<PerfilEnum> perfis) {
        super();
        this.id = id;
        this.cpfCnpj = cpfCnpj;
        this.senha = senha;
        this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority("ROLE_" + x.getRole())).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return cpfCnpj;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
