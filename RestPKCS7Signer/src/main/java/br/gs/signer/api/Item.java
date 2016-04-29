package br.gs.signer.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Item {

    private final String alias;
    private final String subject;
    private final String serialNumber;
    private final String initDate;
    private final String endDate;
    private final String issuer;

    public Item(String alias, String subject, String serialNumber, Date initDate, Date endDate, String issuer) {
        DateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        this.alias = alias;
        this.subject = this.corte(subject);
        this.serialNumber = serialNumber;
        this.initDate = f.format(initDate);
        this.endDate = f.format(endDate);
        this.issuer = this.corte(issuer);
    }

    public String getAlias() {
        return alias;
    }

    public String getSubject() {
        return subject;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getInitDate() {
        return initDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getIssuer() {
        return issuer;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(subject);
        return buffer.toString();
    }

    private String corte(String texto) {

        int end = 0;
        end = texto.indexOf(",");
        if (end == -1) {
            end = texto.length();
        }

        String cortado = texto.substring(0, end);
        return cortado.replace("CN=", "");
    }
}
