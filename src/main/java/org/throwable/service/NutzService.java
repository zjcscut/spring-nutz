package org.throwable.service;

import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.IocBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zjc
 * @version 2017/1/23 1:33
 * @description
 */
@IocBean
public class NutzService {

	@Autowired
	private SpringService springService;

	public String nutzHello(){
		return "hello,this is nutz service!" + springService.nutzTrans2();
	}


	@Aop({"sysTransactionInterceptor"})
	public String nutzTrans2(){
		return "hello,this is nutz trans2!";
	}

}
