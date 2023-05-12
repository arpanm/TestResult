package com.reliance.jpl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Build.
 */
@Entity
@Table(name = "build")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Build implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "branch")
    private String branch;

    @Column(name = "tag")
    private String tag;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_on")
    private LocalDate updatedOn;

    @OneToMany(mappedBy = "build")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "build" }, allowSetters = true)
    private Set<TestResult> results = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Build id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranch() {
        return this.branch;
    }

    public Build branch(String branch) {
        this.setBranch(branch);
        return this;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getTag() {
        return this.tag;
    }

    public Build tag(String tag) {
        this.setTag(tag);
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getCreatedBy() {
        return this.createdBy;
    }

    public Build createdBy(Long createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public Build createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedBy() {
        return this.updatedBy;
    }

    public Build updatedBy(Long updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedOn() {
        return this.updatedOn;
    }

    public Build updatedOn(LocalDate updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(LocalDate updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Set<TestResult> getResults() {
        return this.results;
    }

    public void setResults(Set<TestResult> testResults) {
        if (this.results != null) {
            this.results.forEach(i -> i.setBuild(null));
        }
        if (testResults != null) {
            testResults.forEach(i -> i.setBuild(this));
        }
        this.results = testResults;
    }

    public Build results(Set<TestResult> testResults) {
        this.setResults(testResults);
        return this;
    }

    public Build addResult(TestResult testResult) {
        this.results.add(testResult);
        testResult.setBuild(this);
        return this;
    }

    public Build removeResult(TestResult testResult) {
        this.results.remove(testResult);
        testResult.setBuild(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Build)) {
            return false;
        }
        return id != null && id.equals(((Build) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Build{" +
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
