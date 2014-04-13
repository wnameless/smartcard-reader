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

import java.util.Arrays;

import javax.smartcardio.CommandAPDU;

import org.junit.Before;
import org.junit.Test;

import com.google.common.testing.NullPointerTester;

public class AutomatedReaderTest {

  AutomatedReader reader;
  CommandAPDU[] commands;

  @Before
  public void setUp() throws Exception {
    commands =
        new CommandAPDU[] {
            APDU.builder().setINS(INS.SELECT_FILE).setP1((byte) 0x04)
                .setData("D1580000010000000000000000001100").build(),
            APDU.builder().setINS(INS.GET_DATA).setP1((byte) 0x11)
                .setData("0000").build() };
    reader = new AutomatedReader(commands);
  }

  @Test
  public void testAllNPE() {
    NullPointerTester tester = new NullPointerTester();
    tester.testAllPublicConstructors(AutomatedReader.class);
    tester.testAllPublicInstanceMethods(reader);
    tester.testAllPublicStaticMethods(AutomatedReader.class);
  }

  @Test
  public void testConstructors() {
    assertTrue(new AutomatedReader(commands) instanceof AutomatedReader);
    assertTrue(new AutomatedReader(Arrays.asList(commands)) instanceof AutomatedReader);
  }

  @Test
  public void testStop() {
    reader.stop();
  }

  @Test
  public void testToString() {
    assertEquals(
        "AutomatedReader{["
            + "CommmandAPDU: 21 bytes, nc=16, ne=0, CommmandAPDU: 7 bytes, nc=2, ne=0]}",
        reader.toString());
  }

}
