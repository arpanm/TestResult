package com.reliance.jpl.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.reliance.jpl.domain.TestResult} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestResultDTO implements Serializable {

    private Long id;

    private Float pageLoad;

    private Float timeToInteractive;

    private Long createdBy;

    private LocalDate createdOn;

    private Long updatedBy;

    private LocalDate updatedOn;

    private BuildDTO build;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPageLoad() {
        return pageLoad;
    }

    public void setPageLoad(Float pageLoad) {
        this.pageLoad = pageLoad;
    }

    public Float getTimeToInteractive() {
        return timeToInteractive;
    }

    public void setTimeToInteractive(Float timeToInteractive) {
        this.timeToInteractive = timeToInteractive;
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

    public BuildDTO getBuild() {
        return build;
    }

    public void setBuild(BuildDTO build) {
        this.build = build;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestResultDTO)) {
            return false;
        }

        TestResultDTO testResultDTO = (TestResultDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, testResultDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestResultDTO{" +
            "id=" + getId() +
            ", pageLoad=" + getPageLoad() +
            ", timeToInteractive=" + getTimeToInteractive() +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", build=" + getBuild() +
            "}";
    }
}
