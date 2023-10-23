package br.com.backend.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class RelatorioUtil {

    public void setaAtributosHttpServletResponseRelatorio(HttpServletResponse httpServletResponse,
                                                          String tituloRelatorio) {
        httpServletResponse.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachement; relatorio"
                + tituloRelatorio
                + new SimpleDateFormat("dd.MM.yyyy_HHmmss").format(new Date())
                + ".pdf";
        httpServletResponse.setHeader(headerKey, headerValue);
    }

}
