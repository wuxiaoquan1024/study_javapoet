package com.study.poet;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import static com.study.poet.Constants.toFile;

public class T_FileWriter {

    public static void writeTo(TypeSpec typeSpec, String packageName) {
        try {
            JavaFile build = JavaFile.builder(packageName, typeSpec)
                    .build();
            if (toFile) {
                build.writeTo(Constants.dir);
            } else {
                build.writeTo(System.out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
