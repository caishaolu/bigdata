package com.bigdata.analysis.ananlysis;

/**
 * Copyright@www.chiefclouds.com.
 * Author:Mid_tsai
 * Date:2020/5/26
 * Description:
 */
public class UserVisitSessionAnalyzeSpark {
    public static void main(String[] args) {
        // 正常开发流程:
        // 1. 先根据task_id去Mysql的 task表拿到元数据;
        // 2. 根据元数据的规则条件(比如时间范围等); 根据范围读取 user_visit_action 表的数据
        // 3. 基于 sessionid 聚合统计访问步长以及访问时长
        // 4.

    }
}
