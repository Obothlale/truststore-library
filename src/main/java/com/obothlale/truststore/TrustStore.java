package com.obothlale.truststore;

import java.security.cert.Certificate;
import java.util.List;

public interface TrustStore {
    void create(String fileName);
    void save();
    List<Certificate> getAllCertificates();
}
