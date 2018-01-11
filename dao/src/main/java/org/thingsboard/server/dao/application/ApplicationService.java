/**
 * Copyright © 2016-2017 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.server.dao.application;

import org.thingsboard.server.common.data.Application;
import org.thingsboard.server.common.data.id.*;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.page.TextPageLink;

import java.util.List;

public interface ApplicationService {

    Application saveApplication(Application application);

    Application findApplicationById(ApplicationId applicationId);

    void deleteApplication(ApplicationId applicationId);

    TextPageData<Application> findApplicationsByTenantId(TenantId tenantId, TextPageLink pageLink);

    List<Application> findApplicationsByDeviceType(TenantId tenantId, String deviceType);

    Application assignApplicationToCustomer(ApplicationId applicationId, CustomerId customerId);

    Application unassignApplicationFromCustomer(ApplicationId applicationId);

    Application assignDashboardToApplication(ApplicationId applicationId, DashboardId dashboardId, String dashboardType);

    Application unassignDashboardFromApplication(ApplicationId applicationId, String dashboardType);

    Application assignRulesToApplication(ApplicationId applicationId, List<RuleId> ruleIdList);

    Application assignDeviceTypesToApplication(ApplicationId applicationId, List<String> deviceTypes);

}