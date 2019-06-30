package com.obothlale.truststore.impl;

import com.obothlale.truststore.TrustStore;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class TrustStoreImpl implements TrustStore {

    private static String KEY_STORE_TYPE = "PKCS12";
    private static String PASSWORD = "changeit";

    private KeyStore keyStore;

    public TrustStoreImpl() {
        try {
            this.keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
        } catch (KeyStoreException exception) {
            throw new RuntimeException("Could not get an instance of KeyStore", exception);
        }
    }

    public void create(String trustStoreFilePath) {
        try {
            loadTrustStore(null);
            saveTrustStore(trustStoreFilePath);
        } catch (Exception exception) {
            throw new RuntimeException("Could not create truststore", exception);
        }
    }

    private void loadTrustStore(String trustStoreFilePath) {
        try {
            if (trustStoreFilePath == null) {
                this.keyStore.load(null, PASSWORD.toCharArray());
                return;
            }
            FileInputStream trustStoreFileStream = new FileInputStream(trustStoreFilePath);
            this.keyStore.load(trustStoreFileStream, PASSWORD.toCharArray());
            trustStoreFileStream.close();
        } catch (Exception exception) {
            throw new RuntimeException("Unable to load truststore", exception);
        }
    }

    public void save(String certificateFilePath, String certificateAlias, String trustStoreFilePath) {
        loadTrustStore(trustStoreFilePath);
        try {
            Certificate certificate = getCertificateInstance(certificateFilePath);
            this.keyStore.setCertificateEntry(certificateAlias, certificate);
            saveTrustStore(trustStoreFilePath);
        } catch (Exception exception) {
            throw new RuntimeException("Could not create truststore", exception);
        }
    }

    private void saveTrustStore(String trustStoreFilePath) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        FileOutputStream trustStoreOutputStream = new FileOutputStream(trustStoreFilePath);
        this.keyStore.store(trustStoreOutputStream, PASSWORD.toCharArray());
        trustStoreOutputStream.close();
    }

    public List<Certificate> getAllCertificates(String trustStoreFilePath) {
        loadTrustStore(trustStoreFilePath);
        try {
            List<Certificate> certificates = new ArrayList<Certificate>();
            Enumeration<String> aliases = this.keyStore.aliases();
            while (aliases.hasMoreElements()) {
                certificates.add(this.keyStore.getCertificate(aliases.nextElement()));
            }
            return certificates;
        } catch (Exception exception) {
            throw new RuntimeException("could not get all certificates", exception);
        }
    }


    private Certificate getCertificateInstance(String certificateFilePath) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            return certificateFactory.generateCertificate(new FileInputStream(certificateFilePath));
        } catch (Exception exception) {
            throw new RuntimeException("Unable to get certificate instance", exception);
        }
    }
}
