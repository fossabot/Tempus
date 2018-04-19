package org.thingsboard.server.dao.model.nosql;

import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Transient;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.thingsboard.server.common.data.DeviceTemplate;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.DeviceTemplateId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.dao.model.SearchTextEntity;
import org.thingsboard.server.dao.model.type.JsonCodec;

import java.util.Map;
import java.util.UUID;

import static org.thingsboard.server.dao.model.ModelConstants.*;

@Table(name = DEVICE_TEMPLATE_COLUMN_FAMILY_NAME)
@EqualsAndHashCode
@ToString
public class DeviceTemplateEntity implements SearchTextEntity<DeviceTemplate> {

    @Transient
    private static final long serialVersionUID = 8742340113957940434L;

    @PartitionKey(value = 0)
    @Column(name = ID_PROPERTY)
    private UUID id;

    @PartitionKey(value = 1)
    @Column(name = DEVICE_TEMPLATE_TENANT_ID_PROPERTY)
    private UUID tenantId;

    @PartitionKey(value = 2)
    @Column(name = DEVICE_TEMPLATE_CUSTOMER_ID_PROPERTY)
    private UUID customerId;

    @Column(name = DEVICE_TEMPLATE_NAME_PROPERTY)
    private String name;

    @Column(name = SEARCH_TEXT_PROPERTY)
    private String searchText;

    @Column(name = DEVICE_TEMPLATE_ADDITIONAL_INFO_PROPERTY)
    private JsonNode additionalInfo;

    @Column(name = DEVICE_TEMPLATE_QUERIES_PROPERTY)
    private Map<String, String> queries;


    public DeviceTemplateEntity() {
        super();
    }

    public DeviceTemplateEntity(DeviceTemplate deviceTemplate){
        if(deviceTemplate.getId() != null) {
            this.id = deviceTemplate.getId().getId();
        }
        if(deviceTemplate.getTenantId() != null) {
            this.tenantId = deviceTemplate.getTenantId().getId();
        }
        if(deviceTemplate.getCustomerId() != null) {
            this.customerId = deviceTemplate.getCustomerId().getId();
        }

        this.name = deviceTemplate.getName();
        this.additionalInfo = deviceTemplate.getAdditionalInfo();
        this.queries = deviceTemplate.getQueries();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public JsonNode getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(JsonNode additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Map<String, String> getQueries() {
        return queries;
    }

    public void setQueries(Map<String, String> queries) {
        this.queries = queries;
    }

    @Override
    public String getSearchTextSource() {
        return getName();
    }

    @Override
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchText() {
        return searchText;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public DeviceTemplate toData() {
        DeviceTemplate deviceTemplate = new DeviceTemplate(new DeviceTemplateId(id));
        deviceTemplate.setCreatedTime(UUIDs.unixTimestamp(id));
        if(tenantId != null) {
            deviceTemplate.setTenantId(new TenantId(tenantId));
        }
        if(customerId != null) {
            deviceTemplate.setCustomerId(new CustomerId(customerId));
        }

        if(queries != null) {
            deviceTemplate.setQueries(queries);
        }
        deviceTemplate.setName(name);
        deviceTemplate.setAdditionalInfo(additionalInfo);
        return deviceTemplate;
    }
}
