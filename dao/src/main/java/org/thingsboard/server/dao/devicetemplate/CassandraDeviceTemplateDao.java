package org.thingsboard.server.dao.devicetemplate;

import com.datastax.driver.core.querybuilder.Select;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thingsboard.server.common.data.DeviceTemplate;
import org.thingsboard.server.dao.DaoUtil;
import org.thingsboard.server.dao.model.nosql.DeviceTemplateEntity;
import org.thingsboard.server.dao.nosql.CassandraAbstractSearchTextDao;
import org.thingsboard.server.dao.util.NoSqlDao;

import java.util.Optional;
import java.util.UUID;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;
import static org.thingsboard.server.dao.model.ModelConstants.*;

@Component
@Slf4j
@NoSqlDao
public class CassandraDeviceTemplateDao extends CassandraAbstractSearchTextDao<DeviceTemplateEntity, DeviceTemplate> implements DeviceTemplateDao {

    @Override
    protected Class<DeviceTemplateEntity> getColumnFamilyClass() {
        return DeviceTemplateEntity.class;
    }

    @Override
    protected String getColumnFamilyName() {
        return DEVICE_TEMPLATE_COLUMN_FAMILY_NAME;
    }

    public DeviceTemplate save(DeviceTemplate deviceTemplate) {
        DeviceTemplate savedDeviceTemplate = super.save(deviceTemplate);
        return savedDeviceTemplate;
    }

    @Override
    public Optional<DeviceTemplate> findDeviceTemplateByTenantIdAndName(UUID tenantId, String deviceTemplateName) {
        Select select = select().from(DEVICE_TEMPLATE_BY_TENANT_AND_NAME_VIEW_NAME);
        Select.Where query = select.where();
        query.and(eq(DEVICE_TEMPLATE_TENANT_ID_PROPERTY, tenantId));
        query.and(eq(DEVICE_TEMPLATE_NAME_PROPERTY, deviceTemplateName));
        return Optional.ofNullable(DaoUtil.getData(findOneByStatement(query)));
    }
}


