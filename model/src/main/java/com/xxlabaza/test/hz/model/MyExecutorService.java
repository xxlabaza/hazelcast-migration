/*
 * Copyright 2017 Artem Labazin - All Rights Reserved.
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.xxlabaza.test.hz.model;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author Artem Labazin <xxlabaza@gmail.com>
 * @since 30.01.2017
 */
public class MyExecutorService implements Callable<Void>, Serializable, HazelcastInstanceAware {

  public static final String EXECUTOR_NAME = "my-executor";

  public static final String MAP_NAME = "hello-map";

  private transient HazelcastInstance hazelcastInstance;

  @Override
  public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
    this.hazelcastInstance = hazelcastInstance;
  }

  @Override
  public Void call() throws Exception {
    Set<Object> localKeySet = hazelcastInstance.getMap(MAP_NAME).localKeySet();
    System.out.println("Total keyset size: " + localKeySet.size());
    localKeySet.forEach(it -> System.out.println("Hello-1: " + it));
    return null;
  }
}
