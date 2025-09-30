package com.pg24.bidding.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment.storage")
public class StorageProperties {
    private String baseDir = "/tmp/payment-slips";

    public String getBaseDir() { return baseDir; }
    public void setBaseDir(String baseDir) { this.baseDir = baseDir; }
}
