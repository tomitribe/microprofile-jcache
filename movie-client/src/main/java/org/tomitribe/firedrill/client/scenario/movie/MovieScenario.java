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
package org.tomitribe.firedrill.client.scenario.movie;

import com.github.javafaker.Book;
import com.github.javafaker.Faker;
import org.tomitribe.firedrill.client.scenario.Endpoint;
import org.tomitribe.firedrill.client.scenario.ScenarioInvoker;
import org.tomitribe.firedrill.client.scenario.WeightedRandomResult;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.tomitribe.firedrill.client.scenario.Endpoint.endpoint;

/**
 * @author Roberto Cortez
 */
@ApplicationScoped
public class MovieScenario extends ScenarioInvoker {
    @Inject
    private Faker faker;

    @Override
    protected List<Endpoint> getEndpoints() {
        return Stream.of(endpoint("movie/rest/movies", "GET"),
                         endpoint("movie/rest/movies/count", "GET"),
                         endpoint("movie/rest/movies", "POST", this::generatePostData),
                         endpoint("movie/rest/movies/1", "DELETE"),
                         endpoint("movie/rest/movies/1", "GET"))
                     .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    @Override
    protected WeightedRandomResult<Endpoint> distributeEndpoints() {
        final ArrayList<Endpoint> endpoints =
                getEndpoints().stream()
                              .filter(e -> e.getMethod().equals("GET"))
                              .collect(ArrayList::new, (list, endpoint) -> {
                                  list.add(endpoint);
                                  list.add(endpoint);
                                  list.add(endpoint);
                                  list.add(endpoint);
                              }, ArrayList::addAll);

        endpoints.addAll(getEndpoints());
        shuffle(endpoints);
        return new WeightedRandomResult<>(endpoints);
    }

    private Object generatePostData() {
        if (Math.random() * 100 < 95) {
            final Book book = faker.book();
            return new Movie.MovieWrapper(new Movie(book.author(),
                                                    book.title(),
                                                    faker.number().numberBetween(1900, 2017),
                                                    book.genre(),
                                                    faker.number().numberBetween(1, 10)));
        } else {
            return "";
        }
    }
}
