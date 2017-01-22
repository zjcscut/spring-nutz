package org.throwable.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.throwable.Application;
import org.throwable.web.NutzController;
import org.throwable.web.SpringController;

import static org.junit.Assert.*;

/**
 * @author zjc
 * @version 2017/1/23 1:38
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class AnnotationBeanPostProcessorConfigurationTest {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void Test1()throws Exception{
		NutzController nutzController = applicationContext.getBean(NutzController.class);
		SpringController springController = applicationContext.getBean(SpringController.class);
		nutzController.nutzSayHello();
		springController.springSayHello();
	}

}