package com.learn.agent;

import java.util.*;

/**
 * 反思模块
 * 对生成的答案进行二次校验，避免超纲内容或复杂术语
 */
public class ReflectionModule {

    private static final Map<String, String> TERM_REPLACEMENTS = new LinkedHashMap<>();
    private static final String[] FORBIDDEN_TERMS = {
        "协变返回类型", "逆变", "泛型擦除机制", "双亲委派",
        "CAS", "ABA问题", "偏向锁轻量级锁重量级锁"
    };

    static {
        TERM_REPLACEMENTS.put("多态", "同一个方法调用不同对象会有不同结果");
        TERM_REPLACEMENTS.put("封装", "把数据和方法包在一起，对外只暴露需要的部分");
        TERM_REPLACEMENTS.put("继承", "子类可以直接使用父类的内容");
        TERM_REPLACEMENTS.put("抽象类", "不能直接创建对象的类，只能被继承");
        TERM_REPLACEMENTS.put("接口", "规定了一些方法，让实现它的类来实现");
        TERM_REPLACEMENTS.put("多线程", "同时做多件事");
        TERM_REPLACEMENTS.put("同步", "排队一个个来，防止乱套");
        TERM_REPLACEMENTS.put("递归", "自己调用自己");
        TERM_REPLACEMENTS.put("算法复杂度", "程序运行快不快");
    }

    public static class ReflectionResult {
        private final boolean approved;
        private final String revisedAnswer;
        private final List<String> warnings;
        private final List<String> improvements;

        public ReflectionResult(boolean approved, String revisedAnswer,
                               List<String> warnings, List<String> improvements) {
            this.approved = approved;
            this.revisedAnswer = revisedAnswer;
            this.warnings = warnings;
            this.improvements = improvements;
        }

        public boolean isApproved() { return approved; }
        public String getRevisedAnswer() { return revisedAnswer; }
        public List<String> getWarnings() { return warnings; }
        public List<String> getImprovements() { return improvements; }
    }

    public static ReflectionResult reflect(String originalAnswer, String question) {
        List<String> warnings = new ArrayList<>();
        List<String> improvements = new ArrayList<>();
        String revised = originalAnswer;

        if (originalAnswer.length() > 1500) {
            warnings.add("答案偏长，可能超出新手接受范围，建议分段输出");
            if (originalAnswer.length() > 2000) {
                revised = originalAnswer.substring(0, 2000) + "\n\n（内容较长，建议分步学习）";
            }
        }

        for (String term : FORBIDDEN_TERMS) {
            if (originalAnswer.contains(term)) {
                warnings.add("发现较难术语: " + term);
            }
        }

        for (Map.Entry<String, String> entry : TERM_REPLACEMENTS.entrySet()) {
            if (originalAnswer.contains(entry.getKey())) {
                improvements.add("已将「" + entry.getKey() + "」替换为更易懂的解释");
            }
        }

        if (!originalAnswer.contains("```") && !originalAnswer.contains("代码")) {
            if (!originalAnswer.contains("没有代码") && !originalAnswer.contains("不需要代码")) {
                improvements.add("建议补充示例代码，帮助理解");
            }
        }

        if (!originalAnswer.contains("注意") && !originalAnswer.contains("易错")
            && !originalAnswer.contains("常见错误") && !originalAnswer.contains("坑")) {
            improvements.add("建议增加易错点提醒");
        }

        boolean approved = warnings.isEmpty() && improvements.size() <= 1;

        return new ReflectionResult(approved, revised, warnings, improvements);
    }

    public static String simplifyTerm(String term) {
        return TERM_REPLACEMENTS.getOrDefault(term, term);
    }
}
