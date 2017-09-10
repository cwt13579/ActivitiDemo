package com.anxpp.demo.activiti;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anxpp.demo.activiti.core.entity.User;
import com.anxpp.demo.activiti.core.repo.UserRepo;
import com.anxpp.demo.activiti.core.service.UserService;
import com.anxpp.demo.activiti.core.service.impl.UserServiceImpl;
import com.anxpp.demo.activiti.simple.Config.Constant;
import com.anxpp.demo.activiti.simple.core.entity.ApplySimple;
import com.anxpp.demo.activiti.simple.core.service.ApplySimpleService;
import com.anxpp.demo.activiti.simple.core.service.impl.ApplySimpleImpl;
import com.anxpp.demo.activiti.utils.SpringUtil;

@RestController("/")
@SpringBootApplication
public class ActivitiDemoApplication {
	private final static Logger log = LoggerFactory.getLogger(ActivitiDemoApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(ActivitiDemoApplication.class, args);
	}
	@Autowired
    private ApplySimpleService applySimpleService;
	@Autowired
	private UserRepo userRepo;
	@Bean
	@Transactional
	public CommandLineRunner init(){
		return args->{
//			UserService userService = SpringUtil.getBean(UserServiceImpl.class);
			log.info("程序初始化...");
//			User user = new User();
//			user.setId(0L);
//			user.setDept(10L);
//			user.setName("cwt");
//			userService.save(user);
//			
//			User user1 = new User();
//			user1.setId(1L);
//			user1.setDept(10L);
//			user1.setName("admin");
//			userService.save(user1);
		};
	}
	@RequestMapping("/start")
	public void start() {
		applySimpleService = SpringUtil.getBean(ApplySimpleImpl.class);
		ApplySimple applySimple = new ApplySimple();
		applySimple.setComtent("good");
		applySimple.setInsertAt(new Date());
		applySimple.setUpdateBy(0L);
		applySimple.setInsertBy(0L);
		applySimpleService.startProcess(applySimple);
	}
	@RequestMapping("/list0")
	public List<Task> taskList0(){
		return applySimpleService.getTaskByUid(0L);
	}
	@RequestMapping("/list1")
	public List<Task> taskList1(){
		return applySimpleService.getTaskByUid(1L);
	}
	@RequestMapping("/list2")
	public List<Task> taskList2(){
		return applySimpleService.getTaskByAppno(11L);
	}
	@RequestMapping("/complete")
	public void completeSimpleCheck() {
		applySimpleService.completeSimpleCheck("30018", Constant.STATE_TO_CHECK);
	}
	@RequestMapping("/end")
	public void end() {
		applySimpleService.completeSimpleCheck("30018", Constant.CHECK_PASS);
	}
	
	@RequestMapping("/his")
	public List<HistoricTaskInstance> hisList(){
		return applySimpleService.getHisTaskByUid(0L);
	}
	@RequestMapping("/his1")
	public List<HistoricTaskInstance> hisList1(){
		return applySimpleService.getHisTaskByAppno(11L);
	}
	@RequestMapping("/q")
	public void selfQuery() {
		
	}
}
