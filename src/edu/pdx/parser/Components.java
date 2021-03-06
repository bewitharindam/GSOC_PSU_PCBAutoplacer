/*
 * Copyright 2014 Arindam Bannerjee
 * This work is distributed under the terms of the "MIT license". Please see the file
 * LICENSE in this distribution for license terms.
 *
 */

package edu.pdx.parser;

public class Components {

  private String nameOfComp;
  private String nameOfCompPart;
  private int numOfPin;

  public Components (String compName, String partName, int pin) {
    this.nameOfComp = compName;
    this.nameOfCompPart = partName;	
    this.numOfPin = pin;
  }
  public String getNameOfComp(){
    return nameOfComp;
  }
  public String getNameOfCompPart(){
    return nameOfCompPart;
  }
  public int getNumOfPin(){
    return numOfPin;
  }
  public void setNumOfPin(int numOfPin){
    this.numOfPin = numOfPin;
  }
}
