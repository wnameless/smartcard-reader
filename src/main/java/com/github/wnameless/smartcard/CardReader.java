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

import static com.github.wnameless.smartcard.SmartCardAPDU.Select;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

/**
 * 
 * CardReader is a friendly wrapper to Java smart card API.
 * 
 */
@SuppressWarnings("restriction")
public final class CardReader {

  private CardReader() {}

  /**
   * Executes a CommandAPDU and returns a Set of CardResponse from all smart
   * card readers.
   * 
   * @param command
   *          a CommandAPDU
   * @return a Set of CardResponse
   */
  public static Set<CardResponse> read(CommandAPDU command) {
    checkNotNull(command);
    Set<CardResponse> responses = newHashSet();
    for (CardTerminal terminal : getCardTerminals()) {
      responses.add(getResponse(terminal, command));
    }
    responses.remove(null);
    return responses;
  }

  /**
   * Returns a CardResponse of executed command on specified card terminal.
   * 
   * @param terminal
   *          a CardTerminal
   * @param command
   *          a CommandAPDU
   * @return a CardResponse or null
   */
  public static CardResponse readOnTerminal(CardTerminal terminal,
      CommandAPDU command) {
    return getResponse(checkNotNull(terminal), checkNotNull(command));
  }

  private static CardResponse getResponse(CardTerminal terminal,
      CommandAPDU command) {
    try {
      Card card = terminal.connect("*");
      CardChannel channel = card.getBasicChannel();
      channel.transmit(new CommandAPDU(Select));
      ResponseAPDU response = channel.transmit(command);
      return new CardResponse(channel.getChannelNumber(), response.getData());
    } catch (CardException e) {
      Logger.getLogger(CardReader.class.getName()).log(Level.SEVERE, null,
          e.getMessage());
    }
    return null;
  }

  /**
   * Returns all CardTerminals of the system.
   * 
   * @return a List of CardTerminal
   */
  public static List<CardTerminal> getCardTerminals() {
    TerminalFactory factory = TerminalFactory.getDefault();
    List<CardTerminal> terminals = emptyList();
    try {
      terminals = factory.terminals().list();
    } catch (CardException e) {
      Logger.getLogger(CardReader.class.getName()).log(Level.SEVERE, null,
          e.getMessage());
    }
    return terminals;
  }

}
