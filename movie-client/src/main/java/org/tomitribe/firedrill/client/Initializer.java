/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tomitribe.firedrill.client;

import org.tomitribe.firedrill.client.scenario.movie.MovieScenario;
import org.tomitribe.sabot.Config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;

@Singleton
@Startup
@Lock(LockType.READ)
public class Initializer {
    @Resource
    private ManagedExecutorService mes;

    @Inject
    private MovieScenario movieScenario;

    @Inject
    @Config(value = "movie.client.threads", defaultValue = "5")
    private Integer targetUrl;

    @PostConstruct
    void postConstruct() {
        for (int i = 0; i < targetUrl; i++) {
            mes.execute(movieScenario);
        }
    }
}
