/**
 *
 * @author Wei-Ming Wu
 *
 *
 *         Copyright 2013 Wei-Ming Wu
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.swing.Timer;

/**
 * 
 * {@link AutomatedReader} can continuously perform certain {@link CardTask} by a time interval.
 * 
 */
public final class AutomatedReader {

  private final List<CommandAPDU> commands;
  private Timer timer;

  /**
   * Returns an {@link AutomatedReader}.
   * 
   * @param commands an array of CommandAPDU
   */
  public AutomatedReader(CommandAPDU... commands) {
    Objects.requireNonNull(commands);
    this.commands = new ArrayList<>(Arrays.asList(commands));
  }

  /**
   * Returns an {@link AutomatedReader}.
   * 
   * @param commands a List of CommandAPDU
   */
  public AutomatedReader(List<CommandAPDU> commands) {
    Objects.requireNonNull(commands);
    this.commands = new ArrayList<>(commands);
  }

  /**
   * Starts to read smartcards and performs a {@link CardTask} continuously. This {@link CardTask}
   * is only performed if there is any CHANGE on the responses from card terminals.<br>
   * <br>
   * If this method is called again, the previous {@link CardTask} will be stopped and the new
   * {@link CardTask} will perform.
   * 
   * @param time in milliseconds
   * @param task a {@link CardTask}
   */
  public synchronized void reading(int time, final CardTask task) {
    Objects.requireNonNull(task);
    stop();
    final Set<ResponseAPDU> lastResponses = new LinkedHashSet<>();
    timer = new Timer(time, (event) -> {
      Map<CardTerminal, List<ResponseAPDU>> responses = CardReader.getInstance().read(commands);
      List<ResponseAPDU> allResponseAPDUs =
          responses.values().stream().flatMap(r -> r.stream()).toList();
      if (lastResponses.addAll(allResponseAPDUs) && !lastResponses.isEmpty()) {
        lastResponses.clear();
        lastResponses.addAll(allResponseAPDUs);
        for (CardTerminal terminal : responses.keySet()) {
          task.execute(terminal, responses.get(terminal));
        }
      }
    });
    timer.start();
  }

  /**
   * Stops the reading of smartcards.
   */
  public synchronized void stop() {
    if (timer != null) timer.stop();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof AutomatedReader) {
      AutomatedReader ar = (AutomatedReader) o;
      return Objects.equals(commands, ar.commands);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(commands);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" + commands + "}";
  }

}
