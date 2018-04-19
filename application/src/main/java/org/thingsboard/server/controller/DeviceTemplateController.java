package org.thingsboard.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.DeviceTemplate;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.common.data.audit.ActionType;
import org.thingsboard.server.dao.devicetemplate.DeviceTemplateService;
import org.thingsboard.server.exception.ThingsboardException;

@RestController
@RequestMapping("/api")
@Slf4j
public class DeviceTemplateController extends BaseController {

    @Autowired
    private DeviceTemplateService deviceTemplateService;

    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/devicetemplate", method = RequestMethod.POST)
    @ResponseBody
    public DeviceTemplate saveDeviceTemplate(@RequestBody DeviceTemplate deviceTemplate) throws ThingsboardException {
        try {
            deviceTemplate.setTenantId(getCurrentUser().getTenantId());
            DeviceTemplate savedDeviceTemplate = checkNotNull(deviceTemplateService.save(deviceTemplate));
            return savedDeviceTemplate;
        } catch (Exception e) {
            logEntityAction(emptyId(EntityType.DEVICE_TEMPLATE), deviceTemplate,
                    null, deviceTemplate.getId() == null ? ActionType.ADDED : ActionType.UPDATED, e);
            throw handleException(e);
        }
    }
}
