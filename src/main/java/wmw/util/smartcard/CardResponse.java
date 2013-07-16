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
package wmw.util.smartcard;

import java.util.Arrays;

import com.google.common.base.Objects;

public final class CardResponse {

  private final int channelNum;
  private final byte[] data;

  public CardResponse(int channelNum, byte[] data) {
    this.channelNum = channelNum;
    this.data = data;
  }

  public int getChannelNum() {
    return channelNum;
  }

  public byte[] getData() {
    return Arrays.copyOf(data, data.length);
  }

  public boolean equals(Object o) {
    if (o instanceof CardResponse) {
      CardResponse cr = (CardResponse) o;
      return channelNum == cr.channelNum && Arrays.equals(data, cr.data);
    }
    return false;
  }

  public int hashCode() {
    return Objects.hashCode(channelNum, Arrays.hashCode(data));
  }

  public String toString() {
    return Objects.toStringHelper(this.getClass())
        .add("ChannelNum", channelNum).add("Data", data).toString();
  }

}
