package wmw.util.smartcard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.smartcardio.CommandAPDU;
import javax.swing.Timer;

import com.google.common.base.Objects;

import static com.google.common.collect.Sets.newHashSet;

@SuppressWarnings("restriction")
public final class AutomatedReader {

  private final CommandAPDU command;
  private final CardTask task;

  public AutomatedReader(CommandAPDU command, int time, CardTask task) {
    this.command = command;
    this.task = task;
    reading(time);
  }

  private void reading(int time) {
    final Set<CardResponse> lastResponses = newHashSet();
    Timer timer = new Timer(time, new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        Set<CardResponse> responses = CardReader.read(command);
        if (lastResponses.addAll(responses) && !lastResponses.isEmpty()) {
          lastResponses.clear();
          lastResponses.addAll(responses);
          task.execute(responses);
        }
      }

    });
    timer.start();
  }

  public String toString() {
    return Objects.toStringHelper(this.getClass()).add("Command", command)
        .add("Task", task).toString();
  }

}
