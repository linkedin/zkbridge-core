/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.zookeeper.server.backup;

import java.io.File;

import org.apache.zookeeper.common.ConfigException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BackupConfigTest {
  private static final File DEFAULT_STATUS_DIR = new File("/backup/status");
  private static final File DEFAULT_TMP_DIR = new File("/tmp/backup");
  private static final File DEFAULT_STORAGE_CONFIG = new File("/storage/config");
  private static final String DEFAULT_STORAGE_MOUNT_PATH = "/storage/path";
  // Use LocalBackupStorage for testing
  private static final String DEFAULT_STORAGE_PROVIDER_CLASS_NAME =
      "org.apache.zookeeper.server.backup.storage.impl.LocalBackupStorage";

  @Test
  public void testEnabled() throws Exception {
    assertFalse(new BackupConfig.Builder().build().isPresent());
    assertTrue(builder().build().isPresent());
    assertFalse(
        builder().withProperty(BackupSystemProperty.BACKUP_ENABLED, "false").build().isPresent());
    assertTrue(
        builder().withProperty(BackupSystemProperty.BACKUP_ENABLED, "true").build().isPresent());
  }

  @Test
  public void testStatusDir() throws Exception {
    try {
      new BackupConfig.Builder().setEnabled(true).setTmpDir(DEFAULT_TMP_DIR)
          .setStorageConfig(DEFAULT_STORAGE_CONFIG).setMountPath(DEFAULT_STORAGE_MOUNT_PATH)
          .build();
      assertTrue(false);
    } catch (ConfigException exc) {
      assertTrue(true);
    }

    assertEquals(DEFAULT_STATUS_DIR, builder().build().get().getStatusDir());
    File expected = new File("/expected");
    assertEquals(expected, builder().setStatusDir(expected).build().get().getStatusDir());
    assertEquals(expected,
        builder().withProperty(BackupSystemProperty.BACKUP_STATUS_DIR, expected.getAbsolutePath())
            .build().get().getStatusDir());
  }

  @Test
  public void testTmpDir() throws Exception {
    try {
      new BackupConfig.Builder().setEnabled(true).setStatusDir(DEFAULT_STATUS_DIR)
          .setStorageConfig(DEFAULT_STORAGE_CONFIG).setMountPath(DEFAULT_STORAGE_MOUNT_PATH)
          .build();
      assertTrue(false);
    } catch (ConfigException exc) {
      assertTrue(true);
    }

    assertEquals(DEFAULT_TMP_DIR, builder().build().get().getTmpDir());
    File expected = new File("/expected");
    assertEquals(expected, builder().setTmpDir(expected).build().get().getTmpDir());
    assertEquals(expected,
        builder().withProperty(BackupSystemProperty.BACKUP_TMP_DIR, expected.getAbsolutePath())
            .build().get().getTmpDir());
  }

  @Test
  public void testInterval() throws Exception {
    assertEquals(BackupConfig.DEFAULT_BACKUP_INTERVAL_MS,
        builder().build().get().getBackupInterval());
    long expectedInterval = 3 * 60 * 1000L; // 3 minutes
    assertEquals(expectedInterval,
        builder().setBackupInterval(expectedInterval).build().get().getBackupInterval());
    assertEquals(expectedInterval, builder()
        .withProperty(BackupSystemProperty.BACKUP_INTERVAL_MS, "180000") // 180000 ms = 3 min
        .build().get().getBackupInterval());
  }

  @Test
  public void testStorageConfig() throws Exception {
    try {
      new BackupConfig.Builder().setEnabled(true).setStatusDir(DEFAULT_STATUS_DIR)
          .setTmpDir(DEFAULT_TMP_DIR).setMountPath(DEFAULT_STORAGE_MOUNT_PATH).build();
      assertTrue(false);
    } catch (ConfigException exc) {
      assertTrue(true);
    }

    assertEquals(DEFAULT_STORAGE_CONFIG, builder().build().get().getStorageConfig());
    File expected = new File("/expected");
    assertEquals(expected, builder().setStorageConfig(expected).build().get().getStorageConfig());
    assertEquals(expected, builder()
        .withProperty(BackupSystemProperty.BACKUP_STORAGE_CONFIG, expected.getAbsolutePath())
        .build().get().getStorageConfig());
  }

  @Test
  public void testMountPath() throws Exception {
    try {
      new BackupConfig.Builder().setEnabled(true).setStatusDir(DEFAULT_STATUS_DIR)
          .setTmpDir(DEFAULT_TMP_DIR).setStorageConfig(DEFAULT_STORAGE_CONFIG).build();
      assertTrue(false);
    } catch (ConfigException exc) {
      assertTrue(true);
    }

    assertEquals(DEFAULT_STORAGE_MOUNT_PATH, builder().build().get().getMountPath());
    String expected = "/expected";
    assertEquals(expected, builder().setMountPath(expected).build().get().getMountPath());
    assertEquals(expected,
        builder().withProperty(BackupSystemProperty.BACKUP_MOUNT_PATH, expected).build().get()
            .getMountPath());
  }

  @Test
  public void testRetentionPeriod() throws Exception {
    assertEquals(BackupConfig.DEFAULT_RETENTION_DAYS, builder().build().get().getRetentionPeriod());
    int expected = BackupConfig.DEFAULT_RETENTION_DAYS;
    assertEquals(expected,
        builder().setRetentionPeriodInDays(expected).build().get().getRetentionPeriod());
    assertEquals(expected,
        builder().withProperty(BackupSystemProperty.BACKUP_RETENTION_DAYS, "20").build().get()
            .getRetentionPeriod());
  }

  @Test
  public void testRetentionMaintenanceInterval() throws Exception {
    assertEquals(BackupConfig.DEFAULT_RETENTION_MAINTENANCE_INTERVAL_MS,
        builder().build().get().getRetentionMaintenanceInterval());
    long expected = 3 * 60 * 60 * 1000L; // 3 hours
    assertEquals(expected, builder().setRetentionMaintenanceInterval(expected).build().get()
        .getRetentionMaintenanceInterval());
    assertEquals(expected, builder()
        .withProperty(BackupSystemProperty.BACKUP_RETENTION_MAINTENANCE_INTERVAL_MS, "10800000")
        .build().get().getRetentionMaintenanceInterval());
  }

  private BackupConfig.Builder builder() {
    return new BackupConfig.Builder().setEnabled(true).setStatusDir(DEFAULT_STATUS_DIR)
        .setTmpDir(DEFAULT_TMP_DIR).setStorageConfig(DEFAULT_STORAGE_CONFIG)
        .setMountPath(DEFAULT_STORAGE_MOUNT_PATH)
        .setStorageProviderClassName(DEFAULT_STORAGE_PROVIDER_CLASS_NAME);
  }
}