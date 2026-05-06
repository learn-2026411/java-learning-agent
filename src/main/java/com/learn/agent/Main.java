package com.learn.agent;

import java.util.*;

/**
 * Java 学习答疑 Agent - 主入口
 * 
 * 功能：解决编程初学者在学习 Java 过程中遇到的两大痛点：
 * 1. 遇到代码报错、概念理解偏差时，无法得到针对性的、贴合新手视角的分步讲解
 * 2. 零散的知识点难以串联，缺乏系统化的学习路径规划
 * 
 * 核心流程：意图识别 -> 知识库匹配 -> 反思校验 -> 结构化输出
 */
public class Main {

    private static final String[] WELCOME = {
        "",
        "======================================================",
        "           Java 学习答疑 Agent v1.0                   ",
        "                                                      ",
        "  解决问题类型：                                      ",
        "    [1] 语法报错 - 代码报错诊断与修复                 ",
        "    [2] 概念理解 - Java 核心概念讲解                  ",
        "    [3] 作业答疑 - 编程题思路与实现                   ",
        "                                                      ",
        "  输入你的 Java 问题，我来帮你解答！                  ",
        "  输入 quit 退出                                      ",
        "======================================================",
        ""
    };

    public static void main(String[] args) {
        JavaLearningAgent agent = new JavaLearningAgent();
        Scanner scanner = new Scanner(System.in);

        for (String line : WELCOME) {
            System.out.println(line);
        }

        int questionCount = 0;
        long totalTokens = 0;

        while (true) {
            try {
                System.out.print("\n[你]> ");
                String question = scanner.nextLine().trim();

                if (question.isEmpty()) {
                    System.out.println("请输入问题，或输入 quit 退出");
                    continue;
                }

                if (question.equalsIgnoreCase("quit") || question.equalsIgnoreCase("exit")) {
                    System.out.println("\n感谢使用！再见！");
                    System.out.println("本次对话统计：");
                    System.out.println("  问题数：" + questionCount);
                    System.out.println("  Token 消耗：" + totalTokens);
                    if (questionCount > 0) {
                        System.out.println("  日均 Token：" + (totalTokens / questionCount));
                    }
                    break;
                }

                JavaLearningAgent.Response response = agent.process(question);

                System.out.println("\n" + response.getLog());

                System.out.println("================================================");
                System.out.println("              答 案");
                System.out.println("================================================\n");
                System.out.println(response.getAnswer());

                questionCount++;
                totalTokens += response.getTokenCost();

                System.out.println("\n-------------------------------------------");
                System.out.println("本次消耗：" + response.getTokenCost() + " tokens");

            } catch (Exception e) {
                System.out.println("处理问题时出现错误：" + e.getMessage());
                e.printStackTrace();
            }
        }

        scanner.close();
    }
}
