package com.learn.agent;

import java.util.*;

/**
 * 意图识别模块
 * 判断用户问题类型：语法报错、概念理解、作业答疑
 */
public class IntentRecognizer {

    private static final Map<String, String[]> ERROR_PATTERNS = new LinkedHashMap<>();
    private static final Map<String, String[]> CONCEPT_PATTERNS = new LinkedHashMap<>();
    private static final Map<String, String[]> HOMEWORK_PATTERNS = new LinkedHashMap<>();

    static {
        ERROR_PATTERNS.put("syntax", new String[]{"语法错误", "cannot find symbol", "syntax error", "报错", "错误"});
        ERROR_PATTERNS.put("nullpointer", new String[]{"空指针", "NullPointerException", "null"});
        ERROR_PATTERNS.put("indexout", new String[]{"数组越界", "IndexOutOfBoundsException", "越界"});
        ERROR_PATTERNS.put("type", new String[]{"类型转换", "ClassCastException", "类型不匹配"});

        CONCEPT_PATTERNS.put("oop", new String[]{"面向对象", "封装", "继承", "多态", "什么是", "的概念"});
        CONCEPT_PATTERNS.put("collection", new String[]{"集合", "List", "Map", "Set", "ArrayList", "HashMap"});
        CONCEPT_PATTERNS.put("thread", new String[]{"线程", "并发", "synchronized", "Thread"});
        CONCEPT_PATTERNS.put("jvm", new String[]{"JVM", "垃圾回收", "内存", "GC", "堆", "栈"});

        HOMEWORK_PATTERNS.put("algorithm", new String[]{"排序", "算法", "递归", "二分", "查找"});
        HOMEWORK_PATTERNS.put("design", new String[]{"设计模式", "单例", "工厂", "观察者"});
        HOMEWORK_PATTERNS.put("file", new String[]{"文件", "读写", "IO", "流"});
    }

    public enum QuestionType {
        SYNTAX_ERROR("语法报错"),
        CONCEPT_UNDERSTANDING("概念理解"),
        HOMEWORK_HELP("作业答疑"),
        GENERAL("通用问题");

        private final String description;
        QuestionType(String description) { this.description = description; }
        public String getDescription() { return description; }
    }

    public static class RecognitionResult {
        private final QuestionType type;
        private final String subtype;
        private final double confidence;
        private final String reasoning;

        public RecognitionResult(QuestionType type, String subtype, double confidence, String reasoning) {
            this.type = type;
            this.subtype = subtype;
            this.confidence = confidence;
            this.reasoning = reasoning;
        }

        public QuestionType getType() { return type; }
        public String getSubtype() { return subtype; }
        public double getConfidence() { return confidence; }
        public String getReasoning() { return reasoning; }
    }

    public static RecognitionResult recognize(String question) {
        if (question == null || question.trim().isEmpty()) {
            return new RecognitionResult(QuestionType.GENERAL, "unknown", 0.0, "空问题");
        }

        String q = question.toLowerCase();

        for (Map.Entry<String, String[]> entry : ERROR_PATTERNS.entrySet()) {
            for (String pattern : entry.getValue()) {
                if (q.contains(pattern.toLowerCase())) {
                    return new RecognitionResult(
                        QuestionType.SYNTAX_ERROR,
                        entry.getKey(),
                        0.95,
                        "检测到报错关键词: " + pattern
                    );
                }
            }
        }

        for (Map.Entry<String, String[]> entry : CONCEPT_PATTERNS.entrySet()) {
            for (String pattern : entry.getValue()) {
                if (q.contains(pattern.toLowerCase())) {
                    return new RecognitionResult(
                        QuestionType.CONCEPT_UNDERSTANDING,
                        entry.getKey(),
                        0.90,
                        "检测到概念关键词: " + pattern
                    );
                }
            }
        }

        for (Map.Entry<String, String[]> entry : HOMEWORK_PATTERNS.entrySet()) {
            for (String pattern : entry.getValue()) {
                if (q.contains(pattern.toLowerCase())) {
                    return new RecognitionResult(
                        QuestionType.HOMEWORK_HELP,
                        entry.getKey(),
                        0.85,
                        "检测到作业关键词: " + pattern
                    );
                }
            }
        }

        return new RecognitionResult(QuestionType.GENERAL, "general", 0.6, "未识别特定模式，作为通用问题处理");
    }
}
