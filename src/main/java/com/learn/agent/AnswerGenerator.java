package com.learn.agent;

import java.util.*;

/**
 * 答案生成器
 * 以「问题拆解 + 分步讲解 + 示例代码 + 易错点提醒」形式输出
 */
public class AnswerGenerator {

    private KnowledgeBase knowledgeBase;

    public AnswerGenerator(KnowledgeBase knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    public String generate(String question, IntentRecognizer.RecognitionResult intent) {
        StringBuilder answer = new StringBuilder();

        answer.append("【问题拆解】\n");
        answer.append(breakDownQuestion(question, intent));
        answer.append("\n\n");

        answer.append("【分步讲解】\n");
        answer.append(generateStepByStep(question, intent));
        answer.append("\n\n");

        answer.append("【示例代码】\n");
        answer.append(generateExample(question, intent));
        answer.append("\n\n");

        answer.append("【易错点提醒】\n");
        answer.append(generateTips(question, intent));

        return answer.toString();
    }

    private String breakDownQuestion(String question, IntentRecognizer.RecognitionResult intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("你的问题是：[ ").append(question).append(" ]\n");
        sb.append("问题类型：").append(intent.getType().getDescription());
        if (intent.getSubtype() != null && !intent.getSubtype().equals("unknown")) {
            sb.append(" -> ").append(intent.getSubtype());
        }
        sb.append("\n");
        sb.append("识别依据：").append(intent.getReasoning());
        return sb.toString();
    }

    private String generateStepByStep(String question, IntentRecognizer.RecognitionResult intent) {
        List<KnowledgeBase.KnowledgeEntry> matched = knowledgeBase.match(question);
        StringBuilder sb = new StringBuilder();

        switch (intent.getType()) {
            case SYNTAX_ERROR:
                sb.append("第一步：找到报错位置\n");
                sb.append("  -> 根据错误信息定位到具体行\n\n");
                sb.append("第二步：分析报错原因\n");
                sb.append("  -> 对照知识点理解为什么报错\n\n");
                sb.append("第三步：修改代码\n");
                sb.append("  -> 按照正确写法修正\n\n");
                sb.append("第四步：验证修复\n");
                sb.append("  -> 重新运行确认不再报错");
                break;
            case CONCEPT_UNDERSTANDING:
                if (!matched.isEmpty()) {
                    sb.append("第一步：了解基本概念\n");
                    sb.append("  -> ").append(matched.get(0).getContent()).append("\n\n");
                    if (matched.size() > 1) {
                        sb.append("第二步：相关知识点\n");
                        sb.append("  -> ").append(matched.get(1).getContent()).append("\n\n");
                    }
                }
                sb.append("第三步：理解应用场景\n");
                sb.append("  -> 知道在什么时候用它");
                break;
            case HOMEWORK_HELP:
                sb.append("第一步：理解题目要求\n");
                sb.append("  -> 明确需要实现什么功能\n\n");
                sb.append("第二步：拆解问题\n");
                sb.append("  -> 把大问题分成小步骤\n\n");
                sb.append("第三步：逐个实现\n");
                sb.append("  -> 先实现核心逻辑\n\n");
                sb.append("第四步：测试验证\n");
                sb.append("  -> 用边界数据测试");
                break;
            default:
                sb.append("第一步：理解问题\n");
                sb.append("  -> ").append(question).append("\n\n");
                sb.append("第二步：分析思路\n");
                sb.append("  -> 找到解决方法\n\n");
                sb.append("第三步：代码实现\n");
                sb.append("  -> 写出解决方案");
        }

        return sb.toString();
    }

    private String generateExample(String question, IntentRecognizer.RecognitionResult intent) {
        String q = question.toLowerCase();

        if (q.contains("循环") || q.contains("for") || q.contains("while")) {
            return "// for 循环示例\n" +
                "for (int i = 0; i < 5; i++) {\n" +
                "    System.out.println(\"第 \" + (i + 1) + \" 次循环\");\n" +
                "}\n\n" +
                "// while 循环示例\n" +
                "int count = 0;\n" +
                "while (count < 5) {\n" +
                "    System.out.println(\"count = \" + count);\n" +
                "    count++;\n" +
                "}";
        }

        if (q.contains("数组") || q.contains("array")) {
            return "// 数组声明和初始化\n" +
                "int[] scores = new int[5];\n" +
                "scores[0] = 90;\n" +
                "scores[1] = 85;\n\n" +
                "// 遍历数组\n" +
                "for (int i = 0; i < scores.length; i++) {\n" +
                "    System.out.println(scores[i]);\n" +
                "}\n\n" +
                "// for-each 遍历\n" +
                "for (int score : scores) {\n" +
                "    System.out.println(score);\n" +
                "}";
        }

        if (q.contains("类") || q.contains("对象") || q.contains("oop")) {
            return "// 定义一个学生类\n" +
                "public class Student {\n" +
                "    private String name;  // 姓名\n" +
                "    private int age;      // 年龄\n\n" +
                "    // 构造方法\n" +
                "    public Student(String name, int age) {\n" +
                "        this.name = name;\n" +
                "        this.age = age;\n" +
                "    }\n\n" +
                "    // Getter 方法\n" +
                "    public String getName() { return name; }\n" +
                "    public int getAge() { return age; }\n" +
                "}\n\n" +
                "// 创建对象并使用\n" +
                "Student s = new Student(\"张三\", 20);\n" +
                "System.out.println(s.getName());";
        }

        if (q.contains("异常") || q.contains("try") || q.contains("catch")) {
            return "try {\n" +
                "    int result = 10 / 0;  // 可能出错的代码\n" +
                "} catch (ArithmeticException e) {\n" +
                "    System.out.println(\"出错了：\" + e.getMessage());\n" +
                "} finally {\n" +
                "    System.out.println(\"无论是否出错都会执行\");\n" +
                "}";
        }

        return "// Hello World 示例\n" +
            "public class Hello {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\"Hello, World!\");\n" +
            "    }\n" +
            "}";
    }

    private String generateTips(String question, IntentRecognizer.RecognitionResult intent) {
        List<KnowledgeBase.KnowledgeEntry> matched = knowledgeBase.match(question);
        StringBuilder sb = new StringBuilder();

        sb.append("常见新手错误：\n");
        sb.append("* 忘了分号（;）—— 每条语句必须以分号结束\n");
        sb.append("* 中英文混用 —— 必须用英文半角符号\n");
        sb.append("* 大小写敏感 —— int 和 Int 是不同的\n");
        sb.append("* 数组越界 —— 下标从 0 开始，最大是 length-1\n");

        if (!matched.isEmpty()) {
            sb.append("\n本题易错点：\n");
            for (String tip : matched.get(0).getTips()) {
                sb.append("* ").append(tip).append("\n");
            }
        }

        return sb.toString();
    }
}
