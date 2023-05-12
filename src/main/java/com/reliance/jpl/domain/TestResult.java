package com.reliance.jpl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TestResult.
 */
@Entity
@Table(name = "test_result")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "page_load")
    private Float pageLoad;

    @Column(name = "time_to_interactive")
    private Float timeToInteractive;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_on")
    private LocalDate updatedOn;

    @ManyToOne
    @JsonIgnoreProperties(value = { "results" }, allowSetters = true)
    private Build build;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestResult id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPageLoad() {
        return this.pageLoad;
    }

    public TestResult pageLoad(Float pageLoad) {
        this.setPageLoad(pageLoad);
        return this;
    }

    public void setPageLoad(Float pageLoad) {
        this.pageLoad = pageLoad;
    }

    public Float getTimeToInteractive() {
        return this.timeToInteractive;
    }

    public TestResult timeToInteractive(Float timeToInteractive) {
        this.setTimeToInteractive(timeToInteractive);
        return this;
    }

    public void setTimeToInteractive(Float timeToInteractive) {
        this.timeToInteractive = timeToInteractive;
    }

    public Long getCreatedBy() {
        return this.createdBy;
    }

    public TestResult createdBy(Long createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public TestResult createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedBy() {
        return this.updatedBy;
    }

    public TestResult updatedBy(Long updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedOn() {
        return this.updatedOn;
    }

    public TestResult updatedOn(LocalDate updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(LocalDate updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Build getBuild() {
        return this.build;
    }

    public void setBuild(Build build) {
        this.build = build;
    }

    public TestResult build(Build build) {
        this.setBuild(build);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestResult)) {
            return false;
        }
        return id != null && id.equals(((TestResult) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestResult{" +
            "id=" + getId() +
            ", pageLoad=" + getPageLoad() +
            ", timeToInteractive=" + getTimeToInteractive() +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedOn='" + getUpdatedOn() + "'" +
            "}";
    }
}
