package com.study.poet.build.typespec;

import com.study.poet.Abs;
import com.study.poet.IInter;
import java.lang.Override;
import java.lang.Runnable;
import java.lang.String;
import java.lang.System;

/**
 * add Doc
 */
public class PrintClass<R> extends Abs implements IInter<R>, Runnable {
  @Override
  public void print(String msg) {
    System.out.print(msg);
  }

  /**
   * Override Runnable run function
   */
  @Override
  public void run() {
    print("Running");
  }
}
