package com.study.poet.build.typespec;

import java.lang.String;

enum SHAPE {
  CIRCLE("CIRCLE", 1),

  RECT("RECT", 2);

  String name;

  int index;

  private SHAPE(String name, int index) {
    this.name = name;
    this.index = index;
  }
}
