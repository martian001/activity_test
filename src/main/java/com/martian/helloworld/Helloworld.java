package com.martian.helloworld;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class Helloworld {
    //工作流程引擎
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();//默认取classpath下的activiti.cfg.xml

    /** 1.部署流程定义 */
    @Test
    public void deploymentProcessDefinition() {
        Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的service
                .createDeployment()//创建一个部署对象
                .name("helloworld入门程序2")//添加部署名称
                .addClasspathResource("diagrams/helloworld.bpmn")//从classpath的资源中加载，一次只能加载一个文件
                .addClasspathResource("diagrams/helloworld.png")//从classpath的资源中加载，一次只能加载一个文件
                //                .addZipInputStream(new ZipInputStream(getClass().getClassLoader().getResourceAsStream("helloworld.zip")))
                .deploy();//完成部署
        //act_re_deployment表
        System.out.println("部署id：" + deployment.getId());//1
        System.out.println("部署名称：" + deployment.getName());//helloworld入门程序
    }

    /** 2.启动流程实例 */
    @Test
    public void startProcessInstance() {
        String processDefinitionKey = "helloworldId";
        ProcessInstance pi = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的service
                .startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中的id属性值
        //act_re_procdef表
        System.out.println("流程实例ID:" + pi.getId());//101
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId());//helloworldId:1:4

    }

    /** 3.查询当前个人的任务 */
    @Test
    public void findMyprocessTast() {
        String assignee = "李四";
        List<Task> list = processEngine.getTaskService()//与正在执行的任务管理相关的service
                .createTaskQuery()//创建任务查询对象
                .taskAssignee(assignee)//指定个人任务查询，指定班里人
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
        String taskId = "104";
        processEngine.getTaskService().complete(taskId);
        System.out.println("完成任务，任务ID:" + taskId);
    }

    /** 5.查询流程定义 */
    @Test
    public void findProcessDefinition() {
        List<ProcessDefinition> list = processEngine.getRepositoryService()//与流程定义和部署对象相关的service
                .createProcessDefinitionQuery()//创建一个流程定义的查询
                /** 指定查询条件 */
                //        .deploymentId(deploymentId)//使用部署对象id查询
                //        .processDefinitionId(processDefinitionId)//使用流程定义id查询
                //        .processDefinitionKey(processDefinitionKey)//使用流程定义key查询
                //        .processDefinitionNameLike(processDefinitionNameLike)//使用流程定义的名称模糊查询
                /** 排序 */
                .orderByProcessDefinitionVersion().asc()//按照流程定义的版本升序排序
                //        .orderByProcessDefinitionName().desc()//按流程定义的名字降序牌组
                /** 返回结果集 */
                //        .singleResult()//返回唯一结果集
                //        .count()//返回结果集数量
                //        .listPage(firstResult, maxResults)//分页查询
                .list();//返回一个结果集，封装流程定义
        for (ProcessDefinition pd : list) {
            System.out.println("流程定义id：" + pd.getId());//流程定义的key+版本+随机生成数
            System.out.println("流程定义名称：" + pd.getName());//对应helloworld.bpmn文件中的name属性值
            System.out.println("流程定义的key：" + pd.getKey());//对应helloworld.bpmn文件中的id属性值
            System.out.println("流程定义的版本：" + pd.getVersion());//当流程定义的key值相同的情况下，版本升级，默认是1
            System.out.println("资源名称bpmn文件：" + pd.getResourceName());
            System.out.println("资源名称png文件：" + pd.getDiagramResourceName());
            System.out.println("部署对象id：" + pd.getDeploymentId());
            System.out.println("##############################################");

        }

    }
}
