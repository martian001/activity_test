package activity_test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

public class TestActivitiDB {
    /**
     * 使用代码创建工作流需要23张表
     * @throws Exception
     */
    @Test
    public void createTable() throws Exception {
        
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        //数据库连接配置
        processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/activitiDB?useUnicode=true&characterEncoding=utf8");
        processEngineConfiguration.setJdbcUsername("root");
        processEngineConfiguration.setJdbcPassword("root");
        
       /** public static final String DB_SCHEMA_UPDATE_FALSE = "false";不能自动创建表，需要表存在
           public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";先删除表在创建表
           public static final String DB_SCHEMA_UPDATE_TRUE = "true";如果表不存在，自动创建表
        */
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        //工作流核心对象 ProcessEngine
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        System.out.println(processEngine);
    }
    /**
     * 使用配置文件创建工作流需要23张表
     * @throws Exception
     */
    @Test
    public void createTable_2() throws Exception {
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        //工作流核心对象 ProcessEngine
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        System.out.println(processEngine);
    }
}
