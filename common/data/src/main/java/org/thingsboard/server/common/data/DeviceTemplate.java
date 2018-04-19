package org.thingsboard.server.common.data;

import lombok.EqualsAndHashCode;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.DeviceTemplateId;
import org.thingsboard.server.common.data.id.TenantId;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
public class DeviceTemplate extends SearchTextBasedWithAdditionalInfo<DeviceTemplateId> implements HasName {

    private static final long serialVersionUID = -4230420102551536578L;

    private TenantId tenantId;
    private CustomerId customerId;
    private String name;
    private Map<String, String> queries;

    public DeviceTemplate() {
        super();
    }

    public DeviceTemplate(DeviceTemplateId id) {
        super(id);
    }

    public DeviceTemplate(DeviceTemplate deviceTemplate){
        super(deviceTemplate);
        this.tenantId = deviceTemplate.getTenantId();
        this.customerId = deviceTemplate.getCustomerId();
        this.name = deviceTemplate.getName();
        this.queries = deviceTemplate.getQueries();
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public void setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getQueries() {
        return queries;
    }

    public void setQueries(Map<String, String> queries) {
        this.queries = queries;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSearchText() {
        return getName();
    }


}
