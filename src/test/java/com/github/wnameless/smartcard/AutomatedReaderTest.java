package com.github.wnameless.smartcard;

import static com.github.wnameless.smartcard.SmartCardAPDU.READ_PROFILE;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class AutomatedReaderTest {

  private AutomatedReader ar;

  @Before
  public void setUp() throws Exception {
    ar = new AutomatedReader(READ_PROFILE, new CardTask() {

      @Override
      public void execute(Set<CardResponse> responses) {}

    });
  }

  @Test
  public void testConstructor() {
    assertTrue(ar instanceof AutomatedReader);
  }

}
