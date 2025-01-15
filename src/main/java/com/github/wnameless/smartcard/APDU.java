/**
 *
 * @author Wei-Ming Wu
 *
 *
 *         Copyright 2014 Wei-Ming Wu
 *
 *         Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 *         except in compliance with the License. You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *         Unless required by applicable law or agreed to in writing, software distributed under the
 *         License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *         either express or implied. See the License for the specific language governing
 *         permissions and limitations under the License.
 *
 */
package com.github.wnameless.smartcard;

import static com.google.common.base.Preconditions.checkArgument;
import java.nio.ByteBuffer;
import javax.smartcardio.CommandAPDU;
import com.google.common.primitives.Bytes;
import net.sf.rubycollect4j.util.ByteUtils;

/**
 * 
 * {@link APDU} is a helper class designed to make the creation of a CommandAPDU easier.
 *
 */
public final class APDU {

  private APDU() {}

  /**
   * Returns a builder of CommandAPDU.
   * 
   * @return {@link APDUBuilder}
   */
  public static APDUBuilder builder() {
    return new APDUBuilder();
  }

  /**
   * 
   * {@link APDUBuilder} is designed to hold input data from user before a CommandAPDU is created.
   *
   */
  public static class APDUBuilder {

    private final byte[] apdu = new byte[4];
    private byte[] lc = null;
    private byte[] data = null;
    private byte[] le = null;

    /**
     * Sets the CLA byte, default is 0x00.
     * 
     * @param claByte a CLA byte
     * @return this {@link APDUBuilder}
     */
    public APDUBuilder setCLA(byte claByte) {
      apdu[0] = claByte;
      return this;
    }

    /**
     * Sets the INS byte, default is 0x00.
     * 
     * @param insByte a INS byte
     * @return this {@link APDUBuilder}
     */
    public APDUBuilder setINS(byte insByte) {
      apdu[1] = insByte;
      return this;
    }

    /**
     * Sets the P1 byte, default is 0x00.
     * 
     * @param p1Byte a P1 byte
     * @return this {@link APDUBuilder}
     */
    public APDUBuilder setP1(byte p1Byte) {
      apdu[2] = p1Byte;
      return this;
    }

    /**
     * Sets the P2 byte, default is 0x00.
     * 
     * @param p2Byte a byte
     * @return this {@link APDUBuilder}
     */
    public APDUBuilder setP2(byte p2Byte) {
      apdu[3] = p2Byte;
      return this;
    }

    private APDUBuilder setLc(int lcLength) {
      lc = lengthOfData(lcLength);
      return this;
    }

    private byte[] lengthOfData(int length) {
      byte[] lenBytes;
      if (length < 256) {
        lenBytes = new byte[] {(byte) length};
      } else {
        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.position(1);
        lenBytes = buffer.putShort((short) length).array();
      }
      return lenBytes;
    }

    private APDUBuilder clearLc() {
      lc = null;
      data = null;
      return this;
    }

    /**
     * Sets the DATA bytes, default is empty.<br>
     * If the DATA is set, a Lc bytes is set automatically as well.
     * 
     * @param dataBytes the DATA bytes
     * @return this {@link APDUBuilder}
     */
    public APDUBuilder setData(byte... dataBytes) {
      checkArgument(dataBytes.length >= 1 && dataBytes.length <= 65535,
          "Data length is between 1..65535");
      setLc(dataBytes.length);
      data = dataBytes;
      return this;
    }

    /**
     * Sets the DATA bytes by given HEX string, default is empty.<br>
     * If the DATA is set, a Lc bytes are set automatically as well.
     * 
     * @param hexString a hex string
     * @return this {@link APDUBuilder}
     * @throws IllegalArgumentException if hexadecimal string is invalid
     */
    public APDUBuilder setData(String hexString) {
      byte[] bytes = ByteUtils.fromHexString(hexString);
      if (bytes.length == 0) {
        lc = null;
        data = null;
      } else {
        data = bytes;
        setLc(data.length);
      }
      return this;
    }

    /**
     * Deletes the DATA bytes.<br>
     * If the DATA is deleted, the Lc bytes are deleted automatically as well.
     * 
     * @return this {@link APDUBuilder}
     */
    public APDUBuilder clearData() {
      clearLc();
      return this;
    }

    /**
     * Sets the Le bytes, default is empty.
     * 
     * @param leLength length of Le
     * @return this {@link APDUBuilder}
     */
    public APDUBuilder setLe(int leLength) {
      checkArgument(leLength >= 1 && leLength <= 65535, "Le is between 1..65535");
      le = lengthOfData(leLength);
      return this;
    }

    /**
     * Deletes the Le bytes.
     * 
     * @return this {@link APDUBuilder}
     */
    public APDUBuilder clearLe() {
      le = null;
      return this;
    }

    /**
     * Returns a CommandAPDU by user given data.
     * 
     * @return CommandAPDU
     */
    public CommandAPDU build() {
      byte[] finalApdu = apdu;
      if (lc != null) finalApdu = Bytes.concat(apdu, lc, data);
      if (le != null) finalApdu = Bytes.concat(finalApdu, le);
      return new CommandAPDU(finalApdu);
    }

  }

}
