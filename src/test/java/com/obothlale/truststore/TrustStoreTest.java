package com.obothlale.truststore;

import com.obothlale.truststore.impl.TrustStoreImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
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
        String fileName = "truststore.jks";
        trustStore.create(fileName);
        File file = new File(getClass().getClassLoader().getResource(fileName).getFile());
        Assert.assertNotNull(file);
    }

    @Test
    public void testSaveCertificates() {
        trustStore.save();
    }

    @Test
    public void testGetCerts() throws IOException {
        List<Certificate> allCertificates = trustStore.getAllCertificates();
        Assert.assertTrue(allCertificates.size() > 0);
    }

}
