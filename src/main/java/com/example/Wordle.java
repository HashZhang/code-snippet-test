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

