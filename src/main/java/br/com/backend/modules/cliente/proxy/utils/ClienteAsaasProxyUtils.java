package br.com.backend.modules.cliente.proxy.utils;

import br.com.backend.globals.models.telefone.request.TelefoneRequest;

import java.util.List;

public class ClienteAsaasProxyUtils {

    ClienteAsaasProxyUtils() {}

    public static String obtemTelefoneFixoCliente(List<TelefoneRequest> telefones) {

        if (telefones == null) return null;
        else {
            if (telefones.isEmpty()) return null;
        }

        TelefoneRequest telefone = telefones.get(0);

        return telefone.obtemPrefixoComNumeroJuntos();
    }

    public static String obtemTelefoneCelularCliente(List<TelefoneRequest> telefones) {
        if (telefones == null) return null;
        else {
            if (telefones.isEmpty()) return null;
        }

        for (TelefoneRequest telefone : telefones) {
            if (telefone.getNumero().length() == 9)
                return telefone.obtemPrefixoComNumeroJuntos();
        }

        return null;
    }
}
