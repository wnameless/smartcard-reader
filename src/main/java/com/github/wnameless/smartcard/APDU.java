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

import static com.google.common.base.Preconditions.checkArgument;

import java.nio.ByteBuffer;

import javax.smartcardio.CommandAPDU;

import com.github.wnameless.nullproof.annotation.RejectNull;
import com.google.common.primitives.Bytes;

/**
 * 
 * {@link APDU} is a helper class designed to make the creation of a CommandAPDU
 * easier.
 *
 */
@SuppressWarnings("restriction")
@RejectNull
public final class APDU {

  /**
   * Returns a builder of CommandAPDU.
   * 
   * @return a {@link ExtendedAPDUHolder}
   */
  public static ExtendedAPDUHolder builder() {
    return new ExtendedAPDUHolder();
  }

  /**
   * 
   * {@link ExtendedAPDUHolder} is designed to hold input data from user before
   * a CommandAPDU is created.
   *
   */
  @RejectNull
  public static class ExtendedAPDUHolder {

    private final byte[] apdu = new byte[4];
    private byte[] lc = null;
    private byte[] data = null;
    private byte[] le = null;

    public ExtendedAPDUHolder setCLA(byte claByte) {
      apdu[0] = claByte;
      return this;
    }

    public ExtendedAPDUHolder setINS(byte insByte) {
      apdu[1] = insByte;
      return this;
    }

    public ExtendedAPDUHolder setP1(byte p1Byte) {
      apdu[2] = p1Byte;
      return this;
    }

    public ExtendedAPDUHolder setP2(byte p2Byte) {
      apdu[3] = p2Byte;
      return this;
    }

    @Deprecated
    public ExtendedAPDUHolder setLc(int lcLength) {
      checkArgument(lcLength >= 1 && lcLength <= 65535,
          "Lc is between 1..65535");
      if (lcLength < 255) {
        lc = new byte[] { (byte) lcLength };
      } else {
        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.position(1);
        lc = buffer.putShort((short) lcLength).array();
      }
      data = new byte[lcLength];
      return this;
    }

    @Deprecated
    public ExtendedAPDUHolder clearLc() {
      lc = null;
      data = null;
      return this;
    }

    public ExtendedAPDUHolder setData(byte... dataBytes) {
      checkArgument(dataBytes.length >= 1 && dataBytes.length <= 65535,
          "Data length is between 1..65535");
      setLc(dataBytes.length);
      data = dataBytes;
      return this;
    }

    public ExtendedAPDUHolder clearData() {
      lc = null;
      data = null;
      return this;
    }

    public ExtendedAPDUHolder setLe(int leLength) {
      checkArgument(leLength >= 1 && leLength <= 65536,
          "Le is between 1..65535");
      if (leLength < 255) {
        le = new byte[] { (byte) leLength };
      } else {
        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.position(1);
        le = buffer.putShort((short) leLength).array();
      }
      return this;
    }

    public ExtendedAPDUHolder clearLe() {
      le = null;
      return this;
    }

    /**
     * Returns a CommandAPDU by user given data.
     * 
     * @return a CommandAPDU
     */
    public CommandAPDU build() {
      byte[] finalApdu = apdu;
      if (lc != null)
        finalApdu = Bytes.concat(apdu, lc, data);
      if (le != null)
        finalApdu = Bytes.concat(finalApdu, le);
      return new CommandAPDU(finalApdu);
    }

  }

  /**
   * 
   * {@link INS APDU.INS} defines all Smartcard INS codes of ISO/IEC 7816.
   *
   */
  public static class INS {

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

}
