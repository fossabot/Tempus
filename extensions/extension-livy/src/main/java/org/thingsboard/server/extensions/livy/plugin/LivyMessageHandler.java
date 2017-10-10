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
package org.thingsboard.server.extensions.livy.plugin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.thingsboard.server.common.data.DataConstants;
import org.thingsboard.server.common.data.id.RuleId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.kv.*;
import org.thingsboard.server.extensions.api.plugins.PluginCallback;
import org.thingsboard.server.extensions.api.plugins.PluginContext;
import org.thingsboard.server.extensions.api.plugins.handlers.RuleMsgHandler;
import org.thingsboard.server.extensions.api.plugins.msg.RuleToPluginMsg;
import org.thingsboard.server.extensions.api.rules.RuleException;
import org.thingsboard.server.extensions.livy.action.LivyActionMessage;
import org.thingsboard.server.extensions.livy.action.LivyActionPayload;
import org.thingsboard.server.extensions.livy.model.Batch;
import org.thingsboard.server.extensions.livy.model.LivyStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class LivyMessageHandler implements RuleMsgHandler {

    private static final String SPARK_JOB_KEY = "spark_";
    private static final String batchStateURI = "/batches/%d/state";
    private final String baseUrl;
    private final HttpHeaders headers;
    private final Object lock = new Object();
    private final Map<String, String> sparkJobsSessions = new ConcurrentHashMap<>();

    @Override
    public void process(PluginContext ctx, TenantId tenantId, RuleId ruleId, RuleToPluginMsg<?> msg) throws RuleException {
        if (!(msg instanceof LivyActionMessage)) {
            throw new RuleException("Unsupported message type " + msg.getClass().getName() + "!");
        }
        LivyActionPayload payload = ((LivyActionMessage)msg).getPayload();
        final String sparkApplication = SPARK_JOB_KEY + payload.getSparkApplication().toString();
        if(!isSparkJobRunning(sparkApplication)) {
            synchronized (lock) {
                ctx.loadAttributes(ruleId, DataConstants.SERVER_SCOPE, Collections.singleton(sparkApplication),
                        new PluginCallback<List<AttributeKvEntry>>() {
                            @Override
                            public void onSuccess(PluginContext ctx, List<AttributeKvEntry> values) {
                                setSparkJobStatusFromAttributes(values, sparkApplication);
                                if (!isSparkJobRunning(sparkApplication)) {
                                    Batch batch = postSparkJob(payload);
                                    if(batch != null) {
                                        updateRuleAttribute(ctx, tenantId, ruleId, sparkApplication, batch.getId());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(PluginContext ctx, Exception e) {
                                log.error("Failed to fetch application status for tenant. {}", e);
                            }
                        });
            }
        }
    }

    private void updateRuleAttribute(PluginContext ctx, TenantId tenantId, RuleId ruleId, String sparkApplication, int batchId) {
        long ts = System.currentTimeMillis();
        ctx.saveAttributes(tenantId, ruleId, DataConstants.SERVER_SCOPE,
                Collections.singletonList(new BaseAttributeKvEntry(new StringDataEntry(sparkApplication, String.valueOf(batchId)), ts)),
                new PluginCallback<Void>() {
                    @Override
                    public void onSuccess(PluginContext ctx, Void value) {
                        log.warn("Updated attribute for Spark application {} with {}", sparkApplication, batchId);
                    }

                    @Override
                    public void onFailure(PluginContext ctx, Exception e) {
                        log.error("Failed to save attributes {}", e);
                    }
                });
    }

    private Batch postSparkJob(LivyActionPayload payload) {
        try {
            log.warn("Giving a rest call to Livy service with url {} and body {}", baseUrl+payload.getActionPath(), payload.getMsgBody());
            ResponseEntity<Batch> response = new RestTemplate().exchange(
                    baseUrl + payload.getActionPath(),
                    HttpMethod.POST,
                    new HttpEntity<>(payload.getMsgBody(), headers),
                    Batch.class);
            if(response.getStatusCode() == HttpStatus.CREATED){
                return response.getBody();
            }
        } catch (RestClientException e) {
            log.error("Error occurred while rest call", e);
        }
        return null;
    }

    private LivyStatus fetchSparkJobStatus(int batchId){
        String url = String.format(this.baseUrl + batchStateURI, batchId);
        ResponseEntity<String> response = new RestTemplate().exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        if(response.getStatusCode() == HttpStatus.NOT_FOUND){
            return LivyStatus.NOT_FOUND;
        }else{
            return LivyStatus.RUNNING;
        }
    }

    private void setSparkJobStatusFromAttributes(List<AttributeKvEntry> values, String sparkApplication) {
        for (AttributeKvEntry e : values) {
            if (e.getKey().equalsIgnoreCase(sparkApplication) &&
                    !StringUtils.isEmpty(e.getValueAsString())) {
                sparkJobsSessions.put(sparkApplication, e.getValueAsString());
            }
        }
    }

    private boolean isSparkJobRunning(String sparkApplication){
        return !StringUtils.isEmpty(sparkJobsSessions.get(sparkApplication));
    }
}
