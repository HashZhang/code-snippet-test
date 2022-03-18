# 测试 Java 18 新的 Javadoc

> 相关 JEP：
> - [JEP 413: Code Snippets in Java API Documentation](https://openjdk.java.net/jeps/413)

干净整洁更新及时并且有规范的示例的 API 文档会让你获益良多，并且如果 API 文档的代码如果能编译，能随着你的源码变化而变化，就更完美了，Java 18 就给了 Javadoc 这些特性。

我们编写一个 Maven 项目试一下(代码库地址：https://github.com/HashZhang/code-snippet-test )

首先，我们想在普通 maven 项目的 `src/main/java` 和 `src/test/java` 以外新添加一个目录 `src/demo/java` 用于存放示例代码。因为示例代码我们并不想打包到最后发布的 jar 包，示例代码也需要编译，所以我们把这个示例代码目录标记为测试代码目录（为啥不放入 `src/test/java`，因为我们还是想区分开示例代码与单元测试代码的）：
```
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>build-helper-maven-plugin</artifactId>
    <version>3.3.0</version>
    <executions>
        <execution>
            <id>add-demos</id>
            <phase>generate-test-sources</phase>
            <goals>
                <goal>add-test-source</goal>
            </goals>
            <configuration>
                <sources>
                    <source>src/demo/java</source>
                </sources>
            </configuration>
        </execution>
    </executions>
</plugin>
```
我们需要 maven 插件来执行生成 javadoc，同时我们要指定代码段扫描的目录（即你的源码中，执行代码段文件所处于的目录，这个目录我们这里和源码目录 `src/main/java` 隔离开了，是 `src/demo/java`）:
```
<plugin>
    <artifactId>maven-javadoc-plugin</artifactId>
    <version>3.3.2</version>
    <configuration>
        <additionalOptions>--snippet-path ${project.basedir}/src/demo/java</additionalOptions>
    </configuration>
</plugin>
```

首先，我们创建我们的 API 类，即：

```
package com.example;

import org.eclipse.collections.api.bag.primitive.MutableCharBag;
import org.eclipse.collections.impl.factory.Strings;
import org.eclipse.collections.impl.factory.primitive.CharBags;
import org.eclipse.collections.impl.string.immutable.CharAdapter;

/**
 * 猜词游戏类，通过下面的代码初始化
 * {@snippet file="com/example/WordleDemo.java" region="initilize"}
 */
public class Wordle {
    private final String hidden;

    /**
     * 通过一个要猜的词初始化 Wordle
     *
     * @param hidden 要猜的隐藏的词
     */
    public Wordle(String hidden) {
        this.hidden = hidden;
    }

    /**
     * 猜词，返回结果
     * {@snippet file="com/example/WordleDemo.java" region="test"}
     * @param guess 当前猜的词
     * @return 猜词的结果
     */
    public String guess(String guess) {
        CharAdapter guessChars = Strings.asChars(guess);
        CharAdapter hiddenChars = Strings.asChars(this.hidden);
        MutableCharBag remaining = hiddenChars.injectIntoWithIndex(CharBags.mutable.empty(), ((mutableCharBag, each, i) -> {
            return guessChars.get(i) != each ? mutableCharBag.with(each) : mutableCharBag;
        }));
        return guessChars.collectWithIndex((each, i) -> {
            return hiddenChars.get(i) == each ?
                    Character.toUpperCase(each) :
                    remaining.remove(each) ? each : ".";
        }).makeString();
    }
}

```
可以看到，我们在注释中指定了代码段读取的文件以及读取的区域，我们现在来编写示例代码：

```
package com.example;

import org.junit.Assert;
import org.junit.Test;

public class WordleDemo {
    @Test
    public void testWordle() {
        // @start region="initilize"
        // 创建一个 Wordle 并且有一个谜底词 SIEGE
        Wordle wordle = new Wordle("SIEGE");
        // @end

        // @start region="test"
        // 通过 guess 方法传入猜的词，返回的是猜词结果提示
        String result = wordle.guess("SAEGE");
        // @end

        Assert.assertEquals(result, "S, ., E, G, E");
    }
}

```
从示例代码中，我们可以看到对于引用区域的指定(位于 `@start` 与 `@end` 之间).

目前项目结构是：
![image](https://zhxhash-blog.oss-cn-beijing.aliyuncs.com/2022-3-16%20%E5%85%B3%E4%BA%8E%20Java%2018%20%E4%BD%A0%E6%83%B3%E7%9F%A5%E9%81%93%E7%9A%84%E4%B8%80%E5%88%87/code-snippet-test.png)

执行 `mvn javadoc:javadoc`，在 `target/site` 目录下就能看到生成的 Javadoc，Javadoc 中可以包含你项目中的代码段：
![image](https://zhxhash-blog.oss-cn-beijing.aliyuncs.com/2022-3-16%20%E5%85%B3%E4%BA%8E%20Java%2018%20%E4%BD%A0%E6%83%B3%E7%9F%A5%E9%81%93%E7%9A%84%E4%B8%80%E5%88%87/code-snippet-test-demo.png)

**你还可以高亮你的一些注释，或者使用 CSS 编辑样式**，这里就不再赘述了