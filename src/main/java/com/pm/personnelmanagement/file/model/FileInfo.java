package com.pm.personnelmanagement.file.model;

import java.util.UUID;

public class FileInfo {
    private String contentType;
    private String id;
    private String filename;
    private UUID uuid;
    private UUID userUUID;

    public FileInfo() {
    }

    public FileInfo(String contentType, String id, UUID uuid, UUID userUUID) {
        this.contentType = contentType;
        this.id = id;
        this.uuid = uuid;
        this.userUUID = userUUID;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }
}
