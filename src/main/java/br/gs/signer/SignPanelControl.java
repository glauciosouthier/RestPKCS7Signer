package br.gs.signer;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.AuthProvider;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.security.auth.login.LoginException;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.frameworkdemoiselle.certificate.keystore.loader.configuration.Configuration;
import br.gov.frameworkdemoiselle.certificate.signer.factory.PKCS7Factory;
import br.gov.frameworkdemoiselle.certificate.signer.pkcs7.PKCS7Signer;
import br.gov.frameworkdemoiselle.policy.engine.factory.PolicyFactory;
import br.gov.frameworkdemoiselle.policy.engine.factory.PolicyFactory.Policies;
import br.gs.signer.api.Driver;
import br.gs.signer.api.ICPBrasilCertificate;
import br.gs.signer.api.SignPanelView;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;

public class SignPanelControl extends SignPanelView {
	private static final Logger logger = LoggerFactory
			.getLogger(SignPanelControl.class);
	private static final String FILE_PATTERN = ".*\\.(pdf|doc|txt)";
	private static final String USER_CONFIG_FILE = "signer.properties";
	private static final long serialVersionUID = -7511014972803925792L;
	private Properties prop;
	private List<String> filesSigned = new ArrayList<String>(0);
	private Boolean finalized = false;

	public void init() {
		loadDrivers();
		if (!(new File(getPath()).exists())) {
			new File(getPath()).mkdirs();
		}
		if (getFiles() == null || getFiles().isEmpty()) {
			setFiles(loadFileNames(getPath(), FILE_PATTERN));
		}
		super.init();
	}

	public SignPanelControl() {
		init();
	}

	public SignPanelControl(List<String> files) {
		for (String f : files) {
			if (f != null && new File(f).exists()) {
				if (f.contains(File.separator)) {
					if (!f.substring(0, f.lastIndexOf(File.separator) + 1)
							.equals(getPath())) {
						setPath(f.substring(0,
								f.lastIndexOf(File.separator) + 1));
					}
					getFiles().add(f.replaceFirst(getPath(), ""));
				} else {
					getFiles().add(f);
				}
			}
		}
		init();
	}

	public SignPanelControl(String path, String pattern) {
		File directory = new File(path);
		if (directory.exists() && directory.isDirectory()) {
			setFiles(loadFileNames(path, ".*\\" + pattern));
		}
		init();
	}

