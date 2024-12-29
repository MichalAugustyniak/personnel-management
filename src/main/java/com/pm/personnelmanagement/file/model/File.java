package com.pm.personnelmanagement.file.model;

public class File {
    private FileInfo fileInfo;
    private FileResource fileResource;

    public File() {
    }

    public File(FileInfo fileInfo, FileResource fileResource) {
        this.fileInfo = fileInfo;
        this.fileResource = fileResource;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public FileResource getFileResource() {
        return fileResource;
    }

    public void setFileResource(FileResource fileResource) {
        this.fileResource = fileResource;
    }
}
