package br.gs.signer.api;


import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import javax.swing.table.AbstractTableModel;

public class CertListModel extends AbstractTableModel {

    private Object[][] dados;

    private final String[] columnNames = {"Emitido Para", "Número de série", "Válido de", "Válido até", "Emitido Por"};

    @Override
    public int getRowCount() {
        if (dados != null) {
            return dados.length;
        } else {
            return 0;
        }
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return dados[rowIndex][columnIndex];
    }

    public void populate(KeyStore keystore) {
        try {

            if (keystore != null) {
                int linha = keystore.size();
                int coluna = columnNames.length;

                dados = new Object[linha][coluna];

                int ik = 0;
                Enumeration<String> aliases = keystore.aliases();
                while (aliases.hasMoreElements()) {
                    String alias = aliases.nextElement();
                    X509Certificate certificate = (X509Certificate) keystore.getCertificate(alias);

                    Item item = new Item(alias, certificate.getSubjectDN().getName(), certificate.getSerialNumber().toString(), certificate.getNotBefore(), certificate.getNotAfter(), certificate.getIssuerDN().getName());
                    dados[ik][0] = item;
                    dados[ik][1] = item.getSerialNumber();
                    dados[ik][2] = item.getInitDate();
                    dados[ik][3] = item.getEndDate();
                    dados[ik][4] = item.getIssuer();
                    ik++;
                }
                fireTableDataChanged();
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

    }
}
