package com.anxpp.demo.activiti.simple.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anxpp.demo.activiti.core.repo.UserRepo;
import com.anxpp.demo.activiti.simple.Config.Constant;
import com.anxpp.demo.activiti.simple.core.entity.ApplySimple;
import com.anxpp.demo.activiti.simple.core.repo.ApplySimpleRepo;
import com.anxpp.demo.activiti.simple.core.service.ApplySimpleService;

@Service
public class ApplySimpleImpl implements ApplySimpleService {
	private final static Logger log = LoggerFactory.getLogger(ApplySimpleImpl.class);
	
	private final static String PROCESS_NAME = "simpleProcess";
	private final static String PROCESS_NAME1 = "simpleProcess2";

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ApplySimpleRepo applySimpleRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private HistoryService historyService;

	@Override
	@Transactional	//此处不含spring boot预设的切面中包含事务的字段，需要手动设置
	public String startProcess(ApplySimple applySimple) {
		log.info("开始申请...");
		applySimple.setState(STATE_START);
		//保存业务数据
		applySimpleRepo.save(applySimple);
		//启动流程
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", applySimple.getInsertBy());
		Long dept = userRepo.findDeptById(applySimple.getInsertBy());
		log.info(dept.toString());
		params.put("dept", dept);
		params.put("starter", applySimple.getInsertBy());
		params.put(Constant.STRATEGY_FLOW, Constant.STRATEGY_FLOW_CLASS);
		params.put("appno", applySimple.getId());
		params.put("message","重要");
		//ProcessInstance pi= runtimeService.startProcessInstanceByKey(PROCESS_NAME1, params);
		ProcessInstance pi= runtimeService.createProcessInstanceBuilder()
				.processDefinitionKey(PROCESS_NAME1)
				.addVariable("appno", applySimple.getId())
				.addVariable("dept", applySimple.getInsertBy())
				.addVariable("starter", applySimple.getInsertBy())
				.start();
		
		log.info("流程实例ID：" + pi.getId());//流程实例ID：101  
		log.info("流程实例ID：" + pi.getProcessInstanceId());//流程实例ID：101  
		log.info("流程实例ID:" + pi.getProcessDefinitionId());//myMyHelloWorld:1:4  
		return pi.toString();
	}

	@Override
	public Long countTask(Long uid) {
		return taskService.createTaskQuery()
			.processDefinitionKey(PROCESS_NAME1)
			.taskCandidateUser(uid.toString())
			.count();
	}

	@Override
	public List<Task> getTaskByUid(Long uid) {
		List<Task> list =  taskService.createTaskQuery()
				.processDefinitionKey(PROCESS_NAME1)
				.taskCandidateUser(uid.toString())
				.list();
		for(Task task : list) {
			System.out.println(task.getId()+","+task.getName()+","+task.getOwner()+","+task.getAssignee()+","+task.toString());
		}
		return list;
	}
	@Override
	public List<Task> getTaskByAppno(Long appno) {
		List<Task> list =  taskService.createTaskQuery()
				.processDefinitionKey(PROCESS_NAME1)
				//.taskVariableValueEquals("appno", appno)
				.processVariableValueEquals("appno", appno)
				.list();
		for(Task task : list) {
			System.out.println(task.getId()+","+task.getName()+","+task.getOwner()+","+task.getAssignee()+","+task.toString());
		}
		return list;
	}
	@Override
	@Transactional
	public void completeSimpleCheck(String taskId,Integer checkFlag) {
		log.info("完成任务："+taskId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("checkFlag", checkFlag);
		taskService.complete(taskId, params);
	}

	@Override
	public void updateSimpleState(Long id, Integer state) {
		applySimpleRepo.updateState(id, state);
	}

	@Override
	public Long countProcess() {
		return historyService.createHistoricProcessInstanceQuery()
					.processDefinitionKey(PROCESS_NAME1)
					.count();
	}
	
	@Override
	public List<HistoricTaskInstance> getHisTaskByUid(Long uid) {
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
				.taskCandidateUser(uid.toString()).list();
		for(HistoricTaskInstance task : list) {
			System.out.println(task.getId()+","+task.getName()+","+task.getOwner()+","+task.getAssignee()+","+task.toString());
		}
		
		return list;
	}
	
	@Override
	public List<HistoricTaskInstance> getHisTaskByAppno(Long appno) {
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
				//.taskCandidateUser("0")

				//.taskVariableValueEquals("appno", appno.toString())
				//.includeProcessVariables()
				//.includeTaskLocalVariables()
				//.processVariableValueEqualsIgnoreCase("appno", appno.toString())
				//.taskVariableValueEqualsIgnoreCase("appno", appno.toString())
				.processVariableValueEquals("appno", appno)
				.list();
		for(HistoricTaskInstance task : list) {
			System.out.println(task.getId()+","+task.getName()+","+task.getOwner()+","+task.getAssignee()+","+task.toString());
		}
		
		return list;
	}

}