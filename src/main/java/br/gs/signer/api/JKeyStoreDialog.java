package br.gs.signer.api;


import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.security.KeyStore;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import br.gov.frameworkdemoiselle.certificate.exception.CertificateValidatorException;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.DriverNotAvailableException;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.InvalidPinException;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.KeyStoreLoader;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.KeyStoreLoaderException;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.PKCS11NotFoundException;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.factory.KeyStoreLoaderFactory;

/**
 * JDialog especializado para obtencao do KeyStore de um dispositivo usb ou
 * smartcard.
 *
 * @author SUPST/STCTA
 *
 */
public class JKeyStoreDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private final JLabel certificatesLabel = new JLabel();
    private final JScrollPane scrollPane = new JScrollPane();
    private final JButton runButton = new JButton();
    private final JButton cancelButton = new JButton();
    private final JTable table = new JTable();
    private KeyStore keystore = null;
    private CertListModel listaCertificadosModel = null;
    private boolean loaded = false;

    /**
     * Construtor. Aciona a inicializacao dos demais componentes
     */
    public JKeyStoreDialog() {
        init();
    }

    /**
     * Indica se o keystore foi carregado com sucesso.
     *
     * @return True, se for carregado com sucesso. False se contrario.
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Inicializacao dos componentes
     */
    private void init() {
        mountGUI();
    }

    private void mountGUI() {

        try {
            this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
            this.setSize(getDimension());

            // Label da tabela de certificados
            certificatesLabel.setText(SignerConfig.CONFIG_DIALOG_LABEL_TABLE.getValue());
            Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
            TitledBorder title = BorderFactory.createTitledBorder(loweredetched, certificatesLabel.getText());
            title.setTitleJustification(TitledBorder.CENTER);
            title.setTitleFont(new Font(SignerConfig.CONFIG_DIALOG_FONT.getValue(), SignerConfig.CONFIG_DIALOG_FONT_STYLE.getValueInt(), SignerConfig.CONFIG_DIALOG_FONT_SIZE.getValueInt()));

            // Configura a Tabela de Certificados
            listaCertificadosModel = new CertListModel();
            listaCertificadosModel.populate(this.getKeyStore());
            table.setModel(listaCertificadosModel);

            if (table.getRowCount() == 0) {
                runButton.setEnabled(false);
            } else {
                table.setRowSelectionInterval(0, 0);
            }

            table.getTableHeader().setFont(new Font(SignerConfig.CONFIG_DIALOG_FONT.getValue(), SignerConfig.CONFIG_DIALOG_FONT_STYLE.getValueInt(), SignerConfig.CONFIG_DIALOG_FONT_SIZE.getValueInt()));
            table.setFont(new Font(SignerConfig.CONFIG_DIALOG_FONT.getValue(), SignerConfig.CONFIG_DIALOG_FONT_STYLE.getValueInt(), SignerConfig.CONFIG_DIALOG_FONT_SIZE.getValueInt()));
            table.setBounds(SignerConfig.CONFIG_DIALOG_TABLE_CERTIFICATES_X.getValueInt(), SignerConfig.CONFIG_DIALOG_TABLE_CERTIFICATES_Y.getValueInt(), SignerConfig.CONFIG_DIALOG_TABLE_CERTIFICATES_WIDTH.getValueInt(), SignerConfig.CONFIG_DIALOG_TABLE_CERTIFICATES_HEIGHT.getValueInt());
            table.setMinimumSize(new Dimension(SignerConfig.CONFIG_DIALOG_TABLE_CERTIFICATES_WIDTH.getValueInt(), SignerConfig.CONFIG_DIALOG_TABLE_CERTIFICATES_HEIGHT.getValueInt()));
            table.setRowHeight(SignerConfig.CONFIG_DIALOG_TABLE_CERTIFICATES_ROW_HEIGHT.getValueInt());
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            // Dimensiona cada coluna separadamente
            TableColumn tc1 = table.getColumnModel().getColumn(0);
            tc1.setPreferredWidth(200);

            TableColumn tc2 = table.getColumnModel().getColumn(1);
            tc2.setPreferredWidth(140);

            TableColumn tc3 = table.getColumnModel().getColumn(2);
            tc3.setPreferredWidth(140);

            TableColumn tc4 = table.getColumnModel().getColumn(3);
            tc4.setPreferredWidth(250);

            // Configura o Painel
            scrollPane.setBounds(SignerConfig.CONFIG_DIALOG_TABLE_CERTIFICATES_X.getValueInt(), SignerConfig.CONFIG_DIALOG_TABLE_CERTIFICATES_Y.getValueInt(), SignerConfig.CONFIG_DIALOG_TABLE_CERTIFICATES_WIDTH.getValueInt(), SignerConfig.CONFIG_DIALOG_TABLE_CERTIFICATES_HEIGHT.getValueInt());
            scrollPane.setViewportView(table);

            // botao Run
            runButton.setText(SignerConfig.LABEL_DIALOG_BUTTON_RUN.getValue());
            runButton.setFont(new Font(SignerConfig.CONFIG_DIALOG_FONT.getValue(), SignerConfig.CONFIG_DIALOG_FONT_STYLE.getValueInt(), SignerConfig.CONFIG_DIALOG_FONT_SIZE.getValueInt()));
            runButton.setBounds(new Rectangle(SignerConfig.CONFIG_DIALOG_BUTTON_RUN_X.getValueInt(), SignerConfig.CONFIG_DIALOG_BUTTON_RUN_Y.getValueInt(), SignerConfig.CONFIG_DIALOG_BUTTON_RUN_WIDTH.getValueInt(), SignerConfig.CONFIG_DIALOG_BUTTON_RUN_HEIGHT.getValueInt()));

            // botao Cancel
            cancelButton.setText(SignerConfig.LABEL_DIALOG_BUTTON_CANCEL.getValue());
            cancelButton.setFont(new Font(SignerConfig.CONFIG_DIALOG_FONT.getValue(), SignerConfig.CONFIG_DIALOG_FONT_STYLE.getValueInt(), SignerConfig.CONFIG_DIALOG_FONT_SIZE.getValueInt()));
            cancelButton.setBounds(new Rectangle(SignerConfig.CONFIG_DIALOG_BUTTON_CANCEL_X.getValueInt(), SignerConfig.CONFIG_DIALOG_BUTTON_CANCEL_Y.getValueInt(), SignerConfig.CONFIG_DIALOG_BUTTON_CANCEL_WIDTH.getValueInt(), SignerConfig.CONFIG_DIALOG_BUTTON_CANCEL_HEIGHT.getValueInt()));

            this.add(scrollPane, null);
            addComponent(this);
            JPanel buttons=new JPanel();
            buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
            buttons.add(runButton, null);
            buttons.add(cancelButton, null);
            this.add(buttons, null);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    protected void addComponent(JDialog dialog) {
			
	}

	/**
     * Permite acesso ao objeto Table contendo a lista de certificados digitais
     *
     * @return A lista de certificados digitais
     */
    public JTable getTable() {
        return this.table;
    }

    /**
     *
     * @param key
     */
    public void addScrollPaneLineKeyListener(KeyListener key) {
        table.addKeyListener(key);
    }

    /**
     * Adicionar um ActionListener ao botao "Run"
     *
     * @param action ActionListener
     */
    public void addButtonRunActionListener(ActionListener action) {
        runButton.addActionListener(action);
    }

    /**
     * Adicionar um ActionListener ao botao "Cancel"
     *
     * @param action ActionListener
     */
    public void addButtonCancelActionListener(ActionListener action) {
        cancelButton.addActionListener(action);
    }

    /**
     * Retorna o keystore do dispositivo a partir do valor de pin
     */
    public KeyStore getKeyStore() {
        try {
            Cursor hourGlassCursor = new Cursor(Cursor.WAIT_CURSOR);
            setCursor(hourGlassCursor);
            KeyStoreLoader loader = KeyStoreLoaderFactory.factoryKeyStoreLoader();
            loader.setCallbackHandler(new PinCallbackHandler());
            keystore = loader.getKeyStore();
            loaded = true;
            return keystore;
        } catch (DriverNotAvailableException e) {
            showError(SignerConfig.MESSAGE_ERROR_DRIVER_NOT_AVAILABLE.getValue());
        } catch (PKCS11NotFoundException e) {
            showError(SignerConfig.MESSAGE_ERROR_PKCS11_NOT_FOUND.getValue());
        } catch (CertificateValidatorException e) {
            showError(SignerConfig.MESSAGE_ERROR_LOAD_TOKEN.getValue());
        } catch (InvalidPinException e) {
            showError(SignerConfig.MESSAGE_ERROR_INVALID_PIN.getValue());
        } catch (KeyStoreLoaderException ke) {
            showError(ke.getMessage());
        } catch (Exception ex) {
            showError(SignerConfig.MESSAGE_ERROR_UNEXPECTED.getValue());
        } finally {
            Cursor hourGlassCursos = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(hourGlassCursos);
        }
        return null;
    }

    /**
     * Retorna o alias
     *
     * @return
     */
    public String getAlias() {
        if (table.getModel().getRowCount() != 0) {
            int row = table.getSelectedRow();
            Item item = (Item) table.getModel().getValueAt(row, 0);
            return item.getAlias();
        } else {
            return "";
        }
    }

    /**
     * Exibe as mensagens de erro
     *
     * @param message
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, SignerConfig.LABEL_DIALOG_OPTION_PANE_TITLE.getValue(), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Retorna o botao run
     *
     * @return
     */
    public JButton getRunButton() {
        return runButton;
    }

    /**
     * Retorna as dimensoes padroes do panel
     *
     * @return
     */
    public Dimension getDimension() {
        return new Dimension(SignerConfig.CONFIG_DIALOG_DIMENSION_WIDTH.getValueInt(), SignerConfig.CONFIG_DIALOG_DIMENSION_HEIGHT.getValueInt());
    }

    public int getCertificatesCount() {
        return table.getRowCount();
    }
}
