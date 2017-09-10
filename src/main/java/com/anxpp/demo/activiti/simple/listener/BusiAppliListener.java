package com.anxpp.demo.activiti.simple.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 领导审核监听器
 * @author anxpp.com
 * 2016年12月24日 下午12:10:01
 */
public class BusiAppliListener implements TaskListener{
	private static final long serialVersionUID = 4285398130708457006L;
	private final static Logger log = LoggerFactory.getLogger(BusiAppliListener.class);
	@Override
	public void notify(DelegateTask task) {
		log.info("业务申请..."+task.getVariables().toString());
		//设置任务处理候选人 流程发起者
		//String strategy = task.getVariable(Constant.STRATEGY_FLOW).toString();
		log.info(task.getVariable("starter").toString());
		task.setVariables(task.getVariables());
		task.addCandidateUser(task.getVariable("starter").toString());
	}
}
