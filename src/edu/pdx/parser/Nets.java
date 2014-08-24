/*
 * Copyright 2014 Arindam Bannerjee
 * This work is distributed under the terms of the "MIT license". Please see the file
 * LICENSE in this distribution for license terms.
 *
 */

package edu.pdx.parser;

public class Nets {
  
  public int netId;
  public String compName;
  public int pin;

  public Nets (int Id, String comp, int pin) {
    this.netId = Id;
    this.compName = comp;
    this.pin = pin;
  }
}
