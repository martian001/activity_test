package com.martian.erp;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import com.martian.util.ProcessEngineUtil;

/**
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆ @author： liangyanjun <br>
 * ★☆ @time：2016年4月22日上午9:26:08 <br>
 * ★☆ @version： <br>
 * ★☆ @lastMotifyTime： <br>
 * ★☆ @ClassAnnotation： <br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 */
public class ERPTest {
   // 工作流程引擎
   ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();// 默认取classpath下的activiti.cfg.xml
   ProcessEngineUtil engineUtil = ProcessEngineUtil.getEngineUtil();

   /** 1.部署流程定义（从InputStream） */
   @Test
   public void deploymentProcessDefinition() {
      Deployment deployment = engineUtil.deploymentProcessDefinition("中途划转", "IntermediateTransferProcess.bpmn", "IntermediateTransferProcess.png");
      System.out.println("部署id：" + deployment.getId());//
      System.out.println("部署名称：" + deployment.getName());//
   }

   /** 2.启动流程实例 */
   @Test
   public void startProcessInstance() {
      String processDefinitionKey = "intermediateTransferProcess";
      Map variables = new HashMap();
      variables.put("candidateUsers", "liangyanjun");
      ProcessInstance pi = engineUtil.startProcessInstance(processDefinitionKey, variables);
      // act_re_procdef表
      System.out.println("流程实例ID:" + pi.getId());
      System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
   }

   /** 4.完成我的的任务 */
   @Test
   public void completeMyprocessTast() {
      String taskId = "2303";
      Map<String, Object> variables = new HashMap<String, Object>();
      variables.put("specialType", 1);
      engineUtil.completeMyprocessTast(taskId, variables);
   }

   /**
    *  删除流程实例
    *@author:liangyanjun
    *@time:2016年4月22日上午9:39:57
    *@param processInstanceId
    */
   @Test
   public void deleteProcessInstance() {
      String processInstanceId = "301";
      engineUtil.deleteProcessInstance(processInstanceId);
   }
}
