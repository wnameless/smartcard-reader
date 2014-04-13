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
import static com.google.common.collect.Sets.newHashSet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;

import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.swing.Timer;

import com.github.wnameless.nullproof.annotation.RejectNull;
import com.google.common.base.Objects;
import com.google.common.collect.ListMultimap;

/**
 * 
 * {@link AutomatedReader} can continuously perform certain {@link CardTask} by
 * a time interval.
 * 
 */
@RejectNull
public final class AutomatedReader {

  private final List<CommandAPDU> commands;
  private Timer timer;

  /**
   * Returns an {@link AutomatedReader}.
   * 
   * @param commands
   *          an Array of CommandAPDU
   */
  public AutomatedReader(CommandAPDU... commands) {
    this.commands = newArrayList(commands);
  }

  /**
   * Returns an {@link AutomatedReader}.
   * 
   * @param commands
   *          a List of CommandAPDU
   */
  public AutomatedReader(List<CommandAPDU> commands) {
    this.commands = newArrayList(commands);
  }

  /**
   * Starts to read smartcards and performs a {@link CardTask} continuously.
   * This {@link CardTask} is only performed if there is any CHANGE on the
   * responses from card terminals.<br>
   * <br>
   * If this method is called again, the previous {@link CardTask} will be
   * stopped and the new {@link CardTask} will perform.
   * 
   * @param time
   *          in milliseconds
   * @param task
   *          a {@link CardTask}
   */
  public synchronized void reading(int time, final CardTask task) {
    stop();
    final Set<ResponseAPDU> lastResponses = newHashSet();
    timer = new Timer(time, new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        ListMultimap<CardTerminal, ResponseAPDU> responses =
            CardReader.getInstance().read(commands);
        if (lastResponses.addAll(responses.values())
            && !lastResponses.isEmpty()) {
          lastResponses.clear();
          lastResponses.addAll(responses.values());
          for (CardTerminal terminal : responses.keys()) {
            task.execute(terminal, responses.get(terminal));
          }
        }
      }

    });
    timer.start();
  }

  /**
   * Stops the reading of smartcards.
   */
  public synchronized void stop() {
    if (timer != null)
      timer.stop();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof AutomatedReader) {
      AutomatedReader ar = (AutomatedReader) o;
      return Objects.equal(commands, ar.commands);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(commands);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(getClass()).addValue(commands).toString();
  }

}
