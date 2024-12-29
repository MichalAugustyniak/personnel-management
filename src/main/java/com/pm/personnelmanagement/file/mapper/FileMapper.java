package com.pm.personnelmanagement.file.mapper;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.pm.personnelmanagement.file.exception.NoSuchMetaDataField;
import com.pm.personnelmanagement.file.model.FileInfo;
import com.pm.personnelmanagement.file.model.FileMetaData;

import java.util.Optional;
import java.util.UUID;

public class FileMapper {
    public static FileInfo map(GridFSFile gridFSFile) {
        FileInfo fileInfo = new FileInfo();
        assert gridFSFile.getMetadata() != null;
        String contentType = Optional.ofNullable((String) gridFSFile.getMetadata().get(FileMetaData.CONTENT_TYPE.value()))
                .orElseThrow(() -> new NoSuchMetaDataField(String.format("No such meta data field %s", FileMetaData.CONTENT_TYPE.value())));
        String uuid = Optional.ofNullable((String) gridFSFile.getMetadata().get(FileMetaData.FILE_UUID.value()))
                .orElseThrow(() -> new NoSuchMetaDataField(String.format("No such meta data field %s", FileMetaData.FILE_UUID.value())));
        String userUUID = Optional.ofNullable((String) gridFSFile.getMetadata().get(FileMetaData.USER_UUID.value()))
                .orElseThrow(() -> new NoSuchMetaDataField(String.format("No such meta data field %s", FileMetaData.USER_UUID.value())));

        fileInfo.setContentType(contentType);
        fileInfo.setId(gridFSFile.getId().toString());  // may set bad value
        fileInfo.setFilename(fileInfo.getFilename());
        fileInfo.setUuid(UUID.fromString(uuid));
        fileInfo.setUserUUID(UUID.fromString(userUUID));
        return fileInfo;
    }
}
