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

import javax.smartcardio.CommandAPDU;

@SuppressWarnings("restriction")
public final class SmartCardAPDU {

  public static final byte[] SELECT = new byte[] { (byte) 0x00, (byte) 0xA4,
      (byte) 0x04, (byte) 0x00, (byte) 0x10, (byte) 0xD1, (byte) 0x58,
      (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00,
      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
      (byte) 0x00, (byte) 0x00, (byte) 0x11, (byte) 0x00 };

  public static final CommandAPDU READ_PROFILE = new CommandAPDU(new byte[] {
      (byte) 0x00, (byte) 0xca, (byte) 0x11, (byte) 0x00, (byte) 0x02,
      (byte) 0x00, (byte) 0x00 });

  private SmartCardAPDU() {}

}
