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
