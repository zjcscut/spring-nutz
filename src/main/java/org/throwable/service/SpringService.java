package org.throwable.service;

import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.IocBean;
import org.springframework.stereotype.Service;

/**
 * @author zjc
 * @version 2017/1/23 1:33
 * @description
 */
@Service
public class SpringService {

	public String springHello(){
		return "hello,this is spring service!";
	}

	@Aop({"sysTransactionInterceptor"})
	public String nutzTrans2(){
		return "hello,this is nutz trans2!";
	}
}
