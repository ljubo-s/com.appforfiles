package com.appforfiles.storage;

import org.springframework.stereotype.Component;

import lombok.Data;

import org.springframework.context.annotation.PropertySource;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Component
@ConfigurationProperties(prefix = "storage")
@PropertySource("classpath:storage.properties")
@Data
public class StorageProperties {

    private String invoiceCsvLocation;
    private String invoiceXlsxLocation;
    private String invoiceOrginalXlsxLocation;
    
    private String stornoCsvLocation;
    private String stornoXlsxLocation;
    private String stornoOrginalXlsxLocation;
    
    private String csvInvoicePrefix;
    private String csvStornoPrefix;
    
    private String dbInvoicePath;
    private String dbStornoPath;

}
