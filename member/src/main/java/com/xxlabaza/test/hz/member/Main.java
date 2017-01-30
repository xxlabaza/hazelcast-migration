/* 
 * Copyright 2017 Artem Labazin - All Rights Reserved.
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.xxlabaza.test.hz.member;

import static java.util.Collections.singletonList;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.xxlabaza.test.hz.model.MyExecutorService;

/**
 * @author Artem Labazin <xxlabaza@gmail.com>
 * @since 30.01.2017
 */
public class Main {

  public static void main(String[] args) {
    Config config = new Config();

    NetworkConfig networkConfig = config.getNetworkConfig();
    networkConfig.getJoin().getMulticastConfig().setEnabled(false);
    networkConfig.getJoin().getTcpIpConfig().setEnabled(true);
    networkConfig.getJoin().getTcpIpConfig().setMembers(singletonList("127.0.0.1"));

    config.getExecutorConfig(MyExecutorService.EXECUTOR_NAME)
        .setPoolSize(1)
        .setQueueCapacity(1)
        .setStatisticsEnabled(true);

    Hazelcast.newHazelcastInstance(config);
  }
}
