package junit;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.zip.ZipInputStream;

@Slf4j
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
	public void deployZipProccess() throws Exception {
		Deployment deploy = repositoryService.createDeployment()
				// 部署zip流程文件
				.addZipInputStream(new ZipInputStream(new ClassPathResource("diagrams/MyProcess.bpmn.zip").getInputStream()))//
				.deploy();
		log.info("部署ID:{}", deploy.getId());
		log.info("部署Name:{}", deploy.getName());
	}

	/**
	 * 查看流程定义
	 * id:(key):(version):(随机值)
	 * name:对应流程文件process节点的name属性
	 * key:对应流程文件process节点的id属性
	 * version:发布时自动生成的。如果是第一次发布的流程，version默认从1开始；
	 * 如果当前流程引擎中已存在相同的流程，则找到当前key对应的最高版本号，在最高版本号上加1
	 */
	@Test
	public void queryProcessDefinition() throws Exception {
		//获取仓库服务对象，使用版本的升级排列，查询列表  
		List<ProcessDefinition> pdList = repositoryService.createProcessDefinitionQuery()
				//添加查询条件  
				//.processDefinitionId(processDefinitionId)  
				//.processDefinitionKey(processDefinitionKey)  
				//.processDefinitionName(processDefinitionName)  
				//排序(可以按照id/key/name/version/Cagetory排序)  
				.orderByProcessDefinitionVersion().asc()
				//.count()  
				//.listPage(firstResult, maxResults)  
				//.singleResult()  
				.list();//总的结果集数量  
		//便利集合，查看内容  
		for (ProcessDefinition pd : pdList) {
			System.out.println("id:" + pd.getId());
			System.out.println("name:" + pd.getName());
			System.out.println("key:" + pd.getKey());
			System.out.println("version:" + pd.getVersion());
			System.out.println("resourceName:" + pd.getDiagramResourceName());
			System.out.println("###########################################");
		}
	}

	@Test
	public void deploy() {

		// 部署流程(加载流程描述文件)
		Deployment deployment = repositoryService.createDeployment() // 创建一个部署对象
				.name("MyProcess")// 
				.addClasspathResource("diagrams/MyProcess.bpmn")// 从classpath的资源加载，一次只能加载一个文件  
				.addClasspathResource("diagrams/MyProcess.png")//
				.deploy();
		log.info("部署ID:" + deployment.getId());
		log.info("部署名字:" + deployment.getName());
		log.info("部署类别:" + deployment.getCategory());
		log.info("部署时间:" + deployment.getDeploymentTime());
		log.info("部署房客ID:" + deployment.getTenantId());

		// 启动流程
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("myProcess");
		log.info("流程实例ID:" + pi.getId());

		// 依次完成流程: 张三-->李四-->王五
		completePersonalAllTask("张三");
		completePersonalAllTask("李四");
		completePersonalAllTask("王五");
	}

	/**查询当前的个人任务(实际就是查询act_ru_task表)*/
	private List<Task> findMyPersonalTask(String assignee) {
		log.info("查询{}的任务列表", assignee);
		log.info("#############################################");
		List<Task> taskList = taskService.createTaskQuery()//创建任务查询对象
				.taskAssignee(assignee)//指定个人任务查询，指定办理人
				.list();//获取该办理人下的事务列表

		if (taskList != null && taskList.size() > 0) {
			for (Task task : taskList) {
				log.info("任务ID：" + task.getId());
				log.info("任务名称：" + task.getName());
				log.info("任务的创建时间：" + task.getCreateTime());
				log.info("任务办理人：" + task.getAssignee());
				log.info("流程实例ID：" + task.getProcessInstanceId());
				log.info("执行对象ID：" + task.getExecutionId());
				log.info("流程定义ID：" + task.getProcessDefinitionId());
				log.info("#############################################");
			}
		}
		return taskList;
	}

	/**完成我的任务*/
	private void completeMyPersonalTask(String taskId) {
		taskService.complete(taskId);//完成taskId对应的任务
		log.info("完成ID为" + taskId + "的任务");
		log.info("#############################################");
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
