package com.martian.personTask;

import java.util.HashMap;
import java.util.List;
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
 * ★☆ @time：2015年11月9日上午10:37:57 <br>
 * ★☆ @version： <br>
 * ★☆ @lastMotifyTime： <br>
 * ★☆ @ClassAnnotation： <br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 */
public class TaskTest {
   // 工作流程引擎
   ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();// 默认取classpath下的activiti.cfg.xml
   ProcessEngineUtil engineUtil = ProcessEngineUtil.getEngineUtil();

   /** 1.部署流程定义（从InputStream） */
   @Test
   public void deploymentProcessDefinition() {
      Deployment deployment = engineUtil.deploymentProcessDefinition("任务分配", "task.bpmn", "task.png");
      System.out.println("部署id：" + deployment.getId());//
      System.out.println("部署名称：" + deployment.getName());//
   }

   /** 2.启动流程实例+设置任务办理人 */
   @Test
   public void startProcessInstance() {
      String processDefinitionKey = "task";
      Map<String, Object> variables = new HashMap<String, Object>();
      variables.put("userId", "张三");
      /**启动流程实例的同时，设置流程变量，使用流程变量用来指定任务的办理人，对应task.bpmn文件的中${userId}*/
      ProcessInstance pi = engineUtil.startProcessInstance(processDefinitionKey, variables);
      // act_re_procdef表
      System.out.println("流程实例ID:" + pi.getId());
      System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
   }

   /**
    * 重新分配任务办理人
    */
   @Test
   public void setAssignee() {
      String taskId = "";
      String userId = "";
      processEngine.getTaskService().setAssignee(taskId, userId);
   }

   /**
    * 完成任务
    */
   @Test
   public void completeMyprocessTast() {
      String taskId = "1405";
      engineUtil.completeMyprocessTast(taskId);
   }

   /**
    * 查询个人任务
    */
   @Test
   public void findMyprocessTast() {
      String assignee = "张三";
      List<org.activiti.engine.task.Task> list = engineUtil.findMyprocessTast(assignee);
      for (org.activiti.engine.task.Task task : list) {
         System.out.println("任务id:" + task.getId());
         System.out.println("任务名称:" + task.getName());
         System.out.println("任务创建时间:" + task.getCreateTime());
         System.out.println("任务办理人:" + task.getAssignee());
         System.out.println("任务实例id:" + task.getProcessInstanceId());
         System.out.println("执行对象id:" + task.getExecutionId());
         System.out.println("流程定义id:" + task.getProcessDefinitionId());
         System.out.println("#########################################################");
      }

   }
}
