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
package org.tomitribe.firedrill.client.scenario;

import javax.ws.rs.client.Entity;
import java.util.function.Supplier;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

/**
 * @author Roberto Cortez
 */
public class Endpoint {
    private String path;
    private String method;
    private Supplier supplier;

    public Endpoint(final String path, final String method, final Supplier supplier) {
        this.path = path;
        this.method = method;
        this.supplier = supplier;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public Entity getEntity() {
        return entity(supplier.get(), APPLICATION_JSON_TYPE);
    }

    public static Endpoint endpoint(final String endpoint, final String method) {
        return new Endpoint(endpoint, method, () -> "");
    }

    public static Endpoint endpoint(final String endpoint, final String method, final Supplier supplier) {
        return new Endpoint(endpoint, method, supplier);
    }
}
