JavaPoet定义了一系列类来尽可能优雅的描述java源文件的结构。观察JavaPoet的代码主要的类
可以分为以下几种：

Spec：用来描述Java中基本的元素，包括类型，注解，字段，方法和参数等。
    AnnotationSpec：用于生成注解对象的类
    FieldSpec：用于配置生成成员变量的类
    MethodSpec：用于生成方法对象的类
    ParameterSpec：用于生成参数对象的类
    TypeSpec：用于生成类、接口、枚举对象的类
TypeName：用来描述类型的引用，包括Void，原始类型（int，long等）和Java类等。
    ArrayTypeName：通过指定元素类型生成包含该元素的数组，或获取指定的mirror/type等效的数组类型
    ClassName：通过包名和类名生成的对象，在JavaPoet中相当于为其指定Class
    ParameterizedTypeName：通过MainClass和IncludeClass生成包含泛型的Class
    TypeVariableName：泛型变量类型
    WildcardTypeName：通配符类型
CodeBlock：用来描述代码块的内容，包括普通的赋值，if判断，循环判断等。
JavaFile：完整的Java文件，JavaPoet的主要的入口。
CodeWriter：读取JavaFile并转换成可阅读可编译的Java源文件。