package org.thingsboard.server.dao.devicetemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thingsboard.server.common.data.Customer;
import org.thingsboard.server.common.data.DeviceTemplate;
import org.thingsboard.server.common.data.Tenant;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.dao.customer.CustomerDao;
import org.thingsboard.server.dao.entity.AbstractEntityService;
import org.thingsboard.server.dao.exception.DataValidationException;
import org.thingsboard.server.dao.service.DataValidator;
import org.thingsboard.server.dao.tenant.TenantDao;
import static org.thingsboard.server.dao.model.ModelConstants.NULL_UUID;

@Service
@Slf4j
public class DeviceTemplateServiceImpl extends AbstractEntityService implements DeviceTemplateService {

    @Autowired
    private DeviceTemplateDao deviceTemplateDao;

    @Autowired
    private TenantDao tenantDao;

    @Autowired
    private CustomerDao customerDao;

    @Override
    public DeviceTemplate save(DeviceTemplate deviceTemplate) {
        log.trace("Executing save DeviceTemplate [{}]", deviceTemplate);
        deviceTemplateDataValidator.validate(deviceTemplate);
        return deviceTemplateDao.save(deviceTemplate);
    }


    private DataValidator<DeviceTemplate> deviceTemplateDataValidator = new DataValidator<DeviceTemplate>() {
        @Override
        protected void validateCreate(DeviceTemplate deviceTemplate) {
            deviceTemplateDao.findDeviceTemplateByTenantIdAndName(deviceTemplate.getTenantId().getId(), deviceTemplate.getName()).ifPresent(
                    d -> {
                        throw new DataValidationException("Device Template with such name already exists!");
                    }
            );
        }

        @Override
        protected void validateUpdate(DeviceTemplate deviceTemplate) {
            deviceTemplateDao.findDeviceTemplateByTenantIdAndName(deviceTemplate.getTenantId().getId(), deviceTemplate.getName()).ifPresent(
                    d -> {
                        if (!d.getUuidId().equals(deviceTemplate.getUuidId())) {
                            throw new DataValidationException("Device Template with such name already exists!");
                        }
                    }
            );
        }

        @Override
        protected void validateDataImpl(DeviceTemplate deviceTemplate) {
            if (StringUtils.isEmpty(deviceTemplate.getName())) {
                throw new DataValidationException("Device Template name should be specified!");
            }
            if (deviceTemplate.getTenantId() == null) {
                throw new DataValidationException("Device Template should be assigned to tenant!");
            } else {
                Tenant tenant = tenantDao.findById(deviceTemplate.getTenantId().getId());
                if (tenant == null) {
                    throw new DataValidationException("Device Template is referencing to non-existent tenant!");
                }
            }
            if (deviceTemplate.getCustomerId() == null) {
                deviceTemplate.setCustomerId(new CustomerId(NULL_UUID));
            } else if (!deviceTemplate.getCustomerId().getId().equals(NULL_UUID)) {
                Customer customer = customerDao.findById(deviceTemplate.getCustomerId().getId());
                if (customer == null) {
                    throw new DataValidationException("Can't assign device template to non-existent customer!");
                }
                if (!customer.getTenantId().getId().equals(deviceTemplate.getTenantId().getId())) {
                    throw new DataValidationException("Can't assign device template to customer from different tenant!");
                }
            }
        }
    };
}
