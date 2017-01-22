package org.throwable.service;

import org.nutz.ioc.loader.annotation.IocBean;

/**
 * @author zjc
 * @version 2017/1/23 1:33
 * @description
 */
@IocBean
public class NutzService {

	public String nutzHello(){
		return "hello,this is nutz service!";
	}
}
