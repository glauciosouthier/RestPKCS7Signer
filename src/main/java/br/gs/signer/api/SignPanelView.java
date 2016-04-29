package br.gs.signer.api;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.gov.frameworkdemoiselle.certificate.CertificateManager;
import br.gov.frameworkdemoiselle.policy.engine.factory.PolicyFactory;
import br.gov.frameworkdemoiselle.policy.engine.factory.PolicyFactory.Policies;

public abstract class SignPanelView extends JDialog {

	private static final long serialVersionUID = -4440444836057161131L;
	private JKeyStoreDialog keyStoreDialog;
	private JTable table = new JTable();
	private List<String> filesSelected = new ArrayList<String>(0);
	private String path = System.getProperty("user.home") + File.separator;
	private PolicyFactory.Policies policy = PolicyFactory.Policies.AD_RB_CADES_2_1;
	private List<String> files = new ArrayList<String>(0);

	public void init() {
		// new AssinadorConfig.getBundle("security-applet") ;
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if (SignerConfig.LOOK_AND_FEEL.getValue().equals(info.getName())) {
				try {
					UIManager.setLookAndFeel(info.getClassName());
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
				break;
			}
		}

		keyStoreDialog = new JKeyStoreDialog() {
			@Override
			protected void addComponent(JDialog dialog) {
				// makeComboFiles();
				makeComboPolicies(dialog);
				makeTableFiles(dialog);
			};
		};

		this.setSize(keyStoreDialog.getDimension());
		this.getRootPane().setDefaultButton(keyStoreDialog.getRunButton());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		keyStoreDialog.addButtonCancelActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelButton_actionPerformed();
			}
		});

		keyStoreDialog.addButtonRunActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				runButton_actionPerformed();
			}
		});

		keyStoreDialog.addScrollPaneLineKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				table_KeyPressed(e, keyStoreDialog.getTable());
			}
		});

		keyStoreDialog
				.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		keyStoreDialog.setComponentOrientation(this.getComponentOrientation());
		keyStoreDialog.setLocationRelativeTo(this);

		if (keyStoreDialog.getCertificatesCount() != 0) {
			keyStoreDialog.setVisible(true);
		}

	}

	protected void makeTableFiles(JDialog dialog) {

		FilesModel model = new FilesModel();
		model.populate(getFiles());
		table.setModel(model);
		table.getTableHeader().setFont(
				new Font(SignerConfig.CONFIG_DIALOG_FONT.getValue(),
						SignerConfig.CONFIG_DIALOG_FONT_STYLE.getValueInt(),
						SignerConfig.CONFIG_DIALOG_FONT_SIZE.getValueInt()));
		table.setFont(new Font(SignerConfig.CONFIG_DIALOG_FONT.getValue(),
				SignerConfig.CONFIG_DIALOG_FONT_STYLE.getValueInt(),
				SignerConfig.CONFIG_DIALOG_FONT_SIZE.getValueInt()));

		if (table.getRowCount() != 0) {
			table.setRowSelectionInterval(0, table.getRowCount() - 1);
		}
		ListSelectionModel cellSelectionModel = table.getSelectionModel();
		cellSelectionModel
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		cellSelectionModel
				.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						String selectedData = null;

						int[] selectedRow = table.getSelectedRows();
						int[] selectedColumns = table.getSelectedColumns();

						for (int i = 0; i < selectedRow.length; i++) {
							selectedData = (String) table.getValueAt(
									selectedRow[i], selectedColumns[0]);
							selectedData = getPath() + File.separator
									+ selectedData;
							if (!getFilesSelected().contains(selectedData)) {
								getFilesSelected().add(selectedData);
								System.out.println("Selected: " + selectedData);
							}
						}

					}

				});
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		dialog.add(scrollPane, null);
		dialog.repaint();
	}

	protected void makeComboPolicies(JDialog dialog) {
		final JComboBox<PolicyFactory.Policies> combo = new JComboBox<PolicyFactory.Policies>(
				getPolicyList());
		try {
			if (getTemporalPolicyList() != null && isACTenabled()) {
				for (Policies p : getTemporalPolicyList()) {
					combo.addItem(p);
				}
			}
		} catch (Throwable e) {
			throw new RuntimeException(
					"Timestamping Authority (TSA) server not configured.");
		}

		combo.setSelectedItem(getPolicy());
		combo.setFont(new Font(SignerConfig.CONFIG_DIALOG_FONT.getValue(),
				SignerConfig.CONFIG_DIALOG_FONT_STYLE.getValueInt(),
				SignerConfig.CONFIG_DIALOG_FONT_SIZE.getValueInt()));
		combo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setPolicy((Policies) combo.getSelectedItem());
			}

		});
		dialog.add(combo, null);
		dialog.repaint();
	}

	protected boolean isACTenabled() {
		return getClass().getResourceAsStream("/timestamp-config.properties") != null;
	}

	protected void makeComboFiles(JDialog dialog) {

		final JComboBox<String> combo = new JComboBox<String>();
		combo.addItem("");
		for (String f : getFiles()) {
			combo.addItem(f);
		}
		combo.setFont(new Font(SignerConfig.CONFIG_DIALOG_FONT.getValue(),
				SignerConfig.CONFIG_DIALOG_FONT_STYLE.getValueInt(),
				SignerConfig.CONFIG_DIALOG_FONT_SIZE.getValueInt()));
		combo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setFilesSelected(combo.getSelectedObjects());
			}
		});

		dialog.add(combo, null);
		dialog.repaint();
	}

	protected void table_KeyPressed(KeyEvent e, JTable table) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_TAB: // se a tecla pressionada for tab
			int rowCount = table.getRowCount();
			int selectedRow = table.getSelectedRow();

			if (selectedRow == rowCount - 1) {
				table.requestFocus();
				table.changeSelection(0, 0, false, false);
			} else {
				table.requestFocus();
				table.changeSelection(selectedRow + 1, 0, false, false);
			}
			break;
		}
	}

	/**
	 * Chamado ao clique do botao Ok
	 */
	protected void runButton_actionPerformed() {

		try {
			KeyStore keystore = keyStoreDialog.getKeyStore();
			String alias = keyStoreDialog.getAlias();
			execute(keystore, alias);
		} catch (Throwable e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					SignerConfig.MESSAGE_ERROR_UNEXPECTED.getValue() + " - "
							+ e.getMessage(),
					SignerConfig.LABEL_DIALOG_OPTION_PANE_TITLE.getValue(),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Chamado ao clique do botao Cancelar
	 */
	protected void cancelButton_actionPerformed() {
		this.setVisible(false);
		KeyStore keystore = keyStoreDialog.getKeyStore();
		String alias = keyStoreDialog.getAlias();
		keyStoreDialog.setVisible(false);
		keyStoreDialog = null;
		cancel(keystore, alias);
	}

	public ICPBrasilCertificate getICPBrasilCertificate(KeyStore keystore,
			String alias, boolean isValid) throws KeyStoreException {
		if (alias == null || alias.isEmpty()) {
			alias = keystore.aliases().nextElement();
		}
		X509Certificate x509 = (X509Certificate) keystore
				.getCertificateChain(alias)[0];
		CertificateManager cm = new CertificateManager(x509, isValid);
		ICPBrasilCertificate cert = cm.load(ICPBrasilCertificate.class);
		return cert;
	}

	protected void setPolicy(Policies combo) {
		policy = combo;
	}

	public Policies getPolicy() {
		return policy;
	}

	protected List<String> getFiles() {
		return files;
	}

	protected void setFiles(List<String> files) {
		this.files = files;

	}

	public abstract void execute(KeyStore keystore, String alias);

	public abstract void cancel(KeyStore keystore, String alias);

	protected abstract Policies[] getPolicyList();

	protected abstract Policies[] getTemporalPolicyList();

	protected JDialog getDialog() {
		return keyStoreDialog;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setFilesSelected(Object[] objs) {
		filesSelected.clear();
		for (Object o : objs) {
			String file = getPath() + File.separator + (String) o;
			filesSelected.add(file);
			System.out.println(file);
		}
	}

	public List<String> getFilesSelected() {
		return filesSelected;
	}

	protected void showInfoMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Mensagem",
				JOptionPane.INFORMATION_MESSAGE);
	}

	protected void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Error",
				JOptionPane.ERROR_MESSAGE);
	}
}
