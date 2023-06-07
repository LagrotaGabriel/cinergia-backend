package br.com.backend.config.security;

import br.com.backend.models.entities.EmpresaEntity;
import br.com.backend.repositories.empresa.EmpresaRepository;
import br.com.backend.services.exceptions.ObjectNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JWTUtil {

    @Autowired
    EmpresaRepository empresaRepository;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();
    }

    public boolean tokenValido(String token) {
        Claims claims = getClaims(token);
        if (claims != null) {
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());

            return username != null && expirationDate != null && now.before(expirationDate);
        }
        return false;
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

    public EmpresaEntity obtemUsuarioAtivo(HttpServletRequest req) {
        String token = req.getHeader("Authorization").replace("Bearer ", "");
        String username = getUsernameFromToken(token);
        Optional<EmpresaEntity> empresaOptional = empresaRepository.buscaPorCpfCnpj(username);
        return empresaOptional.orElseThrow(() ->
                new ObjectNotFoundException("Nenhuma empresa foi encontrada pelo CPF/CNPJ " + username));
    }

}
