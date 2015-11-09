package com.martian.receiveTast;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import com.martian.util.ProcessEngineUtil;

/**
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆ @author： liangyanjun <br>
 * ★☆ @time：2015年11月9日上午9:50:05 <br>
 * ★☆ @version： <br>
 * ★☆ @lastMotifyTime： <br>
 * ★☆ @ClassAnnotation： <br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 */
public class receiveTastTest {
   // 工作流程引擎
   ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();// 默认取classpath下的activiti.cfg.xml
   ProcessEngineUtil engineUtil = ProcessEngineUtil.getEngineUtil();

   /** 1.部署流程定义（从InputStream） */
   @Test
   public void deploymentProcessDefinition() {
      Deployment deployment = engineUtil.deploymentProcessDefinition("接收任务活动", "receiveTast.bpmn", "receiveTast.png");
      System.out.println("部署id：" + deployment.getId());//
      System.out.println("部署名称：" + deployment.getName());//
   }

   /** 2.启动流程实例 */
   @Test
   public void startProcessInstance() {
      String processDefinitionKey = "receiveTast";
      ProcessInstance pi = engineUtil.startProcessInstance(processDefinitionKey);
      // act_re_procdef表
      System.out.println("流程实例ID:" + pi.getId());
      System.out.println("流程定义ID:" + pi.getProcessDefinitionId());

      /**查询执行对象*/
      Execution execution1 = processEngine.getRuntimeService()//
            .createExecutionQuery()// 创建执行对象查询
            .processInstanceId(pi.getId())// 使用流程实例ID查询
            .activityId("receivetask1")// 当前活动ID，对应receiveTast.bpmn文件的活动中节点id的属性值
            .singleResult();

      /**使用流程变量设置当日销售额，用来传递业务参数*/
      processEngine.getRuntimeService()//
            .setVariableLocal(execution1.getId(), "汇总当日销售额", 2000);

      /**向后执行一步，如果流程处于等待状态，使得流程继续执行*/
      processEngine.getRuntimeService()//
            .signal(execution1.getId());

      /**查询执行对象*/
      Execution execution2 = processEngine.getRuntimeService()//
            .createExecutionQuery()// 创建执行对象查询
            .processInstanceId(pi.getId())// 使用流程实例ID查询
            .activityId("receivetask2")// 当前活动ID，对应receiveTast.bpmn文件的活动中节点id的属性值
            .singleResult();

      /**使用流程变量获取当日销售额*/
      Integer value = (Integer) processEngine.getRuntimeService()//
            .getVariableLocal(execution2.getId(), "汇总当日销售额");
      System.out.println("给老板发送消息，金额：" + value);

      /**向后执行一步，如果流程处于等待状态，使得流程继续执行*/
      processEngine.getRuntimeService()//
            .signal(execution2.getId());
   }
}
