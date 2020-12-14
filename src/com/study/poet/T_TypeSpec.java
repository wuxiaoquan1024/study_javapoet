package com.study.poet;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class T_TypeSpec {

    static final String packageName = Constants.packageName + ".typespec";
    public static void main(String[] args) {
        File[] files = Constants.dir.listFiles();
        for (File file : files) {
            file.delete();
        }
        createClass();
        createInterface();
        createAbsClass();
        createExtendClass();
        createAnnotationClass();
        createEnum();
        createAonymousClass();
        createInnerClass();
    }

    /**
     * 创建Class
     */
    private static void createClass() {
        TypeSpec typeSpce_demo = TypeSpec.classBuilder("TypeSpce_Demo")
                .addModifiers(Modifier.PUBLIC)
                .build();
        buildFile(typeSpce_demo);
    }

    /**
     * 使用TypeSpec.interfaceBuilder创建接口
     */
    private static void createInterface() {

        ParameterSpec id = ParameterSpec.builder(int.class, "id", Modifier.FINAL).build();
        /**
         * 接口中的方法使用public修饰的时候，方法同时必须声明称abstract, default, static三者之一
         */
        MethodSpec action = MethodSpec.methodBuilder("action")
                .addParameter(id)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .build();

        /**
         * 接口中的成员变量必须声明称final static 类型
         */
        FieldSpec tag = FieldSpec.builder(String.class, "TAG")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("\"$L\"", "IInterface") //初始化变量
                .build();

        TypeSpec iInterface = TypeSpec.interfaceBuilder("IInterface")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(action)
                //添加常量
                .addField(tag)
                .build();
        buildFile(iInterface);
    }

    /**
     * 声明带有泛型抽象方法、实现方法的抽象类
     */
    private static void createAbsClass() {
        //定义泛型类型
        TypeVariableName typeName = TypeVariableName.get("T");
        //使用泛型声明参数
        ParameterSpec t = ParameterSpec.builder(typeName, "t").build();

        ParameterSpec callback = ParameterSpec.builder(Runnable.class, "callback").build();
        //定义一个泛型的抽象方法
        MethodSpec doing = MethodSpec.methodBuilder("doing")
                .addModifiers(Modifier.ABSTRACT)
                .addModifiers(Modifier.PROTECTED)
                .addTypeVariable(typeName)  //添加泛型
                .addParameter(t)  //添加方法参数， 参数类型泛型
                .addParameter(callback)
                .build();
        TypeSpec absClass;
        TypeSpec.Builder absClassBuilder = TypeSpec.classBuilder("AbsClass")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addMethod(doing);


        //定义executor.run() 语句
        CodeBlock statement = CodeBlock.builder()
                .add("$N.run();", "executor")
                .build();
        //声明extend 泛型
        TypeVariableName r = TypeVariableName.get("R", Runnable.class);
        ParameterSpec executor = ParameterSpec.builder(r, "executor").build();
        // 定义一个使用了extend 泛型的execute方法
        MethodSpec execute = MethodSpec.methodBuilder("execute")
                .addModifiers(Modifier.PROTECTED)
                .addTypeVariable(r)  //添加泛型
                .addParameter(executor)
                .addCode(statement)
                .build();
        absClassBuilder.addMethod(execute);
        absClass = absClassBuilder.build();

        buildFile(absClass);
    }

    /**
     * 创建继承Abs,实现Runnable 类
     * 继承类、实现接口，必须安装Java规范实现
     */
    private static void createExtendClass() {
        //定义参数
        ParameterSpec msg = ParameterSpec.builder(String.class, "msg").build();
        CodeBlock statement = CodeBlock.builder()
                .add("$T.out.print($N);", System.class, "msg")
                .build();
        CodeBlock callPrint = CodeBlock.builder()
                .add("print(\"$N\");", "Running")
                .build();

        //实现抽象类中的print 抽象方法
        MethodSpec print = MethodSpec.methodBuilder("print")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(msg)
                .addCode(statement)
                .build();

        MethodSpec run = MethodSpec.methodBuilder("run")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC) // 实现接口中定义的方法必须使用public 修饰
                .addJavadoc("Override Runnable run function") //添加Doc 文档
                .addCode(callPrint)
                .build();

        //定义一个泛型名称为R的类型
        TypeVariableName t = TypeVariableName.get("R");
        /*
            创建泛型参数类型
            参数：
                rawType: 实现的类型
                typeName: 类型中定义的泛型类型
         */
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(IInter.class), t);
        TypeSpec printClass = TypeSpec.classBuilder("PrintClass")
                .addMethod(print)
                .superclass(Abs.class)  // 继承类
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(t)     //添加泛型
                .addSuperinterface(parameterizedTypeName) //使用ParameterizedTypeName 将泛型的类型传递给接口中的泛型参数
                .addSuperinterface(Runnable.class)
                .addMethod(run)
                .addJavadoc("add Doc")
                .build();
        buildFile(printClass);
    }

    /**
     * 创建注解类
     *  TypeSpec.annotationBuilder()
     */
    private static void createAnnotationClass() {
        /*
            构建Annotation 元素。如果Annotation 有参数，可以使用addMember方法添加
            addMember详解:
                name: 参数的名称
                format: 参数值的格式化样式
                args:  参数格式化中占位符对应的值
         */
        //给Retention 注解的value 赋类型为RetentionPolicy，值为CLASS 的值
        AnnotationSpec rClazz = AnnotationSpec.builder(Retention.class)
                .addMember("value", "$T.$L", RetentionPolicy.class, RetentionPolicy.CLASS)
                .build();
        AnnotationSpec targetAnnotion = AnnotationSpec.builder(Target.class)
                .addMember("value", "$T.$L", ElementType.class, ElementType.METHOD)
                .addMember("value", "$T.$L", ElementType.class, ElementType.FIELD)
                .build();

        //注解的方法必须是[public, abstract]类型的
        MethodSpec id = MethodSpec.methodBuilder("id")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(int.class)     // 定义方法的返回类型
                .defaultValue("$L", 0)  // 定义方法的默认值
                .build();

        //如果Annotion 有参数，不能使用ClassName, Class 作为参数，必须使用AnnatationSpec作为参数
        TypeSpec annotation = TypeSpec.annotationBuilder("TypeA")
                .addAnnotation(rClazz)  //给类添加注解
                .addAnnotation(targetAnnotion)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(id)
                .build();
        buildFile(annotation);
    }

    /**
     * 创建枚举
     */
    private static void createEnum() {
        /*
        创建的Enum
         */
        TypeSpec oritation = TypeSpec.enumBuilder("ORITATION")
                .addEnumConstant("EAST")
                .addEnumConstant("SOUTH")
                .addEnumConstant("WEAST")
                .addEnumConstant("NORTH")
                .build();
        buildFile(oritation);

        /*
        创建带有name, index 两个属性的自定义枚举
         */
        //声明构造方法
        MethodSpec construct = MethodSpec.constructorBuilder()
                .addParameter(ParameterSpec.builder(String.class, "name").build())
                .addParameter(ParameterSpec.builder(int.class, "index").build())
                .addModifiers(Modifier.PRIVATE)
                .addStatement("this.$L = $N", "name", "name")
                .addStatement("this.$L = $N", "index", "index")
                .build();

        FieldSpec name = FieldSpec.builder(String.class, "name").build();
        FieldSpec index = FieldSpec.builder(int.class, "index").build();
        TypeSpec shape = TypeSpec.enumBuilder("SHAPE")
                .addMethod(construct)
                .addField(name)
                .addField(index)
                /*
                    添加带有自定义属性类型的枚举。
                   通过匿名内部类声明枚举类型
                 */
                .addEnumConstant("CIRCLE", buildEnumType("CIRCLE", 1))
                .addEnumConstant("RECT", buildEnumType("RECT", 2))
                .build();
        buildFile(shape);
    }

    private static TypeSpec buildEnumType(String name, int index) {
        MethodSpec display = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .build();
        //实现一个带有两个参数，没有方法的匿名类
        return TypeSpec.anonymousClassBuilder("\"$L\", $L", name, index)
                .build();

    }

    private static void createAonymousClass() {

        //全局变量使用匿名类初始化
        MethodSpec run = MethodSpec.methodBuilder("run")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .build();

        TypeSpec anonyClass = TypeSpec.anonymousClassBuilder("")
                .addMethod(run)
                .addSuperinterface(Runnable.class)
                .build();

        FieldSpec callbackField = FieldSpec.builder(Runnable.class, "callback")
                .initializer("$L", anonyClass)
                .build();

        ParameterSpec name = ParameterSpec.builder(String.class, "name")
                .build();
        ParameterSpec c = ParameterSpec.builder(Runnable.class, "c").build();

        CodeBlock code = CodeBlock.builder()
                .add("$T cb = $L;", Runnable.class, anonyClass).build();

        // 局部变量使用匿名类初始化
        MethodSpec execute = MethodSpec.methodBuilder("execute")
                .addParameter(name)
                .addParameter(c)
                .addCode(code)
                .build();

        //使用匿名类初始化局部变量
        CodeBlock callCode = CodeBlock.builder()
                .add("$L(\"$L\", $L);", "execute", "张三", anonyClass)
                .build();
        MethodSpec call = MethodSpec.methodBuilder("call")
                .addCode(callCode)
                .addModifiers(Modifier.PUBLIC)
                .build();


        TypeSpec clazz = TypeSpec.classBuilder("AnonymouseClass")
                .addField(callbackField)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(execute)
                .addMethod(call)
                .build();
        buildFile(clazz);
    }

    /**
     * 创建内部类
     */
    private static void createInnerClass() {
        MethodSpec execute = MethodSpec.methodBuilder("execute")
                .addStatement("$T cache = $L", String.class, "name")
                .build();

        TypeSpec inner = TypeSpec.classBuilder("Callback")
                .addMethod(execute)
                .build();

        TypeSpec outer = TypeSpec.classBuilder("InnerClass")
                .addField(FieldSpec.builder(String.class, "name").build())
                .addType(inner) //添加内部类
                .addInitializerBlock(CodeBlock.builder().add("$T s = \"$L\";\n", String.class, "S").build())
                //添加静态块
                .addStaticBlock(CodeBlock.builder().add("$T s = \"$L\";\n", String.class, "S").build())
                .build();
        buildFile(outer);
    }

    private static void buildFile(TypeSpec typeSpec) {
        T_FileWriter.writeTo(typeSpec, packageName);
    }
}
