package com.martian.paralleGeteWay;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import com.martian.util.ProcessEngineUtil;

/**
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆ @author： liangyanjun <br>
 * ★☆ @time：2015年11月8日上午2:03:53 <br>
 * ★☆ @version： <br>
 * ★☆ @lastMotifyTime： <br>
 * ★☆ @ClassAnnotation： <br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 */
public class paralleGeteWayTest {
   // 工作流程引擎
   ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();// 默认取classpath下的activiti.cfg.xml
   ProcessEngineUtil engineUtil = ProcessEngineUtil.getEngineUtil();

   /** 1.部署流程定义（从InputStream） */
   @Test
   public void deploymentProcessDefinition() {
      Deployment deployment = engineUtil.deploymentProcessDefinition("并行网关", "paralleGeteWay.bpmn", "paralleGeteWay.png");
      System.out.println("部署id：" + deployment.getId());//
      System.out.println("部署名称：" + deployment.getName());//
   }

   /** 2.启动流程实例 */
   @Test
   public void startProcessInstance() {
      String processDefinitionKey = "paralleGeteWay";
      ProcessInstance pi = engineUtil.startProcessInstance(processDefinitionKey);
      // act_re_procdef表
      System.out.println("流程实例ID:" + pi.getId());
      System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
   }
   /** 3.查询当前个人的任务 */
   @Test
   public void findMyprocessTast() {
      String assignee = "李四";
      List<Task> list = processEngine.getTaskService()// 与正在执行的任务管理相关的service
            .createTaskQuery()// 创建任务查询对象
            //.taskAssignee(assignee)// 指定个人任务查询，指定班里人
            .list();
      for (Task task : list) {
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
   /** 4.完成我的的任务 */
   @Test
   public void completeMyprocessTast() {
      String taskId = "5502";
      engineUtil.completeMyprocessTast(taskId);
   }
}
