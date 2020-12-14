package com.study.poet.build.typespec;

import java.lang.Runnable;

public abstract class AbsClass {
  protected abstract <T> void doing(T t, Runnable callback);

  protected <R extends Runnable> void execute(R executor) {
    executor.run();
  }
}
