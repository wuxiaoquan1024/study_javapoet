package com.study.poet.build.typespec;

import java.lang.String;

class InnerClass {
  static {
    String s = "S";
  }

  String name;

  {
    String s = "S";
  }

  class Callback {
    void execute() {
      String cache = name;
    }
  }
}
