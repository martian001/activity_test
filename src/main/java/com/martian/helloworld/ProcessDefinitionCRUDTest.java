package com.martian.helloworld;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * 
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆ @author： liangyanjun <br>
 * ★☆ @time：2015年10月25日下午7:59:41 <br>
 * ★☆ @version： <br>
 * ★☆ @lastMotifyTime： <br>
 * ★☆ @ClassAnnotation： <br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * 
 * Deployment 部署对象
 * 1.一次部署的多个文件的信息。对于不需要的流程定义可以删除和修改。
 * 2.对应的表
 * act_re_deployment 部署对象表
 * act_re_procdef 流程定义表
 * act_ge_bytearray 资源文件表
 * act_ge_property  主键生成策略表
 * 
 * ProcessDefinition 流程定义
 * 1.解析.bpmn后得到的流程定义规则的信息，工作流系统就是按照流程定义的规则执行
 */
public class ProcessDefinitionCRUDTest {
   // 工作流程引擎
   ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();// 默认取classpath下的activiti.cfg.xml

   /** 1.查询流程定义 */
   @Test
   public void findProcessDefinition() {
      List<ProcessDefinition> list = processEngine.getRepositoryService()// 与流程定义和部署对象相关的service
            .createProcessDefinitionQuery()// 创建一个流程定义的查询
            /** 指定查询条件 */
            // .deploymentId(deploymentId)//使用部署对象id查询
            // .processDefinitionId(processDefinitionId)//使用流程定义id查询
            // .processDefinitionKey(processDefinitionKey)//使用流程定义key查询
            // .processDefinitionNameLike(processDefinitionNameLike)//使用流程定义的名称模糊查询
            /** 排序 */
            .orderByProcessDefinitionVersion().asc()// 按照流程定义的版本升序排序
            // .orderByProcessDefinitionName().desc()//按流程定义的名字降序牌组
            /** 返回结果集 */
            // .singleResult()//返回唯一结果集
            // .count()//返回结果集数量
            // .listPage(firstResult, maxResults)//分页查询
            .list();// 返回一个结果集，封装流程定义
      for (ProcessDefinition pd : list) {
         System.out.println("流程定义id：" + pd.getId());// 流程定义的key+版本+随机生成数
         System.out.println("流程定义名称：" + pd.getName());// 对应helloworld.bpmn文件中的name属性值
         System.out.println("流程定义的key：" + pd.getKey());// 对应helloworld.bpmn文件中的id属性值
         System.out.println("流程定义的版本：" + pd.getVersion());// 当流程定义的key值相同的情况下，版本升级，默认是1
         System.out.println("资源名称bpmn文件：" + pd.getResourceName());
         System.out.println("资源名称png文件：" + pd.getDiagramResourceName());
         System.out.println("部署对象id：" + pd.getDeploymentId());
         System.out.println("##############################################");

      }

   }

   /** 2.删除流程定义 */
   @Test
   public void deleteProcessDefinition() {
      // 使用部署id，完成删除（act_re_procdef DEPLOYMENT_ID_）
      String deploymentId = "401";
      /**
       * 不带级联删除
       *     只能删除没有启动的流程，如果流程启动，就会抛错
       */
      // processEngine.getRepositoryService()//
      // .deleteDeployment(deploymentId);
      /**
       * 级联删除
       *     不管流程是否启动，都能删除
       */
      processEngine.getRepositoryService()//
            .deleteDeployment(deploymentId, true);
      System.out.println("删除成功");

   }

   /** 3.查看流程图 
    * @throws IOException */
   @Test
   public void viewPic() throws IOException {
      String deploymentId = "1";
      // 获取图片资源名称
      List<String> deploymentResourceNames = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
      // 定义图片资源的名称
      String resourceName = "";
      if (deploymentResourceNames != null && deploymentResourceNames.size() > 0) {
         for (String name : deploymentResourceNames) {
            if (name.indexOf(".png") >= 0) {
               resourceName = name;
            }
         }
      }
      // 获取图片输入流
      InputStream in = processEngine.getRepositoryService().getResourceAsStream(deploymentId, resourceName);
      File file = new File("d:/" + resourceName);
      FileUtils.copyInputStreamToFile(in, file);

   }

   /**
    * 附加功能：查询最新版本的流程定义
    */
   @Test
   public void getLastProsstion() {
      List<ProcessDefinition> list = processEngine.getRepositoryService()//
            .createProcessDefinitionQuery()//
            .orderByProcessDefinitionVersion().asc()// 使用流程定义的版本升序排序
            .list();
      /**
       * Map<String,ProcessDefinition>
       * map集合的key：流程定义的key
       * map集合的value：流程定义对象
       * map集合的特点：当map集合的key值相同的情况下，后一次的值会替换前一次的值
       */
      Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();
      for (ProcessDefinition pd : list) {
         map.put(pd.getKey(), pd);
      }
      List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(map.values());
      for (ProcessDefinition pd : pdList) {
         System.out.println("流程定义id：" + pd.getId());// 流程定义的key+版本+随机生成数
         System.out.println("流程定义名称：" + pd.getName());// 对应helloworld.bpmn文件中的name属性值
         System.out.println("流程定义的key：" + pd.getKey());// 对应helloworld.bpmn文件中的id属性值
         System.out.println("流程定义的版本：" + pd.getVersion());// 当流程定义的key值相同的情况下，版本升级，默认是1
         System.out.println("资源名称bpmn文件：" + pd.getResourceName());
         System.out.println("资源名称png文件：" + pd.getDiagramResourceName());
         System.out.println("部署对象id：" + pd.getDeploymentId());
         System.out.println("##############################################");

      }
   }
}