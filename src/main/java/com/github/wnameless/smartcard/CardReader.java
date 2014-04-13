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

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.emptyList;
import static net.sf.rubycollect4j.RubyCollections.newRubyArray;

import java.util.Arrays;
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
 * {@link CardReader} is a friendly wrapper to Java smartcard API.
 * 
 */
@SuppressWarnings("restriction")
public final class CardReader {

  private static final Logger logger = Logger.getLogger(CardReader.class
      .getName());

  private static final CardReader INSTANCE = new CardReader();

  private CardReader() {}

  /**
   * Returns the singleton object of {@link CardReader}.
   * 
   * @return a {@link CardReader}
   */
  public static CardReader getInstance() {
    return INSTANCE;
  }

  public Set<List<CardResponse>> read(CommandAPDU... commands) {
    return read(Arrays.asList(commands));
  }

  /**
   * Returns a Set of {@link CardResponse} after executing a CommandAPDU from
   * all smart card readers.
   * 
   * @param commands
   *          a List of CommandAPDU
   * @return a Set of {@link CardResponse} List
   */
  public Set<List<CardResponse>> read(List<CommandAPDU> commands) {
    Set<List<CardResponse>> responses = newHashSet();
    for (CardTerminal terminal : getCardTerminals()) {
      responses.add(getResponse(terminal, commands));
    }
    responses.remove(null);
    return responses;
  }

  public List<CardResponse> readOnTerminal(CardTerminal terminal,
      CommandAPDU... commands) {
    return readOnTerminal(terminal, Arrays.asList(commands));
  }

  /**
   * Returns a {@link CardResponse} of executed command on specified card
   * terminal. Returns null if no response.
   * 
   * @param terminal
   *          a CardTerminal
   * @param commands
   *          a List of CommandAPDU
   * @return a List of {@link CardResponse}
   */
  public List<CardResponse> readOnTerminal(CardTerminal terminal,
      List<CommandAPDU> commands) {
    return getResponse(terminal, commands);
  }

  private List<CardResponse> getResponse(CardTerminal terminal,
      List<CommandAPDU> commands) {
    List<CardResponse> responses = newRubyArray();
    try {
      Card card = terminal.connect("*");
      CardChannel channel = card.getBasicChannel();
      for (CommandAPDU command : commands) {
        ResponseAPDU res = channel.transmit(command);
        responses.add(new CardResponse(channel.getChannelNumber(), res
            .getData()));
      }
    } catch (CardException e) {
      logger.log(Level.SEVERE, null, e.getMessage());
      throw new RuntimeException(e);
    }
    return responses;
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
      throw new RuntimeException(e);
    }
    return terminals;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
