package org.throwable.web;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.throwable.service.NutzInterface;
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

    @Autowired
    @Qualifier(value = "nutzInterfaceImpl1")
    private NutzInterface nutzInterfaceImpl1;

    @Inject("refer:nutzInterfaceImpl2")
    private NutzInterface nutzInterfaceImpl2;

    @At("/test/nutzhello")
    @Ok("json")
    public String nutzSayHello() {
        System.out.println("uri -- > " + Mvcs.getReq().getRequestURI());
        return (nutzService.nutzHello() + " " + nutzService.hashCode()
                + " " + springService.springHello() + " " + springService.hashCode());
    }

    @At("/test/nutzhello2")
    @Ok("json")
    public String nutzSayHello2() {
        return nutzInterfaceImpl1.sayHello() + " " + nutzInterfaceImpl2.sayHello();
    }

}
