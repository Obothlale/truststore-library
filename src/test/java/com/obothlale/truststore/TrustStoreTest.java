package com.obothlale.truststore;

import com.obothlale.truststore.impl.TrustStoreImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.cert.Certificate;
import java.util.List;

public class TrustStoreTest {

    private TrustStore trustStore;

    @Before
    public void setup() {
        trustStore = new TrustStoreImpl();
    }

    @Test
    public void testCreate() {
        String trustStoreFile = "truststore.jks";
        trustStore.create("src/main/resources/" + trustStoreFile);
        File file = new File(getClass().getClassLoader().getResource(trustStoreFile).getFile());
        Assert.assertNotNull(file);
    }

    @Test
    public void testSaveCertificates() throws Exception {
        String trustStoreFile = "truststore.jks";
        byte[] previousFileBytes = getFileInBytes(trustStoreFile);

        String certificateFile = "BAGL Root CA 2.cer";
        String bag_root = "bag_root";
        trustStore.save("src/main/resources/" + certificateFile, bag_root, "src/main/resources/" + trustStoreFile);

        byte[] afterSaveFileBytes = getFileInBytes(trustStoreFile);
        Assert.assertNotEquals(previousFileBytes.length, afterSaveFileBytes.length);
    }

    private byte[] getFileInBytes(String trustStoreFile) throws IOException {
        return Files.readAllBytes((new File(getClass().getClassLoader().getResource(trustStoreFile).getFile())).toPath());
    }

    @Test
    public void testGetCerts() throws IOException {
        String trustStoreFile = "truststore.jks";
        List<Certificate> allCertificates = trustStore.getAllCertificates("src/main/resources/" + trustStoreFile);
        Assert.assertTrue(allCertificates.size() > 0);
        System.out.println(allCertificates);
    }

}
