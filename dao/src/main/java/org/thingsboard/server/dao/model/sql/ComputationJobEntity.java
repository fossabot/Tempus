package org.thingsboard.server.dao.model.sql;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.thingsboard.server.common.data.computation.ComputationJob;
import org.thingsboard.server.common.data.computation.ComputationJob;
import org.thingsboard.server.common.data.id.ComputationId;
import org.thingsboard.server.common.data.id.ComputationJobId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.dao.model.BaseSqlEntity;
import org.thingsboard.server.dao.model.ModelConstants;
import org.thingsboard.server.dao.model.SearchTextEntity;
import org.thingsboard.server.dao.util.mapping.JsonStringType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.UUID;

import static org.thingsboard.server.common.data.UUIDConverter.fromString;
import static org.thingsboard.server.common.data.UUIDConverter.fromTimeUUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = ModelConstants.COMPUTATION_JOB_TABLE_NAME)
public class ComputationJobEntity extends BaseSqlEntity<ComputationJob> implements SearchTextEntity<ComputationJob> {
    @Transient
    private static final long serialVersionUID = -4673737406462009031L;

    @Column(name = ModelConstants.COMPUTATION_JOB_NAME)
    private String jobName;

    @Column(name = ModelConstants.COMPUTATION_JOB_COMPUTAION_ID)
    private String computaionId;

    @Type(type = "json")
    @Column(name = ModelConstants.COMPUTATION_JOB_ARG_PRS)
    private JsonNode argParameters;

    @Column(name = ModelConstants.COMPUTATION_JOB_TENANT_ID)
    private String tenantId;

    @Column(name = ModelConstants.SEARCH_TEXT_PROPERTY)
    private String searchText;

    @Override
    public String getSearchTextSource() {
        return jobName;
    }

    public ComputationJobEntity() {
        super();
    }

    public ComputationJobEntity(ComputationJob computationJob){
        if(computationJob.getId() != null) {
            this.setId(computationJob.getId().getId());
        }
        if(computationJob.getName() != null) {
            this.jobName = computationJob.getName();
        }
        if(computationJob.getArgParameters() != null) {
            this.argParameters = computationJob.getArgParameters();
        }
        if(computationJob.getComputationId() != null) {
            this.computaionId = computationJob.getComputationId().toString();
        }
        if(computationJob.getTenantId() != null) {
            this.tenantId = fromTimeUUID(computationJob.getTenantId().getId());
        }
    }

    @Override
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ComputationJobEntity that = (ComputationJobEntity) o;
        if (jobName != null ? !jobName.equals(that.jobName) : that.jobName != null) return false;
        if (searchText != null ? !searchText.equals(that.searchText) : that.searchText != null) return false;
        if (argParameters != null ? !argParameters.equals(that.argParameters) : that.argParameters != null) return false;
        if (computaionId != null ? !computaionId.equals(that.computaionId) : that.computaionId != null) return false;
        if (tenantId != null ? !tenantId.equals(that.tenantId) : that.tenantId != null) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (jobName != null ? jobName.hashCode() : 0);
        result = 31 * result + (searchText != null ? searchText.hashCode() : 0);
        result = 31 * result + (argParameters != null ? argParameters.hashCode() : 0);
        result = 31 * result + (computaionId != null ? computaionId.hashCode() : 0);
        result = 31 * result + (tenantId != null ? tenantId.hashCode() : 0);
        return result;
    }

    @Override
    public ComputationJob toData() {
        ComputationJob computationJob = new ComputationJob(new ComputationJobId(getId()));
        computationJob.setCreatedTime(UUIDs.unixTimestamp(getId()));
        computationJob.setName(jobName);
        computationJob.setArgParameters(argParameters);
        if(computaionId != null) {
            computationJob.setComputationId(new ComputationId(fromString(computaionId)));
        }
        if (tenantId != null) {
            computationJob.setTenantId(new TenantId(fromString(tenantId)));
        }
        return computationJob;
    }

}
