module com.github.wnameless.smartcard {
  requires java.desktop;
  requires java.logging;
  requires transitive java.smartcardio;
  requires net.sf.rubycollect4j;

  exports com.github.wnameless.smartcard;
}
