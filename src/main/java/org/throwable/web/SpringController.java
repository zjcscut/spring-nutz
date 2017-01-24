package org.throwable.web;

import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.throwable.service.NutzService;
import org.throwable.service.SpringService;

/**
 * @author zjc
 * @version 2017/1/23 1:32
 * @description
 */
@Controller
public class SpringController {

    @Autowired
    private NutzService nutzService;

    @Inject
    private SpringService springService;

    @RequestMapping(value = "/test/springhello")
    @ResponseBody
    public String springSayHello() {
        return (nutzService.nutzHello() + " " + nutzService.hashCode()
                + " " + springService.springHello() + " " + springService.hashCode());
    }
}
