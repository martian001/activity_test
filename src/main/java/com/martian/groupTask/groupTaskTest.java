package com.martian.groupTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.junit.Test;

import com.martian.util.ProcessEngineUtil;

/**
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆ @author： liangyanjun <br>
 * ★☆ @time：2015年11月9日下午3:26:14 <br>
 * ★☆ @version： <br>
 * ★☆ @lastMotifyTime： <br>
 * ★☆ @ClassAnnotation： <br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 */
public class groupTaskTest {
   // 工作流程引擎
   ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();// 默认取classpath下的activiti.cfg.xml
   ProcessEngineUtil engineUtil = ProcessEngineUtil.getEngineUtil();

   /** 1.部署流程定义（从InputStream） */
   @Test
   public void deploymentProcessDefinition() {
      Deployment deployment = engineUtil.deploymentProcessDefinition("任务分配-通过组", "groupTask.bpmn", "groupTask.png");
      System.out.println("部署id：" + deployment.getId());//
      System.out.println("部署名称：" + deployment.getName());//
   }

   /** 2.启动流程实例*/
   @Test
   public void startProcessInstance() {
      String processDefinitionKey = "groupTask";
      /**启动流程实例的同时，设置流程变量，使用流程变量用来指定任务的组办理人，对应groupTask.bpmn文件的中Main config candidate user sparated的${userIds}*/
      Map<String, Object> variables = new HashMap<String, Object>();
      variables.put("userIds", "小A,小B,小C,小D");
      ProcessInstance pi = engineUtil.startProcessInstance(processDefinitionKey, variables);
      // act_re_procdef表
      System.out.println("流程实例ID:" + pi.getId());
      System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
   }

   /**
    * 查询正在执行的任务办理人表
    */
   @Test
   public void findRunPersonTask() {
      String taskId = "2105";
      List<IdentityLink> identityLinksForTask = processEngine.getTaskService().getIdentityLinksForTask(taskId);
      for (IdentityLink identityLink : identityLinksForTask) {
         System.out.println("任务编号：" + identityLink.getTaskId() + "，类型：" + identityLink.getType() + "，实例id：" + identityLink.getProcessInstanceId() + "，办理人："
               + identityLink.getUserId());
      }
   }

   /**
    * 查询历史任务办理人表
    */
   @Test
   public void findHistoryPersonTask() {
      // 流程实例id
      String processInstanceId = "2101";
      List<HistoricIdentityLink> historicIdentityLinks = processEngine.getHistoryService().getHistoricIdentityLinksForProcessInstance(processInstanceId);
      for (HistoricIdentityLink identityLink : historicIdentityLinks) {
         // 关于类型：如果是个人任务TYPE的类型表示participant（参与者）
         // 如果是是组任务TYPE的类型表示candidate（候选者：可以进行查询）和participant（参与者：可以进行办理）
         System.out.println("任务编号：" + identityLink.getTaskId() + "，类型：" + identityLink.getType() + "，实例id：" + identityLink.getProcessInstanceId() + "，办理人："
               + identityLink.getUserId());
      }
   }

   /**
    * 拾取任务，将组任务分给个人，指定任务办理人字段
    * 如果组任务没有人拾取，任务完成之后也不会记录办理人，为了方便查看历史记录，应该要拾取任务在办理
    */
   @Test
   public void claim() {
      String taskId = "2105";
      // 分配的个人任务，可以是组任务的成员，也可以是非组任务的成员
      String userId = "小A";
      processEngine.getTaskService().claim(taskId, userId);
   }

   /**
    * 将个人任务回退到组任务，前提，之前一定是组任务
    */
   @Test
   public void fallbackGroup() {
      String taskId = "2105";
      processEngine.getTaskService().claim(taskId, null);
   }

   /**
    * 向组任务添加成员
    */
   @Test
   public void addGroupUser() {
      String taskId = "2105";
      String userId = "大A";
      processEngine.getTaskService()//
            .addCandidateUser(taskId, userId);
   }

   /**
    * 从组任务删除成员
    */
   @Test
   public void deleteGroupUser() {
      String taskId = "2105";
      String userId = "大A";
      processEngine.getTaskService()//
            .deleteCandidateUser(taskId, userId);
   }

   /**
    * 完成任务
    */
   @Test
   public void completeMyprocessTast() {
      String taskId = "2105";
      engineUtil.completeMyprocessTast(taskId);
   }

   /**
    * 查询个人任务
    */
   @Test
   public void findMyprocessTast() {
      String assignee = "小A";
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

   /**
    *根据组的办理人查询当前个人的任务
    */
   @Test
   public void findMyGroupTast() {
      String candidateUser = "小A";
      List<org.activiti.engine.task.Task> list = engineUtil.findMyGroupTast(candidateUser);
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
