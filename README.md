# 测试 Java 18 新的 Javadoc

> 相关 JEP：
> - [JEP 413: Code Snippets in Java API Documentation](https://openjdk.java.net/jeps/413)

干净整洁更新及时并且有规范的示例的 API 文档会让你获益良多，并且如果 API 文档的代码如果能编译，能随着你的源码变化而变化，就更完美了，Java 18 就给了 Javadoc 这些特性。

运行这个项目的 javadoc 编译：`mvn javadoc:javadoc`

在 `target/site` 目录下就能看到生成的 Javadoc，Javadoc 中可以包含你项目中的代码段

