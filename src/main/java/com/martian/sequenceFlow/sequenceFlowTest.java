package com.martian.sequenceFlow;

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
 * ★☆ @time：2015年11月8日上午1:15:21 <br>
 * ★☆ @version： <br>
 * ★☆ @lastMotifyTime： <br>
 * ★☆ @ClassAnnotation： <br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 */
public class sequenceFlowTest {
   // 工作流程引擎
   ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();// 默认取classpath下的activiti.cfg.xml
   ProcessEngineUtil engineUtil = ProcessEngineUtil.getEngineUtil();

   /** 1.部署流程定义（从InputStream） */
   @Test
   public void deploymentProcessDefinition() {
      Deployment deployment = engineUtil.deploymentProcessDefinition("连线", "sequenceFlow.bpmn", "sequenceFlow.png");
      System.out.println("部署id：" + deployment.getId());//
      System.out.println("部署名称：" + deployment.getName());//
   }

   /** 2.启动流程实例 */
   @Test
   public void startProcessInstance() {
      String processDefinitionKey = "sequenceFlow";
      ProcessInstance pi = engineUtil.startProcessInstance(processDefinitionKey);
      // act_re_procdef表
      System.out.println("流程实例ID:" + pi.getId());
      System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
   }

   /** 4.完成我的的任务 */
   @Test
   public void completeMyprocessTast() {
      String taskId = "1803";
      Map<String, Object> variables = new HashMap<String, Object>();
      variables.put("message", "重要");
      engineUtil.completeMyprocessTast(taskId, variables);
   }
}
