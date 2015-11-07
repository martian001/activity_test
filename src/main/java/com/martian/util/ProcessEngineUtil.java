package com.martian.util;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆ @author： liangyanjun <br>
 * ★☆ @time：2015年11月6日上午10:11:03 <br>
 * ★☆ @version： <br>
 * ★☆ @lastMotifyTime： <br>
 * ★☆ @ClassAnnotation： <br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 */
public class ProcessEngineUtil {
   private static ProcessEngineUtil engineUtil = new ProcessEngineUtil();

   // 工作流程引擎
   private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();// 默认取classpath下的activiti.cfg.xml

   public static ProcessEngineUtil getEngineUtil() {
      return engineUtil;
   }

   public ProcessEngine getProcessEngine() {
      return processEngine;
   }

   /** 1.部署流程定义（从InputStream） */
   public Deployment deploymentProcessDefinition(String processDefinitionName, String bpmnName, String pngName) {
      InputStream inputStreamBpmn = this.getClass().getResourceAsStream("/diagrams/" + bpmnName);
      InputStream inputStreamPng = this.getClass().getResourceAsStream("/diagrams/" + pngName);
      Deployment deployment = processEngine.getRepositoryService()// 与流程定义和部署对象相关的service
            .createDeployment()// 创建一个部署对象
            .name(processDefinitionName)// 添加部署名称
            .addInputStream(bpmnName, inputStreamBpmn)//
            .addInputStream(pngName, inputStreamPng)//
            // .addZipInputStream(new ZipInputStream(getClass().getClassLoader().getResourceAsStream("helloworld.zip")))
            .deploy();// 完成部署
      // act_re_deployment表
      // System.out.println("部署id：" + deployment.getId());//
      // System.out.println("部署名称：" + deployment.getName());//
      return deployment;
   }

   /** 2.启动流程实例 */
   public ProcessInstance startProcessInstance(String processDefinitionKey) {
      ProcessInstance pi = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的service
            .startProcessInstanceByKey(processDefinitionKey);// 使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中的id属性值
      // act_re_procdef表
      // System.out.println("流程实例ID:" + pi.getId());
      // System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
      return pi;
   }

   /** 3.查询当前个人的任务 */
   public List<Task> findMyprocessTast(String assignee) {
      List<Task> list = processEngine.getTaskService()// 与正在执行的任务管理相关的service
            .createTaskQuery()// 创建任务查询对象
            .taskAssignee(assignee)// 指定个人任务查询，指定班里人
            .list();
      return list;
   }

   /** 4.完成我的的任务 */
   public void completeMyprocessTast(String taskId) {
      processEngine.getTaskService().complete(taskId);
      System.out.println("完成任务，任务ID:" + taskId);
   }

   /** 5.完成我的的任务 */
   public void completeMyprocessTast(String taskId, Map variables) {
      processEngine.getTaskService().complete(taskId, variables);
      System.out.println("完成任务，任务ID:" + taskId);
   }

}
