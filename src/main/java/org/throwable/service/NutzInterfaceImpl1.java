package org.throwable.service;

import org.nutz.ioc.loader.annotation.IocBean;

/**
 * @author zhangjinci
 * @version 2017/1/23 18:02
 * @function
 */
@IocBean
public class NutzInterfaceImpl1 implements NutzInterface {

    @Override
    public String sayHello() {
        return "hello i am NutzInterfaceImpl1 !!!";
    }
}
