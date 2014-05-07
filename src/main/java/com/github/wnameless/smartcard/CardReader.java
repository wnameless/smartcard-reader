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

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

import com.github.wnameless.nullproof.annotation.RejectNull;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * 
 * {@link CardReader} is a friendly wrapper to Java Smartcard API. It can
 * execute a set of CommandAPDU to all or specified card readers and return the
 * responses back.
 * <P>
 * It's a singleton.
 * 
 */
@RejectNull
public final class CardReader {

  private static final Logger logger = Logger.getLogger(CardReader.class
      .getName());

  private static final TerminalFactory factory = TerminalFactory.getDefault();
  private static final CardReader INSTANCE = new CardReader();

  private CardReader() {}

  /**
   * Returns the instance of {@link CardReader}.
   * 
   * @return {@link CardReader}
   */
  public static CardReader getInstance() {
    return INSTANCE;
  }

  /**
   * Returns a Multimap&lt;CardTerminal, ResponseAPDU&gt; after executing a set
   * of CommandAPDU on all Smartcard readers.
   * 
   * @param commands
   *          an array of CommandAPDU
   * @return ListMultimap&lt;CardTerminal, ResponseAPDU&gt
   */
  public ListMultimap<CardTerminal, ResponseAPDU> read(CommandAPDU... commands) {
    return read(Arrays.asList(commands));
  }

  /**
   * Returns a Multimap&lt;CardTerminal, ResponseAPDU&gt; after executing a set
   * of CommandAPDU on all Smartcard readers.
   * 
   * @param commands
   *          a List of CommandAPDU
   * @return ListMultimap&lt;CardTerminal, ResponseAPDU&gt;
   */
  public ListMultimap<CardTerminal, ResponseAPDU> read(
      List<CommandAPDU> commands) {
    ListMultimap<CardTerminal, ResponseAPDU> responses =
        ArrayListMultimap.create();
    for (CardTerminal terminal : getCardTerminals()) {
      responses.putAll(terminal, getResponse(terminal, commands));
    }
    return responses;
  }

  /**
   * Returns a List of ResponseAPDU of executed command on specified card
   * terminal.
   * 
   * @param terminal
   *          a CardTerminal
   * @param commands
   *          an array of CommandAPDU
   * @return List of ResponseAPDU
   */
  public List<ResponseAPDU> readOnTerminal(CardTerminal terminal,
      CommandAPDU... commands) {
    return readOnTerminal(terminal, Arrays.asList(commands));
  }

  /**
   * Returns a List of ResponseAPDU of executed command on specified card
   * terminal.
   * 
   * @param terminal
   *          a CardTerminal
   * @param commands
   *          a List of CommandAPDU
   * @return List of ResponseAPDU
   */
  public List<ResponseAPDU> readOnTerminal(CardTerminal terminal,
      List<CommandAPDU> commands) {
    return getResponse(terminal, commands);
  }

  private List<ResponseAPDU> getResponse(CardTerminal terminal,
      List<CommandAPDU> commands) {
    List<ResponseAPDU> responses = newArrayList();
    try {
      Card card = terminal.connect("*");
      CardChannel channel = card.getBasicChannel();
      for (CommandAPDU command : commands) {
        ResponseAPDU response = channel.transmit(command);
        responses.add(response);
      }
    } catch (CardException e) {
      logger.log(Level.SEVERE, null, e.getMessage());
    }
    return responses;
  }

  /**
   * Returns all card terminals of the system.
   * 
   * @return List of CardTerminal
   */
  public List<CardTerminal> getCardTerminals() {
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
