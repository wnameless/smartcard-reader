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

import java.util.List;

import javax.smartcardio.CardTerminal;

/**
 * 
 * {@link CardTask} is used to define a {@link CardReader} task.
 * 
 */
@SuppressWarnings("restriction")
public interface CardTask {

  /**
   * Yields a List of {@link CardResponse} to the block.
   * 
   * @param terminal
   *          a CardTerminal
   * @param responses
   *          a List of {@link CardResponse}
   */
  void execute(CardTerminal terminal, List<CardResponse> responses);

}
