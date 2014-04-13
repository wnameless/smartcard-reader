smartcard-reader
=============
A smartcard reader util for Java.

##Purpose
Intend to make the creation of CommandAPDU easier and get the ResponseAPDUs from all CardChannels at once.

#Maven Repo
```xml
<dependency>
    <groupId>com.github.wnameless</groupId>
    <artifactId>smartcard-reader</artifactId>
    <version>1.1.0</version>
</dependency>
```

#Quick Start
```java
// It's an example of reading a TWN Health Insurance Card
CommandAPDU cmd1 = APDU.builder().setINS(INS.SELECT_FILE).setP1((byte) 0x04)
                                 .setData("D1580000010000000000000000001100").build(); // Lc field is set automatically by given data length
CommandAPDU cmd2 = APDU.builder().setINS(INS.GET_DATA).setP1((byte) 0x11)
                                 .setData("0000").build());

ListMultimap<CardTerminal, ResponseAPDU> res // Each value stores all ResponseAPDUs of a CardTerminal
    = CardReader.getInstance().read(cmd1, cmd2);
```

#Feature
Automated reader.
```java
AutomatedReader reader = new AutomatedReader(cmd1, cmd2);
reader.reading(1000, new CardTask() { // It reads from all CardChannels every second(1000ms)

    public void execute(CardTerminal terminal, List<ResponseAPDU> responses) {
        ...
    }

});

...

reader.stop();
```
