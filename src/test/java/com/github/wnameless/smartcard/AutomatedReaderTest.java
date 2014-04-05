package com.github.wnameless.smartcard;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.github.wnameless.smartcard.AutomatedReader;
import com.github.wnameless.smartcard.CardReader;
import com.github.wnameless.smartcard.CardResponse;
import com.github.wnameless.smartcard.CardTask;

public class AutomatedReaderTest {

  private AutomatedReader ar;

  @Before
  public void setUp() throws Exception {
    ar = new AutomatedReader(CardReader.ReadProfileAPDU, new CardTask() {

      @Override
      public void execute(Set<CardResponse> responses) {}

    });
  }

  @Test
  public void testConstructor() {
    assertTrue(ar instanceof AutomatedReader);
  }

}
