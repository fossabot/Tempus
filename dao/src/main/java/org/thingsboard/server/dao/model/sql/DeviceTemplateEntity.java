package org.thingsboard.server.dao.model.sql;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.thingsboard.server.common.data.DeviceTemplate;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.DeviceTemplateId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.dao.model.BaseSqlEntity;
import org.thingsboard.server.dao.model.ModelConstants;
import org.thingsboard.server.dao.model.SearchTextEntity;
import org.thingsboard.server.dao.util.mapping.JsonStringType;

import javax.persistence.*;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = ModelConstants.DEVICE_TEMPLATE_COLUMN_FAMILY_NAME)
public class DeviceTemplateEntity extends BaseSqlEntity<DeviceTemplate> implements SearchTextEntity<DeviceTemplate> {

    @Column(name = ModelConstants.DEVICE_TEMPLATE_TENANT_ID_PROPERTY)
    private String tenantId;

    @Column(name = ModelConstants.DEVICE_TEMPLATE_CUSTOMER_ID_PROPERTY)
    private String customerId;

    @Column(name = ModelConstants.DEVICE_TEMPLATE_NAME_PROPERTY)
    private String name;

    @Column(name = ModelConstants.SEARCH_TEXT_PROPERTY)
    private String searchText;

    @Type(type = "json")
    @Column(name = ModelConstants.DEVICE_TEMPLATE_ADDITIONAL_INFO_PROPERTY)
    private JsonNode additionalInfo;

    @ElementCollection
    @JoinTable(name=ModelConstants.DEVICE_TEMPLATE_QUERIES, joinColumns=@JoinColumn(name="device_template_id"))
    @MapKeyColumn(name=ModelConstants.DEVICE_TEMPLATE_QUERY_ATTRIBUTE)
    @Column(name=ModelConstants.DEVICE_TEMPLATE_QUERY_VALUE)
    private Map<String, String> queries;



    public DeviceTemplateEntity() {
        super();
    }

    public DeviceTemplateEntity(DeviceTemplate deviceTemplate){
        if(deviceTemplate.getId() != null) {
            this.setId(deviceTemplate.getId().getId());
        }
        if(deviceTemplate.getTenantId() != null) {
            this.tenantId = toString(deviceTemplate.getTenantId().getId());
        }
        if(deviceTemplate.getCustomerId() != null) {
            this.customerId = toString(deviceTemplate.getCustomerId().getId());
        }

        this.name = deviceTemplate.getName();
        this.additionalInfo = deviceTemplate.getAdditionalInfo();
        this.queries = deviceTemplate.getQueries();
    }

    @Override
    public String getSearchTextSource() {
        return getName();
    }

    @Override
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public DeviceTemplate toData() {
        DeviceTemplate deviceTemplate = new DeviceTemplate(new DeviceTemplateId(getId()));
        deviceTemplate.setCreatedTime(UUIDs.unixTimestamp(getId()));
        if(tenantId != null) {
            deviceTemplate.setTenantId(new TenantId(toUUID(tenantId)));
        }
        if(customerId != null) {
            deviceTemplate.setCustomerId(new CustomerId(toUUID(customerId)));
        }

        if(queries != null) {
            deviceTemplate.setQueries(queries);
        }
        deviceTemplate.setName(name);
        deviceTemplate.setAdditionalInfo(additionalInfo);
        return deviceTemplate;
    }
}


