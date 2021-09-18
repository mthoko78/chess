package com.mthoko.learners.common.util.encryption;

public class ModeOfOperation {

  public final int OFB;

  public final int CFB;

  public final int CBC;

  public ModeOfOperation(int OFB, int CBF, int CBC) {
    this.OFB = OFB;
    this.CFB = CBF;
    this.CBC = CBC;
  }

}
