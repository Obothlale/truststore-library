package com.obothlale.truststore;

import com.obothlale.truststore.impl.TrustStoreImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.security.cert.Certificate;
import java.util.List;

public class TrustStoreTest {

    private TrustStore trustStore;

    @Before
    public void setup() {
        trustStore = new TrustStoreImpl();
        String trustStoreFile = "truststore.jks";
        trustStore.create("src/main/resources/" + trustStoreFile);
    }

    @Test
    public void testCreateTrustStore() {
        String trustStoreFile = "truststore.jks";
        File file = new File(getClass().getClassLoader().getResource(trustStoreFile).getFile());
        Assert.assertNotNull(file);
    }

    @Test
    public void testSaveCertificatesInTrustStore() {
        String trustStoreFile = "truststore.jks";
        String certificateFile = "BAGL Root CA 2.cer";
        String alias = "bag_root";
        List<Certificate> certificates = trustStore.getAllCertificates("src/main/resources/" + trustStoreFile);

        Assert.assertFalse(certificates.size() > 0);
        trustStore.save("src/main/resources/" + certificateFile, alias, "src/main/resources/" + trustStoreFile);
        certificates = trustStore.getAllCertificates("src/main/resources/" + trustStoreFile);
        Assert.assertTrue(certificates.size() > 0);
    }
}
