/**
 *
 * @author Wei-Ming Wu
 *
 *
 * Copyright 2013 Wei-Ming Wu
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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

import com.google.common.base.Objects;

/**
 * 
 * CardResponse holds the information of the result after reading a smart card.
 * 
 */
public final class CardResponse {

  private final int channelNum;
  private final byte[] data;

  /**
   * Creates a CardResponse.
   * 
   * @param channelNum
   *          number of channel
   * @param data
   *          an array of byte
   */
  public CardResponse(int channelNum, byte[] data) {
    this.channelNum = channelNum;
    this.data = checkNotNull(data);
  }

  /**
   * Returns the number of channel.
   * 
   * @return an int
   */
  public int getChannelNum() {
    return channelNum;
  }

  /**
   * Returns the data after reading a smart card.
   * 
   * @return an array of byte
   */
  public byte[] getData() {
    return Arrays.copyOf(data, data.length);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof CardResponse) {
      CardResponse cr = (CardResponse) o;
      return channelNum == cr.channelNum && Arrays.equals(data, cr.data);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(channelNum, Arrays.hashCode(data));
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(getClass()).add("ChannelNum", channelNum)
        .add("Data", Arrays.toString(data)).toString();
  }

}
