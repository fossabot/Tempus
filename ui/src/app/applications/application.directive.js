/*
 * Copyright © 2017-2018 Hashmap, Inc
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
/* eslint-disable import/no-unresolved, import/default */

import applicationFieldsetTemplate from './application-fieldset.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function ApplicationDirective($compile, $templateCache, toast, $translate, types, applicationService, customerService) {
    var linker = function (scope, element) {
        var template = $templateCache.get(applicationFieldsetTemplate);
        element.html(template);

        scope.types = types;
        scope.isAssignedToCustomer = false;
        scope.isPublic = false;
        scope.assignedCustomer = null;

        scope.applicationCredentials = null;
        scope.application.configurationDescriptor = {
          "schema": {
            "title": "Device Type Filter Configuration",
            "type": "object",
            "properties": {
              "deviceTypes": {
                "title": "Device types",
                "type": "array",
                "minItems" : 1,
                "items": {
                  "type": "object",
                  "title": "Device Type",
                  "properties": {
                    "name": {
                      "title": "Device Type",
                      "type": "string"
                    }
                  }
                },
                "uniqueItems": true
              }
            },
            "required": ["deviceTypes"]
          },
          "form": [
            "deviceTypes"
          ]
        }

        scope.$watch('application', function(newVal) {
            if (newVal) {
                // if (scope.application.id) {
                //     applicationService.getApplicationCredentials(scope.application.id.id).then(
                //         function success(credentials) {
                //             scope.applicationCredentials = credentials;
                //         }
                //     );
                // }
                if (scope.application.customerId && scope.application.customerId.id !== types.id.nullUid) {
                    scope.isAssignedToCustomer = true;
                    customerService.getShortCustomerInfo(scope.application.customerId.id).then(
                        function success(customer) {
                            scope.assignedCustomer = customer;
                            scope.isPublic = customer.isPublic;
                        }
                    );
                } else {
                    scope.isAssignedToCustomer = false;
                    scope.isPublic = false;
                    scope.assignedCustomer = null;
                }
            }
        });

        scope.onApplicationIdCopied = function() {
            toast.showSuccess($translate.instant('application.idCopiedMessage'), 750, angular.element(element).parent().parent(), 'bottom left');
        };

        scope.onAccessTokenCopied = function() {
            toast.showSuccess($translate.instant('application.accessTokenCopiedMessage'), 750, angular.element(element).parent().parent(), 'bottom left');
        };

        $compile(element.contents())(scope);
    }
    return {
        restrict: "E",
        link: linker,
        scope: {
            application: '=',
            isEdit: '=',
            applicationScope: '=',
            theForm: '=',
            onAssignToCustomer: '&',
            onMakePublic: '&',
            onUnassignFromCustomer: '&',
            onManageCredentials: '&',
            onDeleteApplication: '&'
        }
    };
}
