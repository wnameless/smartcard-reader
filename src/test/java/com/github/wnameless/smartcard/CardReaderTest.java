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

public class CardReaderTest {

  public static void main(String[] args) {
    CardReader cr = CardReader.getInstance();
    System.out.println(cr.read(
        APDU.builder()
            .setINS(INS.SELECT_FILE)
            .setP1((byte) 0x04)
            .setData((byte) 0xD1, (byte) 0x58, (byte) 0x00, (byte) 0x00,
                (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x11, (byte) 0x00).build(),
        APDU.builder().setINS(INS.GET_DATA).setP1((byte) 0x11)
            .setData((byte) 0x00, (byte) 0x00).build()));
  }

}
