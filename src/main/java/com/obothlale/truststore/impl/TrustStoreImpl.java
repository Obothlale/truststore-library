package com.obothlale.truststore.impl;

import com.obothlale.truststore.TrustStore;

import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class TrustStoreImpl implements TrustStore {

    private static String PASSWORD = "changeit";

    private KeyStore keyStore;

    public TrustStoreImpl() {
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException exception) {
            throw new RuntimeException("Could not get an instance of KeyStore", exception);
        }
    }

    public void create(String fileName) {
        try {
            char[] password = PASSWORD.toCharArray();
            keyStore.load(null, password);
            FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/truststore.jks");
            keyStore.store(fileOutputStream, password);
            fileOutputStream.close();
        } catch (Exception exception) {
            throw new RuntimeException("Could not create truststore", exception);
        }
    }

    public void save() {
        try {
            char[] password = PASSWORD.toCharArray();
            keyStore.load(new FileInputStream("src/main/resources/truststore.jks"), password);
            File certificateFile = new File(getClass().getClassLoader().getResource("BAGL Root CA 2.cer").getFile());
            Certificate certificate = getCertificateInstance(certificateFile.getName());
            keyStore.setCertificateEntry("bag_root", certificate);

            FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/truststore.jks");
            keyStore.store(fileOutputStream, password);
        } catch (Exception exception) {
            throw new RuntimeException("Could not create truststore", exception);
        }
    }

    public List<Certificate> getAllCertificates() {
        try {
            keyStore.load(new FileInputStream("src/main/resources/truststore.jks"), "changeit".toCharArray());
            Enumeration<String> aliases = keyStore.aliases();
            List<Certificate> certificates = new ArrayList<Certificate>();
            while (aliases.hasMoreElements()) {
                Certificate certificate = keyStore.getCertificate(aliases.nextElement());
                System.out.println(certificate);
                certificates.add(certificate);
            }
            return certificates;
        } catch (Exception exception) {
            throw new RuntimeException("could not get all certificates", exception);
        }
    }

    private Certificate getCertificateInstance(String fileName) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            InputStream certstream = fullStream(fileName);
            Certificate certificate = certificateFactory.generateCertificate(certstream);
            return certificate;
        } catch (Exception exception) {
            throw new RuntimeException("Unable to get certificate instance", exception);
        }
    }

    private static InputStream fullStream(String fname) {
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/BAGL Root CA 2.cer");
            DataInputStream dis = new DataInputStream(fis);
            byte[] bytes = new byte[dis.available()];
            dis.readFully(bytes);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            fis.close();
            dis.close();
            return bais;
        } catch (Exception exception) {
            throw new RuntimeException("Unable to get certificate full streams", exception);
        }
    }

}
