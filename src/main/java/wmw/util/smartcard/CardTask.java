package wmw.util.smartcard;

import java.util.Set;

public interface CardTask {

  void execute(Set<CardResponse> responses);

}
