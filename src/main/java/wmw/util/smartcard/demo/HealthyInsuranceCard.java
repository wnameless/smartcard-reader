package wmw.util.smartcard.demo;

import java.util.Arrays;
import java.util.Set;

import wmw.util.smartcard.AutomatedReader;
import wmw.util.smartcard.CardReader;
import wmw.util.smartcard.CardResponse;
import wmw.util.smartcard.CardTask;

import com.ibm.icu.text.CharsetDetector;

public final class HealthyInsuranceCard {

  public static void main(String[] args) {
    new AutomatedReader(CardReader.ReadProfileAPDU, new CardTask() {

      @Override
      public void execute(Set<CardResponse> responses) {
        for (CardResponse cr : responses) {
          try {
            String profile =
                new CharsetDetector().getString(
                    Arrays.copyOfRange(cr.getData(), 12, 32), "UTF-8").trim();
            System.out.println(profile);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }

    }).reading(1000);
    while (true) {}
  }

}
