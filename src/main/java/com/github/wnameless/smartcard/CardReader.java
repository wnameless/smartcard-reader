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

import static com.github.wnameless.smartcard.SmartCardAPDU.SELECT;
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

import com.github.wnameless.nullproof.NullProof;

/**
 * 
 * CardReader is a friendly wrapper to Java smart card API.
 * 
 */
@SuppressWarnings("restriction")
public class CardReader {

  private static final Logger logger = Logger.getLogger(CardReader.class
      .getName());

  private static final CardReader INSTANCE = NullProof.of(CardReader.class);

  CardReader() {}

  public static CardReader getInstance() {
    return INSTANCE;
  }

  /**
   * Returns a Set of CardResponse after executing a CommandAPDU from all smart
   * card readers.
   * 
   * @param command
   *          a CommandAPDU
   * @return a Set of CardResponse
   */
  public Set<CardResponse> read(CommandAPDU command) {
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
  public CardResponse
      readOnTerminal(CardTerminal terminal, CommandAPDU command) {
    return getResponse(terminal, command);
  }

  private CardResponse getResponse(CardTerminal terminal, CommandAPDU command) {
    try {
      Card card = terminal.connect("*");
      CardChannel channel = card.getBasicChannel();
      channel.transmit(new CommandAPDU(SELECT));
      ResponseAPDU response = channel.transmit(command);
      return new CardResponse(channel.getChannelNumber(), response.getData());
    } catch (CardException e) {
      logger.log(Level.SEVERE, null, e.getMessage());
    }
    return null;
  }

  /**
   * Returns all CardTerminals of the system.
   * 
   * @return a List of CardTerminal
   */
  public List<CardTerminal> getCardTerminals() {
    TerminalFactory factory = TerminalFactory.getDefault();
    List<CardTerminal> terminals = emptyList();
    try {
      terminals = factory.terminals().list();
    } catch (CardException e) {
      logger.log(Level.SEVERE, null, e.getMessage());
    }
    return terminals;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
