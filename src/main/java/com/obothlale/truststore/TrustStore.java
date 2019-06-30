package com.obothlale.truststore;

import java.security.cert.Certificate;
import java.util.List;

public interface TrustStore {
    void create(String trustStoreFilePath);
    void save(String certificateName, String certificateAlias, String trustStoreFilePath);
    List<Certificate> getAllCertificates(String trustStoreFilePath);
}
