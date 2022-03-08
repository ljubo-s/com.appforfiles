package com.appforfiles.storage;

public enum FilesInfo {
    
    INVOICE("invoice"),
    STORNO("storno");
    
    private String value;

    public String getValue() {
        return this.value;
    }

    private FilesInfo(String value) {
        this.value = value;
    }
}
