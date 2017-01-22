package org.throwable.web;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.throwable.service.NutzService;
import org.throwable.service.SpringService;

/**
 * @author zjc
 * @version 2017/1/23 1:32
 * @description
 */
@IocBean
public class NutzController {

	@Autowired
	private NutzService nutzService;

	@Inject
	private SpringService springService;

	public void nutzSayHello(){
		System.out.println(nutzService.nutzHello() + " " + nutzService.hashCode()
		+ " " + springService.springHello() + " " + springService.hashCode());
	}

}
