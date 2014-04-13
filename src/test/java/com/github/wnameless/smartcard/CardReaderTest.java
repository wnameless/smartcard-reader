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
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CardReaderTest {

  CardReader reader;
  CommandAPDU[] commands;

  @Mock
  CardTerminal terminal;

  @Mock
  Card card;

  @Mock
  CardChannel channel;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    when(terminal.connect(any(String.class))).thenReturn(card);
    when(card.getBasicChannel()).thenReturn(channel);

    reader = CardReader.getInstance();
    commands =
        new CommandAPDU[] {
            APDU.builder()
                .setINS(INS.SELECT_FILE)
                .setP1((byte) 0x04)
                .setData((byte) 0xD1, (byte) 0x58, (byte) 0x00, (byte) 0x00,
                    (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                    (byte) 0x00, (byte) 0x00, (byte) 0x11, (byte) 0x00).build(),
            APDU.builder().setINS(INS.GET_DATA).setP1((byte) 0x11)
                .setData((byte) 0x00, (byte) 0x00).build() };
  }

  @Test
  public void testSingleton() {
    assertSame(reader, CardReader.getInstance());
  }

  @Test
  public void testReadOnTermial() throws Exception {
    assertEquals(commands.length, reader.readOnTerminal(terminal, commands)
        .size());
    verify(terminal.connect(any(String.class)), times(1));
  }

  @Test
  public void testToString() {
    assertEquals(CardReader.class.getSimpleName(), reader.toString());
  }

}
