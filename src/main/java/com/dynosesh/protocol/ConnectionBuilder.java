package com.dynosesh.protocol;

import java.util.Objects;

/**
 * Created by rorymalcolm on 29/06/2018.
 *
 * @author rorymalcolm
 */
public class ConnectionBuilder {
  private Connection connection;
  private NodeBuilder nodeBuilder;

  /**
   * Non-public ConnectionBuilder constructor, this is ONLY called from the NodeBuilder object.
   * <p>
   * The NodeBuilder object passes an instance of itself down when .connection() is called on a
   * node that has been declared with the internal DSL.
   * </p>
   *
   * @param nodeBuilder The parent NodeBuilder object
   */
  ConnectionBuilder(NodeBuilder nodeBuilder) {
    connection = new Connection();
    this.nodeBuilder = nodeBuilder;
  }

  /**
   * Returns the value of the built connection.
   *
   * @return The connection
   */
  Connection getConnection() {
    return connection;
  }

  /**
   * Defines the actor for the connection contained within the object
   *
   * @param actorAddress The actor's address
   * @return The current object for chaining methods
   */
  public ConnectionBuilder actor(String actorAddress) {
    this.connection.setAddress(actorAddress);
    return this;
  }

  /**
   * The finalisation step of the connection aspect of the DSL - returns the parent nodebuilder
   * after setting the node that it is connecting to.
   *
   * @param node The node to connect to
   * @return The parent nodebuilder
   */
  public NodeBuilder to(Node node) {
    this.connection.setNode(node);
    if (Objects.equals(this.connection.getAddress(), "")) {
      throw new IllegalArgumentException("No actor address defined");
    }
    return this.nodeBuilder;
  }
}