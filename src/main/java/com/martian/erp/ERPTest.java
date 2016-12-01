package com.martian.erp;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import com.martian.util.FileUtils;
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
   /** 3.查询当前个人的任务 */
   @Test
   public void findMyprocessTast() {
      String assignee = "liangyanjun";
      List<Task> list = processEngine.getTaskService()// 与正在执行的任务管理相关的service
            .createTaskQuery()// 创建任务查询对象
            .taskAssignee(assignee)// 指定个人任务查询，指定班里人
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
      String taskId = "47502";
      Map<String, Object> variables = new HashMap<String, Object>();
      variables.put("specialType", 2);
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
   
   /**
    * 生成历史流程跟踪图
    *@author:liangyanjun
    *@time:2016年12月1日下午5:29:41
    *@throws IOException
    */
   @Test
   public void createPng() throws IOException {
       ProcessEngineUtil engineUtil = ProcessEngineUtil.getEngineUtil();
       String processInstanceId="37501";
       InputStream imageStream = engineUtil.getProcessPngIs(processInstanceId);
       FileUtils.writerFile(imageStream, "D:\\png\\222.png");
   }
}