	protected void loadDrivers() {
		// Configuration.getInstance().addDriver("SafeNet",
		// "/usr/lib64/libeToken.so");
		String userHome = System.getProperty("user.home");
		if (new File(userHome + File.separator + USER_CONFIG_FILE).exists()) {
			prop = new Properties();
			try {
				prop.load(new FileInputStream(userHome + File.separator
						+ USER_CONFIG_FILE));
				Genson genson = new Genson();
				@SuppressWarnings("unchecked")
				List<Driver> drivers = genson.deserialize(
						new StringReader(prop.getProperty("config.driver")),
						new GenericType<List<Driver>>() {
						});
				for (Driver driver : drivers) {
					Configuration.getInstance().addDriver(driver.getName(),
							driver.getPath());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	protected Policies[] getPolicyList() {
		return new Policies[] { PolicyFactory.Policies.AD_RB_CADES_2_0,
				PolicyFactory.Policies.AD_RB_CADES_2_1, };
	}

	protected Policies[] getTemporalPolicyList() {
		return new Policies[] { PolicyFactory.Policies.AD_RA_CADES_2_0,
				PolicyFactory.Policies.AD_RA_CADES_2_1,
				PolicyFactory.Policies.AD_RA_CADES_2_2,
				PolicyFactory.Policies.AD_RC_CADES_2_0,
				PolicyFactory.Policies.AD_RC_CADES_2_1,
				PolicyFactory.Policies.AD_RT_CADES_2_0,
				PolicyFactory.Policies.AD_RT_CADES_2_0,
				PolicyFactory.Policies.AD_RT_CADES_2_1,
				PolicyFactory.Policies.AD_RV_CADES_2_0,
				PolicyFactory.Policies.AD_RV_CADES_2_1 };
	}

	protected List<File> findFiles(final File startingDirectory,
			final String pattern) {
		List<File> files = new ArrayList<File>();
		if (startingDirectory.isDirectory()) {
			File[] sub = startingDirectory.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					// load sub-folders
					if (isLoadSubFolders()) {
						return pathname.isDirectory()
								|| pathname.getName().matches(pattern);
					} else {
						return pathname.getName().matches(pattern);
					}
				}
			});
			for (File fileDir : sub) {
				if (fileDir.isDirectory() && isLoadSubFolders()) {
					// load sub-folders
					files.addAll(findFiles(fileDir, pattern));
				} else {
					files.add(fileDir);
				}
			}
		}
		return files;
	}

	protected boolean isLoadSubFolders() {
		return false;
	}

	protected List<String> loadFileNames(String folder, String pattern) {
		List<String> result = new ArrayList<String>(0);
		List<File> lt = findFiles(new File(folder), pattern);
		for (Object obj7 : lt) {
			File ie = (File) obj7;
			result.add(ie.getName().toString());
		}
		return result;
	}

	@Override
	public void execute(KeyStore keystore, String alias) {
		try {
			if (getFilesSelected().isEmpty()) {
				showErrorMessage("Por favor, escolha um documento para assinar");
				return;
			}
			getFilesSigned().clear();
			/* Parametrizando o objeto doSign */
			PKCS7Signer signer = PKCS7Factory.getInstance().factoryDefault();
			signer.setCertificates(keystore.getCertificateChain(alias));
			signer.setPrivateKey((PrivateKey) keystore.getKey(alias, null));
			signer.setSignaturePolicy(getPolicy());

			signer.setAttached(true);
			logger.info("Efetuando a  assinatura do conteudo");
			for (String file : getFilesSelected()) {
				byte[] content = readContent(new File(file).getAbsolutePath());
				byte[] signed = signer.doSign(content);
				writeContent(signed, file.concat(".p7s"));
				logger.info("Efetuando a validacao da assinatura.");
				boolean checked = signer.check(null, signed);
				if (checked) {
					logger.info("A assinatura e valida.");
					getFilesSigned().add(new File(file.concat(".p7s")).getAbsolutePath());
				} else {
					logger.info("A assinatura nao e valida!");
				}
			}
			String.format("Os arquivos {0}  foram assinados ",
					dif(getFilesSelected(), getFilesSigned()));
			if (getFilesSelected().size() == getFilesSigned().size()) {
				showInfoMessage("Os arquivos selecionados foram assinados e validados com sucesso.");
			} else {
				showErrorMessage(String.format(
						"Os arquivos {0} não foram assinados ",
						dif(getFilesSelected(), getFilesSigned())));
			}
			ICPBrasilCertificate certificado = getICPBrasilCertificate(
					keystore, alias, false);

		} catch (KeyStoreException | UnrecoverableKeyException
				| NoSuchAlgorithmException e) {
			showErrorMessage(e.getMessage());

		} finally {
			logout(keystore);
			destroy();
		}

	}



	protected byte[] readContent(String arquivo) {
		byte[] result = null;
		try {
			File file = new File(arquivo);
			FileInputStream is = new FileInputStream(file);
			result = new byte[(int) file.length()];
			is.read(result);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	protected void writeContent(byte[] conteudo, String arquivo) {
		try {
			File file = new File(arquivo);
			FileOutputStream os = new FileOutputStream(file);
			os.write(conteudo);
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected List<String> dif(List<String> filesSelected,
			List<String> filesSigned2) {
		List<String> out = new ArrayList<String>();
		for (String sel : filesSelected) {
			if (!filesSigned2.contains(sel.concat(".p7s"))) {
				out.add(sel);
			}
		}
		return out;
	}

	@Override
	public void cancel(KeyStore keystore, String alias) {
		logout(keystore);
		destroy();
	}

	public void destroy() {
		setFinalized(true);
		getDialog().setVisible(false);
		getDialog().dispose();
	}

	protected void logout(KeyStore keystore) {
		logger.info("Logout on provider.");
		AuthProvider ap = null;
		if (keystore != null) {
			ap = (AuthProvider) keystore.getProvider();
		}
		if (ap != null) {
			try {
				ap.logout();
			} catch (LoginException e) {
				showErrorMessage(e.getMessage());
			}

		}
	}

	public List<String> getFilesSigned() {
		return filesSigned;
	}

	public void setFilesSigned(List<String> filesSigned) {
		this.filesSigned = filesSigned;
	}

	public Boolean getFinalized() {
		return finalized;
	}

	public void setFinalized(Boolean finalized) {
		this.finalized = finalized;
	}

}
