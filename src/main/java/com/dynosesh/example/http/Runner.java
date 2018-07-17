package com.dynosesh.example.http;

import com.dynosesh.ProtocolMonitor;
import com.dynosesh.example.http.types.Certificate;
import com.dynosesh.example.http.types.CertificateRequest;
import com.dynosesh.example.http.types.CertificateVerify;
import com.dynosesh.example.http.types.ChangeCipherSpec;
import com.dynosesh.example.http.types.ClientKeyExchange;
import com.dynosesh.example.http.types.Finished;
import com.dynosesh.example.http.types.Hello;
import com.dynosesh.example.http.types.HelloDone;
import com.dynosesh.example.http.types.ServerKeyExchange;
import com.dynosesh.protocol.Protocol;
import com.dynosesh.protocol.ProtocolBuilder;

/**
 * Created by Rory Malcolm on 09/07/2018.
 *
 * @author Rory Malcolm (rorymckenziemalcolm@gmail.com)
 */
public class Runner {

  private static final String CLIENT_ADDRESS = "0";
  private static final String SERVER_ADDRESS = "1";

  /**
   * Starts the protocol showcase
   *
   * @param args Arguments from the command line - unused
   */
  public static void main(String[] args) {
    ProtocolBuilder protocolBuilder = new ProtocolBuilder();
    firstSectionOfTransmission(protocolBuilder);
    secondSectionOfTransmission(protocolBuilder);
    thirdSectionOfTransmission(protocolBuilder);
    finalSectionOfTransmission(protocolBuilder);
    Protocol protocol = protocolBuilder
        .build();
    ProtocolMonitor monitor = new ProtocolMonitor(protocol);
    Thread monitorThread = new Thread(new ProtocolServer(monitor));
    Thread thread = new Thread(new SenderClient(2000));
    Thread thread1 = new Thread(new ReceiverClient(2001));
    monitorThread.start();
    thread.start();
    thread1.start();
  }

  private static void firstSectionOfTransmission(ProtocolBuilder protocolBuilder) {
    protocolBuilder
        .node()
          .payload(null)
          .connection()
            .actor(CLIENT_ADDRESS)
            .to("1")
        .node()
          .payload(Hello.class)
          .connection()
            .actor(SERVER_ADDRESS)
            .to("2")
        .node()
          .payload(Hello.class)
          .connection()
            .actor(SERVER_ADDRESS)
            .to("3")
          .connection()
            .actor(SERVER_ADDRESS)
            .to("5");
  }

  private static void secondSectionOfTransmission(ProtocolBuilder protocolBuilder) {
    //No key exchange here, just HelloDone from the server then a ClientKeyExchange before
    //the final section
    protocolBuilder
        .node()
          .payload(HelloDone.class)
          .connection()
            .actor(CLIENT_ADDRESS)
            .to("4")
        .node()
          .payload(ClientKeyExchange.class)
          .connection()
            .actor(CLIENT_ADDRESS)
            .to("12");
  }

  private static void thirdSectionOfTransmission(ProtocolBuilder protocolBuilder) {
    protocolBuilder
        .node()
          .payload(Certificate.class)
          .connection()
            .actor(SERVER_ADDRESS)
            .to("6")
        .node()
          .payload(ServerKeyExchange.class)
          .connection()
            .actor(SERVER_ADDRESS)
            .to("7")
        .node()
          .payload(CertificateRequest.class)
          .connection()
            .actor(SERVER_ADDRESS)
            .to("8")
        .node()
          .payload(HelloDone.class)
          .connection()
            .actor(CLIENT_ADDRESS)
            .to("9")
        .node()
          .payload(Certificate.class)
          .connection()
            .actor(CLIENT_ADDRESS)
            .to("10")
        .node()
          .payload(ClientKeyExchange.class)
          .connection()
            .actor(CLIENT_ADDRESS)
            .to("11")
        .node()
          .payload(CertificateVerify.class)
          .connection()
            .actor(CLIENT_ADDRESS)
            .to("12");
  }

  private static void finalSectionOfTransmission(ProtocolBuilder protocolBuilder) {
    protocolBuilder
        .node()
          .payload(ChangeCipherSpec.class)
          .connection()
            .actor(CLIENT_ADDRESS)
            .to("13")
        .node()
          .payload(Finished.class)
          .connection()
            .actor(SERVER_ADDRESS)
            .to("14")
        .node()
          .payload(ChangeCipherSpec.class)
          .connection()
            .actor(SERVER_ADDRESS)
            .to("15")
        .node()
          .payload(Finished.class);
  }
}
