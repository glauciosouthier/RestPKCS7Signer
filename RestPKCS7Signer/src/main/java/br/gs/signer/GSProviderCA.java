package br.gs.signer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import br.gov.frameworkdemoiselle.certificate.ca.provider.impl.ICPBrasilProviderCA;

public class GSProviderCA extends ICPBrasilProviderCA {
	private static GSProviderCA instance = new GSProviderCA();
	private KeyStore keyStore = null;

	public static GSProviderCA getInstance() {
		return GSProviderCA.instance;
	}

	public KeyStore getKeyStore() {
		if (this.keyStore != null)
			return this.keyStore;
		try {
			String filename = System.getProperty("java.home")
					+ "/lib/security/cacerts".replace('/', File.separatorChar);
			InputStream is = new FileInputStream(filename);
			this.keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			this.keyStore.load(is, "changeit".toCharArray());
		} catch (Throwable error) {
			throw new ICPBrasilProviderCAException(
					"KeyStore default not loaded.", error);
		}
		return this.keyStore;
	}

	public void setKeyStore(KeyStore ks) {
		this.keyStore = ks;
	}

	public void setCertificateChain(Certificate[] chain)
			throws KeyStoreException {
		KeyStore ks = getKeyStore();
		int i = 0;
		for (Certificate cert : chain) {
			if (!isSelfSigned(cert)) {
				// implementar....
				ks.setCertificateEntry("" + (++i), cert);
			}
		}
		setKeyStore(ks);
	}

	public static boolean isSelfSigned(Certificate cert) {
		try {
			// Try to verify certificate signature with its own public key
			PublicKey key = cert.getPublicKey();
			cert.verify(key);
			return true;
		} catch (SignatureException sigEx) {
			// Invalid signature --> not self-signed
		} catch (InvalidKeyException keyEx) {
			// Invalid key --> not self-signed
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
		return false;
	}
}
