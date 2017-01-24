package org.throwable.service;

import org.springframework.stereotype.Service;

/**
 * @author zhangjinci
 * @version 2017/1/23 18:02
 * @function
 */
@Service
public class NutzInterfaceImpl2 implements NutzInterface {

    @Override
    public String sayHello() {
        return "hello i am NutzInterfaceImpl2 !!!";
    }
}
