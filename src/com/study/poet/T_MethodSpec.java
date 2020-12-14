package com.study.poet;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class T_MethodSpec {

    static final String packageName = Constants.packageName + ".methodspec";

    public static void main(String[] args) {
        createConstructMethod();
        createMethod();
    }


    /**
     * 创建构造函数
     * MethodSpec 区别与一般方法，提供了constructorBuilder 静态方法用于创建构造方法
     * 除此之外，其它与一般方法并无区别
     */
    private static void createConstructMethod() {
        MethodSpec construct = MethodSpec.constructorBuilder()
                .addParameter(ParameterSpec.builder(int.class, "age").build())
                .addParameter(ParameterSpec.builder(String.class, "name").build())
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.name = $N", "name")
                .addStatement("this.age = $N", "age")
                .build();


        TypeSpec student = TypeSpec.classBuilder("Student")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(construct)
                .addField(FieldSpec.builder(int.class, "age").build())
                .addField(FieldSpec.builder(String.class, "name").build())
                .build();
        buildFile(student);
    }

    private static void createMethod() {

        //定义一个final, 使用了Normal注解，类型为String类型的变量
        ParameterSpec message = ParameterSpec.builder(String.class, "message")
                .addModifiers(Modifier.FINAL)
                .addAnnotation(AnnotationSpec.builder(Normal.class).build())
                .build();

        //定义一个泛型类型
        TypeVariableName t = TypeVariableName.get("T");
        ParameterSpec pt = ParameterSpec.builder(t, "t").build();

        MethodSpec speak = MethodSpec.methodBuilder("speak")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(message)
                .addParameter(pt)
                //将最后一个参数声明称可变参数。
                .addParameter(ParameterSpec.builder(String[].class, "other").build())
                .varargs()  // 声明可变参数要求方法最有参数必须是数组

                //添加方法泛型
                .addTypeVariable(t)
                .addException(IllegalArgumentException.class)   //添加异常
                /*
                添加控制流(if, switch, whil, do-while, for) 对应的括号对
                 */
                //if-else if-else 控制流
                .beginControlFlow("if ($N.equals(\"a\"))", "message")
                .addCode("throw new $T();\n", IllegalArgumentException.class)
                .nextControlFlow("else if ($N.equals(\"b\"))", "message")
                .addCode("$T.out.println($N);\n", System.class, "message")
                .nextControlFlow("else")
                .addStatement("$T.out.println(\"$L\")", System.class, "Other message")
                .endControlFlow()

                //while 循环
                .addCode("\n$T i = 0;\n", int.class)
                .beginControlFlow("while (i < 10)", "i")
                //addCode 和 addStatement 都可以添加一行代码，区别在于addStatement 带有分号和换行
                .addCode("$T.out.println($N);\n", System.class, "i")
                .addStatement("i++")
                .endControlFlow()

                //给方法添加注解
                .addAnnotation(Normal.class)
                .addStatement("return 0")
                //添加单行注释
                .addComment("$L", "ABC")
                //声明方法的返回类型
                .returns(int.class)
                .build();

        TypeSpec teacher = TypeSpec.classBuilder("Teacher")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(speak)
                .build();
        buildFile(teacher);
    }

    private static void buildFile(TypeSpec typeSpec) {
        T_FileWriter.writeTo(typeSpec, packageName);
    }


}
