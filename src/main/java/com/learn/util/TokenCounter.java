package com.learn.util;

/**
 * Token 计数工具
 * 用于估算 LLM API 调用的 Token 消耗
 */
public class TokenCounter {

    /**
     * 估算 Token 数量
     * 中文约 2 字符 ~= 1 token
     * 英文约 4 字符 ~= 1 token
     */
    public static long estimate(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        long chineseChars = text.chars()
            .filter(c -> c > 0x4E00 && c < 0x9FA5)
            .count();
        long otherChars = text.length() - chineseChars;

        return (long) (chineseChars / 2.0 + otherChars / 4.0);
    }

    public static void main(String[] args) {
        String sample = "这是示例文本 Sample Text 123";
        System.out.println("文本：" + sample);
        System.out.println("估算 Token：" + estimate(sample));
    }
}
