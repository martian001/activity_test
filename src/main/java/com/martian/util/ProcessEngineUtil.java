package com.martian.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;

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

   /** 2.启动流程实例 并设置流程变量*/
   public ProcessInstance startProcessInstance(String processDefinitionKey, Map variables) {
      ProcessInstance pi = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的service
            .startProcessInstanceByKey(processDefinitionKey, variables);// 使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中的id属性值
      // act_re_procdef表
      // System.out.println("流程实例ID:" + pi.getId());
      // System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
      return pi;
   }

   /** 3.根据办理人查询当前个人的任务 */
   public List<Task> findMyprocessTast(String assignee) {
      List<Task> list = processEngine.getTaskService()// 与正在执行的任务管理相关的service
            .createTaskQuery()// 创建任务查询对象
            .taskAssignee(assignee)// 指定个人任务查询，指定班里人
            .list();
      return list;
   }

   /** 3.根据组的办理人查询当前个人的任务 */
   public List<Task> findMyGroupTast(String candidateUser) {
      List<Task> list = processEngine.getTaskService()// 与正在执行的任务管理相关的service
            .createTaskQuery()// 创建任务查询对象
            .taskCandidateUser(candidateUser).list();
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
   /**
    *  删除流程实例
    *@author:liangyanjun
    *@time:2016年4月22日上午9:39:57
    *@param processInstanceId
    */
  public void deleteProcessInstance(String processInstanceId){
     processEngine.getRuntimeService().deleteProcessInstance(processInstanceId, "");
  }
  
  
  /** 获取历史流程跟踪图 的文件流
   * 
   * @author:liangyanjun
   * @time:2016年12月1日下午5:22:12
   * @param repositoryService
   * @param historyService
   * @param processInstanceId
   * @return */
    public InputStream getProcessPngIs(String processInstanceId) {
        HistoryService historyService = processEngine.getHistoryService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        //获取历史流程实例
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId)
                .singleResult();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);
        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processInstance
                .getProcessDefinitionId());
        List<HistoricActivityInstance> highLightedActivitList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).list();
        //高亮环节id集合
        List<String> highLightedActivitis = new ArrayList<String>();
        //高亮线路id集合
        List<String> highLightedFlows = getHighLightedFlows(definitionEntity, highLightedActivitList);
        for (HistoricActivityInstance tempActivity : highLightedActivitList) {
            String activityId = tempActivity.getActivityId();
            highLightedActivitis.add(activityId);
        }
        //中文显示的是口口口，设置字体就好了
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis, highLightedFlows, "宋体", "宋体",
                null, 1.0);
        return imageStream;
    }

  /** 
   * 获取需要高亮的线
   *@author:liangyanjun
   *@time:2016年12月1日下午5:26:05
   *@param processDefinitionEntity
   *@param historicActivityInstances
   *@return
   */
  private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity,
          List<HistoricActivityInstance> historicActivityInstances) {
      List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
      for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
          ActivityImpl activityImpl = processDefinitionEntity.findActivity(historicActivityInstances.get(i).getActivityId());// 得到节点定义的详细信息
          List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();// 用以保存后需开始时间相同的节点
          ActivityImpl sameActivityImpl1 = processDefinitionEntity.findActivity(historicActivityInstances.get(i + 1).getActivityId());
          // 将后面第一个节点放在时间相同节点的集合里
          sameStartTimeNodes.add(sameActivityImpl1);
          for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
              HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
              HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点
              if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {
                  // 如果第一个节点和第二个节点开始时间相同保存
                  ActivityImpl sameActivityImpl2 = processDefinitionEntity.findActivity(activityImpl2.getActivityId());
                  sameStartTimeNodes.add(sameActivityImpl2);
              } else {
                  // 有不相同跳出循环
                  break;
              }
          }
          List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线
          for (PvmTransition pvmTransition : pvmTransitions) {
              // 对所有的线进行遍历
              ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
              // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
              if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                  highFlows.add(pvmTransition.getId());
              }
          }
      }
      return highFlows;
  }
}
