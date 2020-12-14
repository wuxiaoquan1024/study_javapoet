package com.study.poet;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import static com.study.poet.Constants.packageName;

/**
 *
 */
public class T_Format {

    public static void main(String[] args) {
        TypeSpec.Builder tsBuilder = TypeSpec.classBuilder("JavaPoetFormat")
                .addModifiers(Modifier.PUBLIC);
        formatL(tsBuilder);
        formatS(tsBuilder);
        formatT(tsBuilder);
        formatN(tsBuilder);
        formatOther(tsBuilder);

        TypeSpec typeSpec = tsBuilder.build();
        T_FileWriter.writeTo(typeSpec, packageName);
    }

    /**
     * 占位符$L.  在JavaPoet 中表示不需要转义的字面字面值，可以是字符串、基本数据类型、类型声明、变量、注解
     */
    public static void formatL(TypeSpec.Builder builder) {
        FieldSpec str = FieldSpec.builder(String.class, "str")
                .initializer("\"abc\"")
                .build();
        MethodSpec forL = MethodSpec.methodBuilder("formatL")
                .addModifiers(Modifier.PUBLIC)
                //使用字符串常量赋值
                .addStatement("$T formatStr = \"$L\"", String.class, "a")
                //使用已有的变量赋值
                .addStatement("$T fromField = $L", String.class, "str")
                .addStatement("$T fs = $S", String.class, "abc")
                .build();

        builder.addField(str)
            .addMethod(forL);
    }

    /**
     * 占位符$S. JavaPoet 同String.format 中一样，字符串模板，将指定的字符串替换到$S的
     */
    public static void formatS(TypeSpec.Builder builder) {

        ClassName className = ClassName.get(packageName, "JavaPoetFormat");
        FieldSpec tag = FieldSpec.builder(String.class, "TAG")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer(className.simpleName())
                .build();
        builder.addField(tag);

        MethodSpec fors = MethodSpec.methodBuilder("formatS")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$T fs = $S", String.class, "abc")
                //$S 自能是字符串,不能是其它基本数据类型等其它类型
//                .addStatement("$T i = $S", int.class, 1)
                .addStatement("$T i = $S", String.class, "fs")
                .build();

        builder.addMethod(fors);
    }

    /**
     * $T在JavaPoet代指的是TypeName，该模板主要将Class抽象出来，用传入的TypeName指向的Class来代替;也可以是TypeMirror,或者是Element。
     * 另外，在JavaPoet中，$T不仅支持内置类型，还包括自动生成import语句。
     * @param builder
     */
    public static void formatT(TypeSpec.Builder builder) {
        TypeVariableName t = TypeVariableName.get("T");
        MethodSpec formatT = MethodSpec.methodBuilder("formatT")
                .addTypeVariable(t)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(t, "params").build())
                //使用$T 占位符应用已经声明好的T类型。
                .addStatement("$T t = $L", t, "params")
                //使用TypeName
                .addStatement("$T s = \"$L\"", String.class, "params")
                .build();

        builder.addMethod(formatT);

    }

    /**
     * $N占位符，在JavaPoet 中指代一个名称，其可指已经声明的一个类型(类型指代JavaPoet中的类型)。
     * @param builder
     */
    public static void formatN(TypeSpec.Builder builder) {
        TypeVariableName t = TypeVariableName.get("T");
        MethodSpec formatT = MethodSpec.methodBuilder("formatN")
                .addTypeVariable(t)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(t, "params").build())
                .build();

        FieldSpec sn = FieldSpec.builder(String.class, "sn")
                .initializer("")
                .build();

        int[] array = {1, 2, 3, 4, 5, 6};
        ArrayTypeName of = ArrayTypeName.of(int.class);

        MethodSpec invoke = MethodSpec.methodBuilder("invoke")
                .addModifiers(Modifier.PUBLIC)
                //使用$N占位符应用已经定义好的MethodSpec formatT方法
                .addStatement("$N()", formatT)
                //使用$N占位符应用已经定义的FieldSpec sn 常量
                .addStatement("$T t = $N", String.class, sn)
                .addParameter(of, "array")
                //使用参数的int[] 类型直接作为返回值
                .addStatement("return $N", "array")
                .returns(of)
//                .beginControlFlow("for (int i = 0; i < ")
                .build();

        builder.addField(sn);
        builder.addMethod(formatT);
        builder.addMethod(invoke);
    }

    public static void formatOther(TypeSpec.Builder builder) {
        MethodSpec formatOther = MethodSpec.methodBuilder("formatOther")
                .addModifiers(Modifier.PUBLIC)
                .addCode("$T t = $S;", String.class, "println format")
                //$W 占位符表示空格或者换行
                .addCode("$W$W$W$W$W$W$W")
                .addCode("$T i = $L;", int.class, 0)
                //占位符$> 表示向右缩进一个制表符
                .addStatement("$>")
                .addCode("$T.out.println(\"abc\");", System.class)
                //占位符$> 表示向左缩进一个制表符
                .addStatement("$<")
                .addCode("$T.out.println(\"efg\");", System.class)
                //占位符位置
                .addStatement("$2T.out.println($1S)", "xyz", System.class)
                .build();

        builder.addMethod(formatOther);
    }

}
