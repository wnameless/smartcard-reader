/**
 *
 * @author Wei-Ming Wu
 *
 *
 * Copyright 2014 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package com.github.wnameless.smartcard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import javax.smartcardio.CommandAPDU;

import org.junit.Before;
import org.junit.Test;

import com.github.wnameless.smartcard.APDU.APDUBuilder;
import com.google.common.primitives.Bytes;
import com.google.common.testing.NullPointerTester;

public class APDUTest {

  APDUBuilder builder;

  @Before
  public void setUp() throws Exception {
    builder = APDU.builder();
  }

  @Test
  public void testPrivateConstructor() throws Exception {
    Constructor<APDU> c = APDU.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c.getModifiers()));
    c.setAccessible(true);
    c.newInstance();
  }

  @Test
  public void testAllNPE() {
    NullPointerTester tester = new NullPointerTester();
    tester.testAllPublicConstructors(APDUBuilder.class);
    tester.testAllPublicInstanceMethods(builder);
    tester.testAllPublicStaticMethods(APDUBuilder.class);
  }

  @Test
  public void testBuilder() {
    assertTrue(APDU.builder() instanceof APDUBuilder);
  }

  @Test
  public void testBuild() {
    assertTrue(builder.build() instanceof CommandAPDU);
  }

  @Test
  public void testDefaultAPDU() {
    assertEquals(new CommandAPDU(new byte[] { (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00 }), builder.build());
  }

  @Test
  public void testSetCLA() {
    assertEquals(new CommandAPDU(new byte[] { (byte) 0x01, (byte) 0x00,
        (byte) 0x00, (byte) 0x00 }), builder.setCLA((byte) 0x01).build());
  }

  @Test
  public void testSetINS() {
    assertEquals(new CommandAPDU(new byte[] { (byte) 0x00, INS.APPEND_RECORD,
        (byte) 0x00, (byte) 0x00 }), builder.setINS(INS.APPEND_RECORD).build());
  }

  @Test
  public void testSetP1() {
    assertEquals(new CommandAPDU(new byte[] { (byte) 0x00, (byte) 0x00,
        (byte) 0x01, (byte) 0x00 }), builder.setP1((byte) 0x01).build());
  }

  @Test
  public void testSetP2() {
    assertEquals(new CommandAPDU(new byte[] { (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x02 }), builder.setP2((byte) 0x02).build());
  }

  @Test
  public void testSetData() {
    assertEquals(new CommandAPDU(new byte[] { (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x11 }),
        builder.setData((byte) 0x11).build());
  }

  @Test
  public void testSetLongData() {
    byte[] longData = new byte[512];
    assertEquals(
        new CommandAPDU(Bytes.concat(new byte[] { (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00 },
            longData)), builder.setData(longData).build());
  }

  @Test
  public void testSetDataException() {
    try {
      builder.setData(new byte[0]);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Data length is between 1..65535", e.getMessage());
    }
    try {
      builder.setData(new byte[65536]);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Data length is between 1..65535", e.getMessage());
    }
  }

  @Test
  public void testClearData() {
    assertEquals(new CommandAPDU(new byte[] { (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00 }), builder.setData((byte) 0x11).clearData()
        .build());
  }

  @Test
  public void testSetLe() {
    assertEquals(new CommandAPDU(new byte[] { (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x10 }), builder.setLe(16).build());
    assertEquals(new CommandAPDU(new byte[] { (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00 }),
        builder.setLe(512).build());
  }

  @Test
  public void testClearLe() {
    assertEquals(new CommandAPDU(new byte[] { (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00 }), builder.setLe(512).clearLe().build());
  }

  @Test
  public void testSetLeException() {
    try {
      builder.setLe(0);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Le is between 1..65535", e.getMessage());
    }
    try {
      builder.setLe(65536);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Le is between 1..65535", e.getMessage());
    }
  }

}
