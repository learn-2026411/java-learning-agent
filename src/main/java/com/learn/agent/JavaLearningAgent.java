package com.learn.agent;

import java.util.*;

/**
 * Java 学习答疑 Agent 核心类
 * 单 Agent 长链推理流程：
 * 意图识别 -> 知识库匹配 -> 反思校验 -> 结构化输出
 */
public class JavaLearningAgent {

    private KnowledgeBase knowledgeBase;
    private AnswerGenerator answerGenerator;
    private long totalTokenEstimate;

    public JavaLearningAgent() {
        this.knowledgeBase = new KnowledgeBase();
        this.answerGenerator = new AnswerGenerator(knowledgeBase);
        this.totalTokenEstimate = 0;
    }

    public Response process(String question) {
        long startTime = System.currentTimeMillis();
        StringBuilder log = new StringBuilder();
        log.append("================================================\n");
        log.append("          Java 学习答疑 Agent\n");
        log.append("================================================\n\n");

        log.append("[第一步] 意图识别\n");
        IntentRecognizer.RecognitionResult intent = IntentRecognizer.recognize(question);
        log.append("  类型：").append(intent.getType().getDescription()).append("\n");
        log.append("  子类：").append(intent.getSubtype()).append("\n");
        log.append("  置信度：").append(String.format("%.0f%%", intent.getConfidence() * 100)).append("\n");
        log.append("  分析：").append(intent.getReasoning()).append("\n\n");

        log.append("[第二步] 知识库匹配\n");
        List<KnowledgeBase.KnowledgeEntry> matched = knowledgeBase.match(question);
        log.append("  匹配到 ").append(matched.size()).append(" 个相关知识点：\n");
        for (int i = 0; i < Math.min(3, matched.size()); i++) {
            log.append("  ").append(i + 1).append(". ").append(matched.get(i).getTopic()).append("\n");
        }
        log.append("\n");

        log.append("[第三步] 生成答案\n");
        String answer = answerGenerator.generate(question, intent);
        log.append("  生成完成，答案长度：").append(answer.length()).append(" 字符\n\n");

        log.append("[第四步] 反思校验\n");
        ReflectionModule.ReflectionResult reflection = ReflectionModule.reflect(answer, question);
        if (reflection.isApproved()) {
            log.append("  [OK] 答案通过审查\n");
        } else {
            log.append("  [WARN] 建议优化：\n");
            for (String w : reflection.getWarnings()) {
                log.append("    - ").append(w).append("\n");
            }
        }
        for (String imp : reflection.getImprovements()) {
            log.append("    + ").append(imp).append("\n");
        }
        log.append("\n");

        long tokens = estimateTokens(question + answer);
        totalTokenEstimate += tokens;
        log.append("[运行统计]\n");
        log.append("  本次 Token 消耗：").append(tokens).append("\n");
        log.append("  历史累计：").append(totalTokenEstimate).append("\n");
        log.append("  处理耗时：").append(System.currentTimeMillis() - startTime).append(" ms\n");

        String finalAnswer = reflection.getRevisedAnswer();
        return new Response(finalAnswer, log.toString(), intent, tokens);
    }

    private long estimateTokens(String text) {
        long chineseChars = text.chars().filter(c -> c > 0x4E00 && c < 0x9FA5).count();
        long otherChars = text.length() - chineseChars;
        return (long) (chineseChars / 2.0 + otherChars / 4.0);
    }

    public static class Response {
        private final String answer;
        private final String log;
        private final IntentRecognizer.RecognitionResult intent;
        private final long tokenCost;

        public Response(String answer, String log, IntentRecognizer.RecognitionResult intent, long tokenCost) {
            this.answer = answer;
            this.log = log;
            this.intent = intent;
            this.tokenCost = tokenCost;
        }

        public String getAnswer() { return answer; }
        public String getLog() { return log; }
        public IntentRecognizer.RecognitionResult getIntent() { return intent; }
        public long getTokenCost() { return tokenCost; }
    }
}
