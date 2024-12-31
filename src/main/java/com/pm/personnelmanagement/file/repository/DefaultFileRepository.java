package com.pm.personnelmanagement.file.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.pm.personnelmanagement.file.dto.SaveFileRepositoryRequest;
import com.pm.personnelmanagement.file.mapper.FileMapper;
import com.pm.personnelmanagement.file.model.File;
import com.pm.personnelmanagement.file.model.FileMetaData;
import com.pm.personnelmanagement.file.model.FileResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DefaultFileRepository implements FileRepository {
    private final GridFsTemplate gridFsTemplate;
    private final GridFSBucket gridFSBucket;

    public DefaultFileRepository(GridFsTemplate gridFsTemplate, MongoTemplate mongoTemplate) {
        this.gridFsTemplate = gridFsTemplate;
        this.gridFSBucket = GridFSBuckets.create(mongoTemplate.getDb());
    }

    @Override
    public void save(SaveFileRepositoryRequest request) {
        System.out.println("Trying to save the file...");
        System.out.println("with " + request);
        DBObject metaData = new BasicDBObject();
        metaData.put(FileMetaData.FILE_UUID.value(), request.fileUUID().toString());
        metaData.put(FileMetaData.USER_UUID.value(), request.saveFileRequest().userUUID().toString());
        gridFsTemplate.store(
                request.saveFileRequest().inputStream(),
                request.saveFileRequest().filename(),
                request.saveFileRequest().mediaType().toString(),
                metaData
        );
    }

    @Override
    public void deleteByUUID(UUID uuid) {
        Query query = new Query(Criteria.where(FileMetaData.FILE_UUID.value()).is(uuid.toString()));
        gridFsTemplate.delete(query);
    }

    @Override
    public Optional<File> findByUUID(UUID uuid) {
        Query query = new Query(Criteria.where("metadata." + FileMetaData.FILE_UUID.value()).is(uuid.toString()));
        GridFSFile gridFSFile = gridFsTemplate.findOne(query);
        if (gridFSFile == null) {
            return Optional.ofNullable(null);
        }
        InputStream inputStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        Resource resource = new InputStreamResource(inputStream);
        FileResource fileResource = new FileResource(resource, gridFSFile.getObjectId().toString());
        System.out.println("Found the file");
        return Optional.of(new File(FileMapper.map(gridFSFile), fileResource));
    }

    @Override
    public Page<File> findAll() {
        return null;
    }
}
