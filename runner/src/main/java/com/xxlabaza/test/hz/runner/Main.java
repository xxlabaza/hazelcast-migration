/* 
 * Copyright 2017 Artem Labazin - All Rights Reserved.
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.xxlabaza.test.hz.runner;


import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.xxlabaza.test.hz.model.MyExecutorService;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

/**
 * @author Artem Labazin <xxlabaza@gmail.com>
 * @since 30.01.2017
 */
public class Main {

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    ClientConfig clientConfig = new ClientConfig();

    clientConfig.getNetworkConfig()
        .setConnectionAttemptLimit(10)
        .addAddress("127.0.0.1");

    HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

    IMap<Integer, String> map = hazelcastInstance.getMap(MyExecutorService.MAP_NAME);

    if (map.isEmpty()) {
      System.out.println("Initial data loading...");
      IntStream.range(0, 100).forEach(it -> {
        map.put(it, UUID.randomUUID().toString());
      });
      System.out.println("Data is loaded");
    }

    System.out.println("Submitting executors");
    hazelcastInstance
        .getExecutorService(MyExecutorService.EXECUTOR_NAME)
        .submitToAllMembers(new MyExecutorService())
        .values()
        .forEach(it -> {
          try {
            it.get();
          } catch (InterruptedException | ExecutionException ex) {
            // Ooops...
          }
        });
    System.out.println("Executor are finished");

    hazelcastInstance.shutdown();
  }
}
