package org.jenkinsci.plugins.heisentest;

import hudson.model.AbstractBuild;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.logging.Logger;

@Entity(name = "HEISENTEST_BUILD_DETAILS")
public class HeisentestBuildDetails implements BuildDetails {

    private final static Logger LOGGER = Logger.getLogger(HeisentestBuildDetails.class.getName());

    private String id;
    private String name;
    private String fullName;
    private Date startDate = new Date();
    private Date endDate;
    private Long duration;
    private String result;

    @Id
    @Column(nullable = false, unique = true)
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Column(nullable = false, unique = false)
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Column(nullable = false, unique = false)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    @Column(nullable = false, unique = false)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(final Date start) {
        this.startDate = start;
    }

    @Column(nullable = true, unique = false)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(final Date end) {
        this.endDate = end;
    }

    @Column(nullable = true, unique = false)
    public Long getDuration() {
        return duration;
    }

    public void setDuration(final Long duration) {
        this.duration = duration;
    }

    @Column(nullable = true, unique = false)
    public String getResult() {
        return result;
    }

    public void setResult(final String result) {
        if (result != null) {
            this.result = result;
        }
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", this.fullName, this.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        // fail-fast logic
        if (this == obj) {
            return true;
        }
        if (null == obj) {
            return false;
        }
        if (!(obj instanceof BuildDetails)) {
            return false;
        }

        final BuildDetails other = (BuildDetails) obj;

        return other.hashCode() == this.hashCode();
    }

    public HeisentestBuildDetails() {
    }

    public HeisentestBuildDetails(final String id, final String name, final String fullName,
                                  final Date startDate, final Date endDate, final long duration) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    public HeisentestBuildDetails(final AbstractBuild<?, ?> build) {
        this.name = build.getRootBuild().getProject().getDisplayName();
        this.fullName = build.getFullDisplayName();
        this.startDate = build.getTime();
        this.id = String
                .format("%s/%s/", this.name, build.getId());
    }
}
