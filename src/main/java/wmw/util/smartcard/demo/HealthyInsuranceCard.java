package wmw.util.smartcard.demo;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Set;

import javax.smartcardio.CommandAPDU;

import wmw.util.smartcard.AutomatedReader;
import wmw.util.smartcard.CardResponse;
import wmw.util.smartcard.CardTask;

@SuppressWarnings("restriction")
public final class HealthyInsuranceCard {

  private static byte[] ReadProfileAPDU = new byte[] { (byte) 0x00,
      (byte) 0xca, (byte) 0x11, (byte) 0x00, (byte) 0x02, (byte) 0x00,
      (byte) 0x00 };

  private static CommandAPDU command = new CommandAPDU(ReadProfileAPDU);

  public static void main(String[] args) {
    new AutomatedReader(command, 1000, new CardTask() {

      @Override
      public void execute(Set<CardResponse> responses) {
        for (CardResponse cr : responses) {
          try {
            String profile =
                new String(Arrays.copyOfRange(cr.getData(), 12, 32), "Big5")
                    .trim();
            System.out.println(profile);
          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          }
        }
      }

    });
    while (true) {}
  }

}
