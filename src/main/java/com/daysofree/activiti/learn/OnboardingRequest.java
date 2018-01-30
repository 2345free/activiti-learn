package com.daysofree.activiti.learn;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

public class OnboardingRequest {
    public static void main(String[] args) {
        // 代码方式实例化引擎
//        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
//                .setJdbcUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000")
//                .setJdbcUsername("sa")
//                .setJdbcPassword("")
//                .setJdbcDriver("org.h2.Driver")
//                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        // 读取配置文件方式实例化引擎
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml");

        ProcessEngine processEngine = cfg.buildProcessEngine();
        String pName = processEngine.getName();
        String ver = ProcessEngine.VERSION;
        System.out.println("ProcessEngine [" + pName + "] Version: [" + ver + "]");
    }
}
