package com.study.poet.build.methodspec;

import com.study.poet.Normal;
import java.lang.IllegalArgumentException;
import java.lang.String;
import java.lang.System;

public class Teacher {
  @Normal
  public final <T> int speak(@Normal final String message, T t, String... other) throws
      IllegalArgumentException {
    if (message.equals("a")) {
      throw new IllegalArgumentException();
    } else if (message.equals("b")) {
      System.out.println(message);
    } else {
      System.out.println("Other message");
    }

    int i = 0;
    while (i < 10) {
      System.out.println(i);
      i++;
    }
    return 0;
    // ABC
  }
}
