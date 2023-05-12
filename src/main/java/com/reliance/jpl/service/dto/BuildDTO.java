package com.reliance.jpl.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.reliance.jpl.domain.Build} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BuildDTO implements Serializable {

    private Long id;

    private String branch;

    private String tag;

    private Long createdBy;

    private LocalDate createdOn;

    private Long updatedBy;

    private LocalDate updatedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDate updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BuildDTO)) {
            return false;
        }

        BuildDTO buildDTO = (BuildDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, buildDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BuildDTO{" +
            "id=" + getId() +
            ", branch='" + getBranch() + "'" +
            ", tag='" + getTag() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedOn='" + getUpdatedOn() + "'" +
            "}";
    }
}
