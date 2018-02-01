package junit;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ActivitiSpringTest extends BaseTest {

	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RuntimeService historyService;
	@Autowired
	private TaskService managementService;

	@Test
	public void deploy() {

		// 部署流程(加载流程描述文件)
		Deployment deployment = repositoryService.createDeployment().name("MyProcess").addClasspathResource("diagrams/MyProcess.bpmn").addClasspathResource("diagrams/MyProcess.png").deploy();
		System.out.println("部署ID:" + deployment.getId());
		System.out.println("部署名字:" + deployment.getName());
		System.out.println("部署类别:" + deployment.getCategory());
		System.out.println("部署时间:" + deployment.getDeploymentTime());
		System.out.println("部署房客ID:" + deployment.getTenantId());

		// 启动流程
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("myProcess");
		System.out.println("流程实例ID:" + pi.getId());

		// 依次完成流程: 张三-->李四-->王五
		completePersonalAllTask("张三");
		completePersonalAllTask("李四");
		completePersonalAllTask("王五");
	}

	/**查询当前的个人任务(实际就是查询act_ru_task表)*/
	private List<Task> findMyPersonalTask(String assignee) {
		System.out.printf("查询%s的任务列表\n", assignee);
		List<Task> taskList = taskService.createTaskQuery()//创建任务查询对象
				.taskAssignee(assignee)//指定个人任务查询，指定办理人
				.list();//获取该办理人下的事务列表

		if (taskList != null && taskList.size() > 0) {
			for (Task task : taskList) {
				System.out.println("任务ID：" + task.getId());
				System.out.println("任务名称：" + task.getName());
				System.out.println("任务的创建时间：" + task.getCreateTime());
				System.out.println("任务办理人：" + task.getAssignee());
				System.out.println("流程实例ID：" + task.getProcessInstanceId());
				System.out.println("执行对象ID：" + task.getExecutionId());
				System.out.println("流程定义ID：" + task.getProcessDefinitionId());
				System.out.println("#############################################");
			}
		}
		return taskList;
	}

	/**完成我的任务*/
	private void completeMyPersonalTask(String taskId) {
		taskService.complete(taskId);//完成taskId对应的任务
		System.out.println("完成ID为" + taskId + "的任务");

	}

	private void completePersonalAllTask(String assignee) {
		List<Task> taskList = findMyPersonalTask(assignee);
		if (taskList != null && taskList.size() > 0) {
			for (Task task : taskList) {
				completeMyPersonalTask(task.getId());
			}
		}
	}

}
