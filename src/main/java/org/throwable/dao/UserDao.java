package org.throwable.dao;

import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.throwable.entity.User;

/**
 * @author zhangjinci
 * @version 2017/2/4 10:55
 * @function
 */
@Repository
public class UserDao {

    @Autowired
    private NutDao dao;

    public User fetchByName(String name) {
        Cnd cnd = Cnd.NEW();
        cnd.where().and("name", "=", name);
        return dao.fetch(User.class, cnd);
    }

    public User insertUser(User user) {
        return dao.insert(user);
    }

    public void updateUser(User user) {
        dao.update(user);
    }
}
