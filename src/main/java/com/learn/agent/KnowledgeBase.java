package com.learn.agent;

import java.util.*;

/**
 * 本地知识库
 * 存储 Java 基础知识，支持上下文匹配
 */
public class KnowledgeBase {

    private Map<String, KnowledgeEntry> knowledgeMap;

    public KnowledgeBase() {
        this.knowledgeMap = new HashMap<>();
        initKnowledge();
    }

    private void initKnowledge() {
        knowledgeMap.put("variable", new KnowledgeEntry(
            "变量与数据类型",
            "变量是存储数据的基本单元。Java 是强类型语言，变量必须先声明后使用。",
            new String[]{"变量", "数据类型", "int", "String", "声明"},
            Arrays.asList("注意变量命名规范：首字母小写，驼峰命名法", "局部变量必须初始化后才能使用")
        ));

        knowledgeMap.put("loop", new KnowledgeEntry(
            "循环结构",
            "Java 提供三种循环：for、while、do-while。for 循环适合已知迭代次数的场景，while 循环适合未知迭代次数。",
            new String[]{"循环", "for", "while", "遍历", "迭代"},
            Arrays.asList("注意循环终止条件，避免死循环", "for-each 循环适合遍历集合")
        ));

        knowledgeMap.put("oop", new KnowledgeEntry(
            "面向对象编程",
            "面向对象三大特性：封装、继承、多态。类是对象的抽象，对象是类的实例。",
            new String[]{"面向对象", "类", "对象", "封装", "继承", "多态", "public", "private"},
            Arrays.asList("新手常见错误：混淆类名和对象名", "private 成员只能在类内部访问")
        ));

        knowledgeMap.put("array", new KnowledgeEntry(
            "数组",
            "数组是固定长度的同类型元素序列。创建时必须指定长度，长度不可改变。",
            new String[]{"数组", "[]", "index", "下标", "长度"},
            Arrays.asList("数组下标从 0 开始", "访问越界会抛出 ArrayIndexOutOfBoundsException")
        ));

        knowledgeMap.put("string", new KnowledgeEntry(
            "字符串处理",
            "String 是不可变对象，拼接会产生新字符串。StringBuilder 适合频繁修改。",
            new String[]{"字符串", "String", "拼接", "equals", "length"},
            Arrays.asList("比较字符串内容用 equals()，不能用 ==", "频繁拼接用 StringBuilder 更高效")
        ));

        knowledgeMap.put("exception", new KnowledgeEntry(
            "异常处理",
            "try-catch-finally 结构捕获异常。异常分为受检异常和非受检异常。",
            new String[]{"异常", "try", "catch", "throw", "Exception", "报错"},
            Arrays.asList("finally 块无论是否异常都会执行", "不要用异常代替正常的条件判断")
        ));

        knowledgeMap.put("collection", new KnowledgeEntry(
            "集合框架",
            "List、Set、Map 是三大接口。ArrayList 适合随机访问，LinkedList 适合频繁插入删除。",
            new String[]{"集合", "List", "Map", "Set", "ArrayList", "HashMap", "add", "get"},
            Arrays.asList("Map 用 put 存值，get 取值", "泛型可以在编译时检查类型安全")
        ));

        knowledgeMap.put("method", new KnowledgeEntry(
            "方法与函数",
            "方法必须定义在类内部。语法：访问修饰符 返回类型 方法名(参数列表) { 方法体 }",
            new String[]{"方法", "函数", "调用", "参数", "返回值", "void"},
            Arrays.asList("方法参数是值传递，基本类型不改变原值", "构造方法没有返回类型，名字与类名相同")
        ));
    }

    public static class KnowledgeEntry {
        private String topic;
        private String content;
        private String[] keywords;
        private List<String> tips;

        public KnowledgeEntry(String topic, String content, String[] keywords, List<String> tips) {
            this.topic = topic;
            this.content = content;
            this.keywords = keywords;
            this.tips = tips;
        }

        public String getTopic() { return topic; }
        public String getContent() { return content; }
        public String[] getKeywords() { return keywords; }
        public List<String> getTips() { return tips; }
    }

    public List<KnowledgeEntry> match(String question) {
        List<KnowledgeEntry> matched = new ArrayList<>();
        String q = question.toLowerCase();

        for (KnowledgeEntry entry : knowledgeMap.values()) {
            int score = 0;
            for (String keyword : entry.getKeywords()) {
                if (q.contains(keyword.toLowerCase())) {
                    score++;
                }
            }
            if (score > 0) {
                matched.add(entry);
            }
        }

        matched.sort((a, b) -> {
            int scoreA = countMatches(a, q);
            int scoreB = countMatches(b, q);
            return Integer.compare(scoreB, scoreA);
        });

        return matched;
    }

    private int countMatches(KnowledgeEntry entry, String question) {
        int count = 0;
        for (String keyword : entry.getKeywords()) {
            if (question.contains(keyword.toLowerCase())) {
                count++;
            }
        }
        return count;
    }

    public KnowledgeEntry get(String topic) {
        return knowledgeMap.get(topic);
    }
}
