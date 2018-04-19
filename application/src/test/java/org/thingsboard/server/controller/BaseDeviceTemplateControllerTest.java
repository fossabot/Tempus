package org.thingsboard.server.controller;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.thingsboard.server.common.data.DeviceTemplate;
import org.thingsboard.server.common.data.Tenant;
import org.thingsboard.server.common.data.User;
import org.thingsboard.server.common.data.security.Authority;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class BaseDeviceTemplateControllerTest extends AbstractControllerTest {

    private IdComparator<DeviceTemplate> idComparator = new IdComparator<>();

    private Tenant savedTenant;
    private User tenantAdmin;

    @Before
    public void beforeTest() throws Exception {
        loginSysAdmin();

        Tenant tenant = new Tenant();
        tenant.setTitle("My tenant");
        savedTenant = doPost("/api/tenant", tenant, Tenant.class);
        Assert.assertNotNull(savedTenant);

        tenantAdmin = new User();
        tenantAdmin.setAuthority(Authority.TENANT_ADMIN);
        tenantAdmin.setTenantId(savedTenant.getId());
        tenantAdmin.setEmail("tenant2@thingsboard.org");
        tenantAdmin.setFirstName("Joe");
        tenantAdmin.setLastName("Downs");

        tenantAdmin = createUserAndLogin(tenantAdmin, "testPassword1");
    }

    @After
    public void afterTest() throws Exception {
        loginSysAdmin();

        doDelete("/api/tenant/"+savedTenant.getId().getId().toString())
                .andExpect(status().isOk());
    }

    @Test
    public void testSaveDeviceTemplate() throws Exception {
        DeviceTemplate deviceTemplate = new DeviceTemplate();
        Map<String, String> queries = new HashMap<>();
        queries.put("Attribute A","Not Equals 1");
        deviceTemplate.setName("DT");
        deviceTemplate.setQueries(queries);

        DeviceTemplate savedDeviceTemplate = doPost("/api/devicetemplate", deviceTemplate, DeviceTemplate.class);
        Assert.assertEquals(savedDeviceTemplate.getName(), "DT");
    }

    @Test
    public void testSaveDeviceTemplateWithEmptyName() throws Exception {
        DeviceTemplate deviceTemplate = new DeviceTemplate();
        doPost("/api/devicetemplate", deviceTemplate)
                .andExpect(status().isBadRequest())
                .andExpect(statusReason(containsString("Device Template name should be specified!")));
    }

    @Test
    public void testSaveDeviceTemplateWithDuplicateName() throws Exception {
        DeviceTemplate deviceTemplate = new DeviceTemplate();
        deviceTemplate.setName("DT1");
        DeviceTemplate savedDeviceTemplate = doPost("/api/devicetemplate", deviceTemplate, DeviceTemplate.class);
        Assert.assertEquals(savedDeviceTemplate.getName(), "DT1");


        DeviceTemplate duplicateDeviceTemplate = new DeviceTemplate();
        duplicateDeviceTemplate.setName("DT1");
        doPost("/api/devicetemplate", duplicateDeviceTemplate)
                .andExpect(status().isBadRequest())
                .andExpect(statusReason(containsString("Device Template with such name already exists!")));
    }
}
