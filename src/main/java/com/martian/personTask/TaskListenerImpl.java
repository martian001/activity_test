package com.martian.personTask;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆ @author： liangyanjun <br>
 * ★☆ @time：2015年11月9日下午2:22:55 <br>
 * ★☆ @version： <br>
 * ★☆ @lastMotifyTime： <br>
 * ★☆ @ClassAnnotation： <br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 * ★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★<br>
 */
public class TaskListenerImpl implements TaskListener {

   /**
    * 
    */
   private static final long serialVersionUID = 4089515424299990369L;

   /**
    * 用来指定任务的办理人
    */
   @Override
   public void notify(DelegateTask delegateTask) {
      /**
       * 指定个人任务的办理人，也可以指定组任务的办理人
       * 通过类去查询数据库，将下一个任务的办理人查询获得，然后通过setAssignee（）的方法指定
       * 
       * 在TaskListener.bpmn文件中的的listener属性中添加该类即可
       * */
      String assignee = "李四";
      delegateTask.setAssignee(assignee);
   }

}
