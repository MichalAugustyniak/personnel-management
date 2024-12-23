package com.pm.personnelmanagement.security.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

@Entity
@Table(name = "global_permissons")
public class GlobalPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(Types.VARCHAR)
    private String resourceUUID;

    private Boolean canRead;
    private Boolean canWrite;
    private Boolean canEdit;
    private Boolean canDelete;

    @Override
    public String toString() {
        return "GlobalPermission{" +
                "id=" + id +
                ", resourceUUID='" + resourceUUID + '\'' +
                ", canRead=" + canRead +
                ", canWrite=" + canWrite +
                ", canEdit=" + canEdit +
                ", canDelete=" + canDelete +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceUUID() {
        return resourceUUID;
    }

    public void setResourceUUID(String resourceUUID) {
        this.resourceUUID = resourceUUID;
    }

    public Boolean getCanRead() {
        return canRead;
    }

    public void setCanRead(Boolean canRead) {
        this.canRead = canRead;
    }

    public Boolean getCanWrite() {
        return canWrite;
    }

    public void setCanWrite(Boolean canWrite) {
        this.canWrite = canWrite;
    }

    public Boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }
}
