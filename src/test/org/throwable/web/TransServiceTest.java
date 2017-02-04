package org.throwable.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.throwable.Application;

import static org.junit.Assert.*;

/**
 * @author zhangjinci
 * @version 2017/2/4 11:01
 * @function
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TransServiceTest {

    @Autowired
    private TransService transService;

    @Test
    public void test1()throws Exception{
        transService.transImpl();
    }
}