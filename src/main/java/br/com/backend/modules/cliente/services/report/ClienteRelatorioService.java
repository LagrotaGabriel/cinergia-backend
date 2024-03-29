package br.com.backend.modules.cliente.services.report;

import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.modules.cliente.repository.impl.ClienteRepositoryImpl;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.InvalidClassException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ClienteRelatorioService {

    //TODO MELHORAR O CÓDIGO. POSSIVELMENTE PODE SER DIVIDIDO EM UMA CLASSE UTILITÁRIA PARA RELATÓRIOS

    @Autowired
    ClienteRepositoryImpl clienteRepositoryImpl;

    Color STRONG_BLACK = new Color(0, 0, 0);
    Color WEAK_BLACK = new Color(30, 30, 30);
    Color STRONG_COLOR = new Color(93, 121, 155);
    Color MEDIUM_COLOR = new Color(173, 181, 197);
    Color WEAK_COLOR = new Color(192, 198, 209);

    public void exportarPdf(UUID uuidEmpresaSessao, HttpServletResponse response, List<UUID> uuidsClientes)
            throws DocumentException, IOException {

        log.info("Método responsável por encaminhar para métodos de construção de PDF de relatório de cilentes e " +
                "retornar PDF construído acessado");

        log.info("Verificando se listagem de ids de clientes recebidas por parâmetro é vazia...");
        List<ClienteEntity> clientes = uuidsClientes == null || uuidsClientes.isEmpty()
                ? clienteRepositoryImpl.implementaBuscaPorTodos(uuidEmpresaSessao)
                : clienteRepositoryImpl.implementaBuscaPorIdEmMassa(uuidEmpresaSessao, uuidsClientes);

        Collections.reverse(clientes);

        log.info("Iniciando construção do objeto Document, que se trata da estrutura do PDF gerado...");
        try (Document document = new Document(PageSize.A4)) {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            log.info("Iniciando acesso ao método de construção do título do PDF...");
            document.add(constroiParagrafoTitulo());
            log.info("Iniciando acesso ao método de construção do subtítulo com data, hora e responsável do PDF...");
            document.add(constroiParagrafoDataHora());
            log.info("Iniciando acesso ao método de construção da tabela do PDF...");
            document.add(constroiTabelaObjetos(clientes));
            log.info("Iniciando acesso ao método de construção dos informativos do PDF...");
            document.add(constroiTabelaInformativos(clientes));
            log.info("PDF gerado com sucesso");
        } catch (Exception e) {
            log.error("Ocorreu um erro na criação do PDF: {}", e.getMessage());
            throw new InvalidClassException(e.toString());
        }

    }

    private void escreveHeaderTabela(PdfPTable table) {

        log.info("Método responsável por criar o header da tabela do PDF acessado");

        log.info("Setando estilização do header da tabela...");
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(STRONG_COLOR);
        cell.setPadding(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);

        log.info("Setando fonte do header da tabela...");
        com.lowagie.text.Font font = FontFactory.getFont(FontFactory.COURIER, 7, STRONG_BLACK);

        log.info("Setando campos do header da tabela...");

        cell.setPhrase(new Phrase("Data", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Nome", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Endereço", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Telefone", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Cpf/Cnpj", font));
        table.addCell(cell);
    }

    private void escreveDadosTabela(PdfPTable table, List<ClienteEntity> clientes) {

        log.info("Método responsável por escrever os dados na tabela acessado");

        int contador = 1;

        log.info("Iniciando iteração pelos clientes que devem ser exibidos no relatório...");
        for (ClienteEntity cliente : clientes) {

            log.info("Cliente de id {} em iteração", cliente.getUuid());

            log.info("Setando estilização e fonte das linhas da tabela...");
            PdfPCell cell = new PdfPCell();
            com.lowagie.text.Font font = FontFactory.getFont(FontFactory.COURIER, 6, WEAK_BLACK);
            cell.setPadding(5);

            if (contador % 2 == 1) cell.setBackgroundColor(WEAK_COLOR);
            else cell.setBackgroundColor(MEDIUM_COLOR);

            log.info("Setando campos da tabela...");

            cell.setPhrase(new Phrase(cliente.getDataCriacao() + " " + cliente.getHoraCriacao(), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(cliente.getNome(), font));
            table.addCell(cell);

            String endereco = cliente.getEndereco() == null
                    ? "Sem endereço"
                    : cliente.getEndereco().getLogradouro() + ", " + cliente.getEndereco().getNumero();

            cell.setPhrase(new Phrase(endereco, font));
            table.addCell(cell);

            String telefone = cliente.getTelefones() == null
                    ? "Sem telefone"
                    : obtemTelefoneCliente(cliente);

            cell.setPhrase(new Phrase(telefone, font));
            table.addCell(cell);

            String cpfCnpj = cliente.getCpfCnpj() == null ? "Sem Cpf/Cnpj" : cliente.getCpfCnpj();
            cell.setPhrase(new Phrase(cpfCnpj, font));
            table.addCell(cell);

            contador++;
        }
    }

    private String obtemTelefoneCliente(ClienteEntity cliente) {
        return !cliente.getTelefones().isEmpty()
                ? "(" + cliente.getTelefones().get(0).getPrefixo() + ") " + cliente.getTelefones().get(0).getNumero()
                : "Sem telefone";
    }

    public Paragraph constroiParagrafoTitulo() {
        log.info("Método responsável por criar o título do PDF acessado");

        log.info("Setando estilização do título e setando seu conteúdo...");
        com.lowagie.text.Font font = FontFactory.getFont(FontFactory.COURIER_BOLD, 10, WEAK_BLACK);
        Paragraph p = new Paragraph("Relatório de clientes", font);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(0);
        p.setSpacingAfter(0);

        log.info("Título do PDF criado com sucesso");
        return p;
    }

    public Paragraph constroiParagrafoDataHora() {
        log.info("Método responsável por criar o subtítulo com data, hora e responsável do PDF acessado");

        log.info("Setando estilização do subtítulo e setando seu conteúdo...");
        String currentDateTime = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date());
        com.lowagie.text.Font font = FontFactory.getFont(FontFactory.COURIER, 8, WEAK_BLACK);
        Paragraph p = new Paragraph(currentDateTime, font);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(0);
        p.setSpacingAfter(0);

        log.info("Subtítulo do PDF criado com sucesso");
        return p;
    }

    public PdfPTable constroiTabelaInformativos(List<ClienteEntity> clientes) {
        log.info("Método responsável por criar os informativos do PDF acessado");

        log.info("Setando estilização dos informativos do PDF...");
        PdfPTable table;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{4f});
        table.setSpacingBefore(0);
        table.setSpacingAfter(0);

        Font font = FontFactory.getFont(FontFactory.COURIER, 7, STRONG_BLACK);

        PdfPCell cell = new PdfPCell();
        cell.setPadding(5);
        cell.setBackgroundColor(STRONG_COLOR);
        cell.setBorderColor(WEAK_BLACK);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);

        log.info("Setando conteúdo dos informativos do PDF...");
        cell.setPhrase(new Phrase("Total: " + clientes.size(), font));
        table.addCell(cell);

        log.info("Informativo do PDF criado com sucesso");
        return table;
    }

    public PdfPTable constroiTabelaObjetos(List<ClienteEntity> clientes) {
        log.info("Método responsável por criar a tabela do PDF acessado");

        log.info("Setando estilização da e tamanho da tabela do PDF...");
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1f, 3f, 3f, 1.5f, 1.5f});
        table.setSpacingBefore(5);

        log.info("Encaminhando para método de criação do header da tabela...");
        escreveHeaderTabela(table);

        log.info("Encaminhando para método de inscrição dos dados na tabela...");
        escreveDadosTabela(table, clientes);

        log.info("Tabela do PDF criada com sucesso");
        return table;
    }

}
