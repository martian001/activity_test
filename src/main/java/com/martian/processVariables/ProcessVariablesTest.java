package com.martian.processVariables;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import com.martian.util.ProcessEngineUtil;

/**
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆ @author： liangyanjun <br>
 * ★☆ @time：2015年11月6日上午9:59:35 <br>
 * ★☆ @version： <br>
 * ★☆ @lastMotifyTime： <br>
 * ★☆ @ClassAnnotation： <br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 */
public class ProcessVariablesTest {
   // 工作流程引擎
   ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();// 默认取classpath下的activiti.cfg.xml
   ProcessEngineUtil engineUtil = ProcessEngineUtil.getEngineUtil();

   /** 1.部署流程定义（从InputStream） */
   @Test
   public void deploymentProcessDefinition() {
      Deployment deployment = engineUtil.deploymentProcessDefinition("请假流程", "processVariables.bpmn", "processVariables.png");
      System.out.println("部署id：" + deployment.getId());//
      System.out.println("部署名称：" + deployment.getName());//
   }

   /** 2.启动流程实例 */
   @Test
   public void startProcessInstance() {
      String processDefinitionKey = "processVariables";
      ProcessInstance pi = engineUtil.startProcessInstance(processDefinitionKey);
      // act_re_procdef表
      System.out.println("流程实例ID:" + pi.getId());
      System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
   }

   /** 3.设置流程变量 */
   @Test
   public void setVariables() {
      /**与任务（正在执行）*/
      TaskService taskService = processEngine.getTaskService();
      String taskId = "604";
      /**一：设置流程变量，使用基本数据类型*/
      // taskService.setVariableLocal(taskId, "请假天数", 3);// 与任务ID绑定，该变量只有在104是可见的，下一个任务是看不见该变量的
      // taskService.setVariable(taskId, "请假日期", new Date());
      // taskService.setVariable(taskId, "请假原因", "回家探亲");
      /**二：设置流程变量，使用javabean类型
       * 
       * 当一个javabean（实现序列化）放置到流程变量中，要求javabean的属性不能再发生变化
       *     如果发生变化，在获取的时候会抛异常
       *     
       * 解决方案：在person对象中，添加
       *  private static final long serialVersionUID = 9520289792972222L;
       * */
      Person p = new Person(10, "张三");
      taskService.setVariable(taskId, "人员信息", p);
      System.out.println("变量设置成功");
   }

   /** 4.获取流程变量 */
   @Test
   public void getVariables() {
      /**与任务（正在执行）*/
      TaskService taskService = processEngine.getTaskService();
      String taskId = "604";
      /**一：获取流程变量，使用基本数据类型*/
      // Integer days = (Integer) taskService.getVariable(taskId, "请假天数");
      // Date date = (Date) taskService.getVariable(taskId, "请假日期");
      // String resean = (String) taskService.getVariable(taskId, "请假原因");
      // System.out.println("请假天数:" + days);
      // System.out.println("请假日期:" + date);
      // System.out.println("请假原因:" + resean);
      /**二：设置流程变量，使用javabean类型*/
      Person p = (Person) taskService.getVariable(taskId, "人员信息");
      System.out.println(p.getId() + "  " + p.getName());
   }

   public void setAndGetVariables() {
      /**与流程实例，执行对象（正在执行）*/
      RuntimeService runtimeService = processEngine.getRuntimeService();
      /**与任务（正在执行）*/
      TaskService taskService = processEngine.getTaskService();
   }

   /**查询流程变量历史表*/
   @Test
   public void findHistoryProcessVariables() {
      List<HistoricVariableInstance> list = processEngine.getHistoryService()//
            .createHistoricVariableInstanceQuery()//
            .variableName("请假天数")//
            .list();
      for (HistoricVariableInstance hvi : list) {
         System.out.println(hvi.getId() + "   " + hvi.getProcessInstanceId() + "   " + hvi.getVariableName());
      }

   }

   /**
    * 完成任务
    */
   @Test
   public void completeMyprocessTast() {
      String taskId = "402";
      engineUtil.completeMyprocessTast(taskId);
   }
}
