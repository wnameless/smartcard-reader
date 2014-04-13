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

/**
 * 
 * {@link INS} defines all Smartcard INS codes of ISO/IEC 7816.
 *
 */
public final class INS {

  public static final byte ERASE_BINARY = (byte) 0x0E;
  public static final byte VERIFY = (byte) 0x20;
  public static final byte MANAGE_CHANNEL = (byte) 0x70;
  public static final byte EXTERNAL_AUTHENTICATE = (byte) 0x82;
  public static final byte GET_CHALLENGE = (byte) 0x84;
  public static final byte INTERNAL_AUTHENTICATE = (byte) 0x88;
  public static final byte SELECT_FILE = (byte) 0xA4;
  public static final byte READ_BINARY = (byte) 0xB0;
  public static final byte READ_RECORD = (byte) 0xB2;
  public static final byte GET_RESPONSE = (byte) 0xC0;
  public static final byte ENVELOPE = (byte) 0xC2;
  public static final byte GET_DATA = (byte) 0xCA;
  public static final byte WRITE_BINARY = (byte) 0xD0;
  public static final byte WRITE_RECORD = (byte) 0xD2;
  public static final byte UPDATE_BINARY = (byte) 0xD6;
  public static final byte PUT_DATA = (byte) 0xDA;
  public static final byte UPDATE_DATA = (byte) 0xDC;
  public static final byte APPEND_RECORD = (byte) 0xE2;

}
