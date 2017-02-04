package org.throwable.web;

import org.nutz.trans.Atom;
import org.nutz.trans.Trans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.throwable.dao.UserDao;
import org.throwable.entity.User;

import java.util.Date;

/**
 * @author zhangjinci
 * @version 2017/2/4 10:58
 * @function
 */
@Service
public class TransService {

    @Autowired
    private UserDao userDao;

    public void transImpl(){
        Trans.exec(new Atom() {
            @Override
            public void run() {
                User user = new User();
                user.setAge(22);
                user.setBirth(new Date());
                user.setEmail("43424");
                user.setName("ppppp");
                user = userDao.insertUser(user);

                user.setName("ppEx");
                user.setAge(11);
                userDao.updateUser(user);

            }
        });
    }
}
