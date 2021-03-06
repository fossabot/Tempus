/**
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
package com.hashmapinc.server.extensions.core.plugin.dummy;

import com.hashmapinc.server.extensions.api.component.EmptyComponentConfiguration;
import com.hashmapinc.server.extensions.api.plugins.PluginContext;
import com.hashmapinc.server.extensions.api.component.Plugin;
import com.hashmapinc.server.extensions.api.plugins.AbstractPlugin;
import com.hashmapinc.server.extensions.api.plugins.handlers.RuleMsgHandler;
import com.hashmapinc.server.extensions.core.action.dummy.PassThroughAction;

@Plugin(name = "Pass Through Plugin", actions = {PassThroughAction.class})
public class PassThroughPlugin extends AbstractPlugin<EmptyComponentConfiguration> implements RuleMsgHandler {
    @Override
    public void resume(PluginContext pluginContext) {
    }

    @Override
    public void suspend(PluginContext pluginContext) {
    }

    @Override
    public void stop(PluginContext pluginContext) {
    }

    @Override
    public void init(EmptyComponentConfiguration emptyComponentConfiguration) {
    }
}
