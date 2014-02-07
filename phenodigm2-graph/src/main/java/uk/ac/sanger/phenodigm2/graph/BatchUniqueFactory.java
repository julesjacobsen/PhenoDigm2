/*
 * Copyright © 2011-2013 EMBL - European Bioinformatics Institute
 * and Genome Research Limited
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.  
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.sanger.phenodigm2.graph;

import java.util.Collections;
import java.util.Map;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for making unique Node or Relationship objects when using the Neo4J
 * BatchInserter or BatchGraphDatabase.
 *
 * These are NOT THREAD SAFE and should only be used with a single-threaded
 * BatchInserter or BatchGraphDatabase classes.
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 *
 * @param <T> the type of entity created by this {@link UniqueFactory}.
 */
public abstract class BatchUniqueFactory<T extends PropertyContainer> {

    private static final Logger logger = LoggerFactory.getLogger(BatchUniqueFactory.class);
    
    private final Map<Object, T> index;
    private final GraphDatabaseService graphDatabaseService;

    public BatchUniqueFactory(GraphDatabaseService graphDatabaseService, Map<Object, T> index) {
        this.index = index;
        this.graphDatabaseService = graphDatabaseService;
    }

    
    public static abstract class UniqueRelationshipFactory extends BatchUniqueFactory<Relationship> {

        public UniqueRelationshipFactory(GraphDatabaseService graphDatabaseService, Map<Object, Relationship> index) {
            super(graphDatabaseService, index);
        }

        /**
         * Default implementation of
         * {@link UniqueFactory#initialize(PropertyContainer, Map)}, does
         * nothing for {@link Relationship Relationships}. Override to perform
         * some action with the guarantee that this method is only invoked for
         * the transaction that succeeded in creating the {@link Relationship}.
         *         
         * @see UniqueFactory#initialize(PropertyContainer, Map)
         * @see UniqueFactory#create(Map)
         */
        @Override
        protected void initialize(Relationship relationship, Map<String, Object> properties) {
            // this class has the create() method, initialize() is optional
        }
    }

    public static abstract class UniqueNodeFactory extends BatchUniqueFactory<Node> {

        public UniqueNodeFactory(GraphDatabaseService graphDatabaseService, Map<Object, Node> index) {
            super(graphDatabaseService, index);
        }

        /**
         * Default implementation of {@link UniqueFactory#create(Map)}, creates
         * a plain node. Override to retrieve the node to add to the index by
         * some other means than by creating it. For initialization of the
         * {@link Node}, use the
         * {@link UniqueFactory#initialize(PropertyContainer, Map)} method.
         *
         * @see UniqueFactory#create(Map)
         * @see UniqueFactory#initialize(PropertyContainer, Map)
         */
        @Override
        protected Node create(Map<String, Object> properties) {
            return graphDatabase().createNode();
        }

    }

    public static class UniqueEntity<T extends PropertyContainer> {

        private final T entity;
        private final boolean created;

        UniqueEntity(T entity, boolean created) {
            this.entity = entity;
            this.created = created;
        }

        public T entity() {
            return this.entity;
        }

        public boolean wasCreated() {
            return this.created;
        }
    }

    /**
     * Implement this method to initialize the {@link Node} or
     * {@link Relationship} created for being stored in the index.
     *
     * This method will be invoked exactly once per created unique entity.
     *
     * The created entity might be discarded if another thread creates an entity
     * concurrently. This method will however only be invoked in the transaction
     * that succeeds in creating the node.
     *
     * @param created the created entity to initialize.
     * @param properties the properties that this entity was indexed uniquely
     * with.
     */
    protected abstract void initialize(T created, Map<String, Object> properties);

    /**
     * Implement this method to create the {@link Node} or {@link Relationship}
     * to index.
     *
     * This method will be invoked exactly once per transaction that attempts to
     * create an entry in the index. The created entity might be discarded if
     * another thread creates an entity with the same mapping concurrently.
     *
     * @param properties the properties that this entity will is to be indexed
     * uniquely with.
     * @return the entity to add to the index.
     */
    protected abstract T create(Map<String, Object> properties);

    /**
     * Get the indexed entity, creating it (exactly once) if no indexed entity
     * exists.
     *
     * @param key the key to find the entity under in the index.
     * @param value the value the key is mapped to for the entity in the index.
     * @return the unique entity in the index.
     */
    public final T getOrCreate(String key, Object value) {
        return getOrCreateWithOutcome(key, value).entity();
    }

    /**
     * Get the indexed entity, creating it (exactly once) if no indexed entity
     * exists. Includes the outcome, i.e. whether the entity was created or not.
     *
     * @param key the key to find the entity under in the index.
     * @param value the value the key is mapped to for the entity in the index.
     * @return the unique entity in the index as well as whether or not it was
     * created, wrapped in a {@link UniqueEntity}.
     */
    public final UniqueEntity<T> getOrCreateWithOutcome(String key, Object value) {
//        T result = index.get( key, value ).getSingle();
        T result = index.get(value);
        boolean wasCreated = false;
        if (result == null) {
            Map<String, Object> properties = Collections.singletonMap(key, value);
            T created = create(properties);
//            result = index.putIfAbsent(created, key, value);
            result = index.put(value, created);
//            logger.info("{} {} added to index", key, value);
            if (result == null) {
                initialize(created, properties);
                result = created;
                wasCreated = true;
            } 
        }
        return new UniqueEntity<>(result, wasCreated);
    }

    /**
     * Get the {@link GraphDatabaseService graph database} of the referenced
     * index.
     *
     * @return the {@link GraphDatabaseService graph database} of the referenced
     * index.
     */
    protected final GraphDatabaseService graphDatabase() {
        return graphDatabaseService;
    }

    /**
     * Get the referenced index.
     *
     * @return the referenced index.
     */
    protected final Map<Object, T> index() {
        return index;
    }

}
