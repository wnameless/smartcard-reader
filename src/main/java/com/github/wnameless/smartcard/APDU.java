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
@RejectNull
public final class APDU {

  private APDU() {}

  /**
   * Returns a builder of CommandAPDU.
   * 
   * @return a {@link APDUBuilder}
   */
  public static APDUBuilder builder() {
    return new APDUBuilder();
  }

  /**
   * 
   * {@link APDUBuilder} is designed to hold input data from user before a
   * CommandAPDU is created.
   *
   */
  @RejectNull
  public static class APDUBuilder {

    private final byte[] apdu = new byte[4];
    private byte[] lc = null;
    private byte[] data = null;
    private byte[] le = null;

    public APDUBuilder setCLA(byte claByte) {
      apdu[0] = claByte;
      return this;
    }

    public APDUBuilder setINS(byte insByte) {
      apdu[1] = insByte;
      return this;
    }

    public APDUBuilder setP1(byte p1Byte) {
      apdu[2] = p1Byte;
      return this;
    }

    public APDUBuilder setP2(byte p2Byte) {
      apdu[3] = p2Byte;
      return this;
    }

    private APDUBuilder setLc(int lcLength) {
      checkArgument(lcLength >= 1 && lcLength <= 65535,
          "Lc is between 1..65535");
      data = lengthOfData(lcLength);
      return this;
    }

    private byte[] lengthOfData(int length) {
      if (length < 255) {
        lc = new byte[] { (byte) length };
      } else {
        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.position(1);
        lc = buffer.putShort((short) length).array();
      }
      return new byte[length];
    }

    private APDUBuilder clearLc() {
      lc = null;
      data = null;
      return this;
    }

    public APDUBuilder setData(byte... dataBytes) {
      checkArgument(dataBytes.length >= 1 && dataBytes.length <= 65535,
          "Data length is between 1..65535");
      setLc(dataBytes.length);
      data = dataBytes;
      return this;
    }

    public APDUBuilder clearData() {
      clearLc();
      return this;
    }

    public APDUBuilder setLe(int leLength) {
      checkArgument(leLength >= 1 && leLength <= 65536,
          "Le is between 1..65535");
      le = lengthOfData(leLength);
      return this;
    }

    public APDUBuilder clearLe() {
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

}
