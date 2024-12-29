package com.pm.personnelmanagement.file.model;

import org.springframework.core.io.Resource;

public class FileResource {
    private Resource resource;
    private String fileId;

    public FileResource() {
    }

    public FileResource(Resource resource, String fileId) {
        this.resource = resource;
        this.fileId = fileId;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
