/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.superbiz.moviefun.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@ApplicationScoped
@Transactional(SUPPORTS)
public class MoviesBean {

    @PersistenceContext(unitName = "movie-unit")
    private EntityManager entityManager;

    public Movie find(final Long id) {
        return entityManager.find(Movie.class, id);
    }

    @Transactional(REQUIRED)
    public void addMovie(final Movie movie) {
        entityManager.persist(movie);
    }

    @Transactional(REQUIRED)
    public void editMovie(final Movie movie) {
        entityManager.merge(movie);
    }

    @Transactional(REQUIRED)
    public void deleteMovie(final long id) {
        final Movie movie = entityManager.find(Movie.class, id);
        entityManager.remove(movie);
    }

    public List<Movie> getMovies(final Integer firstResult, final Integer maxResults, final String field, final String searchTerm) {
        final CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Movie> cq = qb.createQuery(Movie.class);
        final Root<Movie> root = cq.from(Movie.class);
        final EntityType<Movie> type = entityManager.getMetamodel().entity(Movie.class);
        if (field != null && searchTerm != null && !"".equals(field.trim()) && !"".equals(searchTerm.trim())) {
            final Path<String> path = root.get(type.getDeclaredSingularAttribute(field.trim(), String.class));

            final Predicate condition =
                    ("rating".equals(field) || "year".equals(field))
                            ? qb.equal(path, Integer.parseInt(searchTerm.trim()))
                            : qb.like(path, "%" + searchTerm.trim() + "%");

            cq.where(condition);
        }

        cq.orderBy(qb.desc(root.get("year")), qb.asc(root.get("title")));

        final TypedQuery<Movie> q = entityManager.createQuery(cq);
        if (maxResults != null) {
            q.setMaxResults(maxResults);
        }
        if (firstResult != null) {
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public int count(final String field, final String searchTerm) {
        final CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        final Root<Movie> root = cq.from(Movie.class);
        final EntityType<Movie> type = entityManager.getMetamodel().entity(Movie.class);
        cq.select(qb.count(root));
        if (field != null && searchTerm != null && !"".equals(field.trim()) && !"".equals(searchTerm.trim())) {
            final Path<String> path = root.get(type.getDeclaredSingularAttribute(field.trim(), String.class));

            final Predicate condition =
                    ("rating".equals(field) || "year".equals(field))
                            ? qb.equal(path, Integer.parseInt(searchTerm.trim()))
                            : qb.like(path, "%" + searchTerm.trim() + "%");

            cq.where(condition);
        }
        return entityManager.createQuery(cq).getSingleResult().intValue();
    }

    public Collection<String> getGenres() {
        final Set<String> result = new HashSet<String>();

        final CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<String> cq = qb.createQuery(String.class);
        final Root<Movie> root = cq.from(Movie.class);
        cq.select(root.<String>get("genre")).distinct(true);
        final TypedQuery<String> q = entityManager.createQuery(cq);
        final List<String> resultList = q.getResultList();
        for (final String r : resultList) {
            final String[] genres = r.split(" *, *");
            Collections.addAll(result, genres);
        }

        return result;
    }
}
