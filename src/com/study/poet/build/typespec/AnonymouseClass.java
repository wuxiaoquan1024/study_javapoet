package com.study.poet.build.typespec;

import java.lang.Override;
import java.lang.Runnable;
import java.lang.String;

public class AnonymouseClass {
  Runnable callback = new Runnable() {
    @Override
    public void run() {
    }
  };

  void execute(String name, Runnable c) {
    Runnable cb = new Runnable() {
      @Override
      public void run() {
      }
    };
  }

  public void call() {
    execute("张三", new Runnable() {
      @Override
      public void run() {
      }
    });
  }
}
