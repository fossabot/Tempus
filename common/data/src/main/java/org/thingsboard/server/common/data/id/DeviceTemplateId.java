package org.thingsboard.server.common.data.id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.thingsboard.server.common.data.EntityType;

import java.util.UUID;

public class DeviceTemplateId extends UUIDBased implements EntityId {

    private static final long serialVersionUID = 2733597177001634321L;

    @JsonCreator
    public DeviceTemplateId(@JsonProperty("id") UUID id) {
        super(id);
    }

    public static DeviceTemplateId fromString(String deviceTemplateId) {
        return new DeviceTemplateId(UUID.fromString(deviceTemplateId));
    }

    @JsonIgnore
    @Override
    public EntityType getEntityType() {
        return EntityType.DEVICE_TEMPLATE;
    }
}
