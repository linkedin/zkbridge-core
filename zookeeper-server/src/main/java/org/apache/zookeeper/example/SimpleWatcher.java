package org.apache.zookeeper.example;

import java.util.concurrent.LinkedBlockingQueue;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A simple {@link Watcher} implementation with just aggregates the watch events.
 */
public class SimpleWatcher implements Watcher {

  private static final Logger LOG = LoggerFactory.getLogger(SimpleWatcher.class);

  LinkedBlockingQueue<WatchedEvent> events = new LinkedBlockingQueue<>();

  @Override
  public void process(WatchedEvent event) {
    try {
      events.put(event);
    } catch (InterruptedException e) {
      LOG.error("ignoring interrupt during event.put", e);
    }
  }

  public LinkedBlockingQueue<WatchedEvent> getEvents() {
    return events;
  }
}
