package org.thingsboard.server.dao.sql.devicetemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.thingsboard.server.common.data.DeviceTemplate;
import org.thingsboard.server.dao.DaoUtil;
import org.thingsboard.server.dao.devicetemplate.DeviceTemplateDao;
import org.thingsboard.server.dao.model.sql.DeviceTemplateEntity;
import org.thingsboard.server.dao.sql.JpaAbstractSearchTextDao;
import org.thingsboard.server.dao.util.SqlDao;

import java.util.Optional;
import java.util.UUID;

import static org.thingsboard.server.common.data.UUIDConverter.fromTimeUUID;

@Component
@SqlDao
public class JPADeviceTemplateDao extends JpaAbstractSearchTextDao<DeviceTemplateEntity, DeviceTemplate> implements DeviceTemplateDao {

    @Autowired
    private DeviceTemplateRepository deviceTemplateRepository;

    @Override
    protected Class<DeviceTemplateEntity> getEntityClass() {
        return DeviceTemplateEntity.class;
    }

    @Override
    protected CrudRepository<DeviceTemplateEntity, String> getCrudRepository() {
        return deviceTemplateRepository;
    }

    @Override
    public Optional<DeviceTemplate> findDeviceTemplateByTenantIdAndName(UUID tenantId, String name) {
        DeviceTemplate deviceTemplate = DaoUtil.getData(deviceTemplateRepository.findByTenantIdAndName(fromTimeUUID(tenantId), name));
        return Optional.ofNullable(deviceTemplate);
    }

}
