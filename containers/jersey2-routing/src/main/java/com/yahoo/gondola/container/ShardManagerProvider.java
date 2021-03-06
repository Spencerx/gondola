/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the New BSD License.
 * See the accompanying LICENSE file for terms.
 */

package com.yahoo.gondola.container;

import com.yahoo.gondola.Config;
import com.yahoo.gondola.Gondola;
import com.yahoo.gondola.container.client.ShardManagerClient;
import com.yahoo.gondola.container.client.ZookeeperShardManagerClient;
import com.yahoo.gondola.container.impl.ZookeeperShardManagerServer;

/**
 * A Provider that provide the CommandListener implementation.
 */
public class ShardManagerProvider {

    public ShardManagerServer getShardManagerServer(RoutingFilter filter) {
        Gondola gondola = filter.getGondola();
        Utils.RegistryConfig conf = Utils.getRegistryConfig(gondola.getConfig());
        ShardManager shardManager =
            new ShardManager(gondola, filter, gondola.getConfig(),
                             getShardManagerClient(gondola.getConfig(), gondola.getHostId()));
        switch (conf.type) {
            case NONE:
                return null;
            case ZOOKEEPER:
                return new ZookeeperShardManagerServer(conf.attributes.get("serviceName"),
                                                       conf.attributes.get("connectString"),
                                                       gondola, shardManager);
        }
        throw new IllegalArgumentException("Unknown config");
    }

    public ShardManagerClient getShardManagerClient(Config config, String clientName) {
        Utils.RegistryConfig conf = Utils.getRegistryConfig(config);
        switch (conf.type) {
            case NONE:
                return null;
            case ZOOKEEPER:
                return new ZookeeperShardManagerClient(conf.attributes.get("serviceName"),
                                                       clientName,
                                                       conf.attributes.get("connectString"),
                                                       config);
        }
        throw new IllegalArgumentException("Unknown config");
    }
}
