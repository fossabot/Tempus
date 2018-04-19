package org.thingsboard.server.dao.devicetemplate;

import org.thingsboard.server.common.data.DeviceTemplate;
import org.thingsboard.server.dao.Dao;

import java.util.Optional;
import java.util.UUID;

public interface DeviceTemplateDao extends Dao<DeviceTemplate> {
    /**
     * Save or update device template object
     *
     * @param deviceTemplate the device template object
     * @return saved device template object
     */
    DeviceTemplate save(DeviceTemplate deviceTemplate);

    /**
     * Find device template by tenantId and device template name.
     *
     * @param tenantId the tenantId
     * @param name the device template name
     * @return the optional device template object
     */
    Optional<DeviceTemplate> findDeviceTemplateByTenantIdAndName(UUID tenantId, String name);
}
