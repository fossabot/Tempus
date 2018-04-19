package org.thingsboard.server.dao.sql.devicetemplate;

import org.springframework.data.repository.CrudRepository;
import org.thingsboard.server.dao.model.sql.DeviceTemplateEntity;
import org.thingsboard.server.dao.util.SqlDao;

@SqlDao
public interface DeviceTemplateRepository extends CrudRepository<DeviceTemplateEntity, String> {

    DeviceTemplateEntity findByTenantIdAndName(String tenantId, String name);

}
