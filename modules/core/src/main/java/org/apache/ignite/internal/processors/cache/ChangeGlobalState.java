/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal.processors.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.ignite.internal.managers.discovery.DiscoveryCustomMessage;
import org.apache.ignite.internal.util.typedef.internal.S;
import org.apache.ignite.lang.IgniteUuid;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class ChangeGlobalState implements DiscoveryCustomMessage {
    /** */
    private static final long serialVersionUID = 0L;

    /** Custom message ID. */
    private IgniteUuid id = IgniteUuid.randomUuid();

    /** Near node ID in case if near cache is being started. */
    private UUID initiatingNodeId;

    /** */
    private UUID requestId;

    /** Activate. */
    private Boolean activate;

    /** Activation batch. */
    private DynamicCacheChangeBatch actBatch;

    /** Exception while file lock. */
    private Map<UUID, Exception> exs = new HashMap<>();

    /** Concurrent change state. */
    private boolean concurrentChangeState;

    /**
     * @param actBatch Action batch.
     */
    public ChangeGlobalState(DynamicCacheChangeBatch actBatch) {
        this.actBatch = actBatch;
    }

    /** {@inheritDoc} */
    @Override public IgniteUuid id() {
        return id;
    }

    /** {@inheritDoc} */
    @Nullable @Override public DiscoveryCustomMessage ackMessage() {
        return !concurrentChangeState ? actBatch : null;
    }

    /** {@inheritDoc} */
    @Override public boolean isMutable() {
        return true;
    }

    /**
     * @param nodeId Node id.
     * @param e Exception.
     */
    public void addException(UUID nodeId, Exception e) {
        exs.put(nodeId, e);
    }

    /**
     *
     */
    public UUID initiatorNodeId() {
        return initiatingNodeId;
    }

    /**
     * @param activate Activate.
     */
    public void setActivate(Boolean activate) {
        this.activate = activate;
    }

    /**
     *
     */
    public Boolean getActivate() {
        return activate;
    }

    /**
     *
     */
    public UUID getInitiatingNodeId() {
        return initiatingNodeId;
    }

    /**
     * @param initiatingNodeId Initiating node id.
     */
    public void setInitiatingNodeId(UUID initiatingNodeId) {
        this.initiatingNodeId = initiatingNodeId;
    }

    /**
     *
     */
    public UUID getRequestId() {
        return requestId;
    }

    /**
     * @param requestId Request id.
     */
    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    /**
     *
     */
    public boolean isConcurrentChangeState() {
        return concurrentChangeState;
    }

    /**
     */
    public void setConcurrentChangeState() {
        this.concurrentChangeState = true;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(ChangeGlobalState.class, this);
    }
}
