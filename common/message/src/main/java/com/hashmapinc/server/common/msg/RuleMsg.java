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
package com.hashmapinc.server.common.msg;

import com.hashmapinc.server.common.data.rule.RuleType;
import com.hashmapinc.server.common.data.rule.Scope;
import com.hashmapinc.server.common.msg.aware.RuleAwareMsg;

/**
 * Message that is used to deliver some data to the rule instance. 
 * For example: aggregated statistics or command decoded from http request. 
 * 
 * @author ashvayka
 *
 * @param <V> - payload 
 */
public interface RuleMsg<V> extends RuleAwareMsg {

	Scope getRuleLevel();
	
	RuleType getRuleType();
	
	V getPayload();
	
}
