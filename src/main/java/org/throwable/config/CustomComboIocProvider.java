package org.throwable.config;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.impl.ScopeContext;
import org.nutz.lang.Lang;
import org.nutz.mvc.IocProvider;
import org.nutz.mvc.NutConfig;

/**
 * @author zhangjinci
 * @version 2017/1/23 16:40
 * @function
 */
public class CustomComboIocProvider implements IocProvider {

    @Override
    public Ioc create(NutConfig config, String[] args) {
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].contains("${main}"))
                    args[i] = args[i].replace("${main}", config.getMainModule().getPackage().getName());
            }
            return new NutIoc(new CustomComboIocLoader(args), new ScopeContext("app"), "app");
        }
        catch (ClassNotFoundException e) {
            throw Lang.wrapThrow(e);
        }
    }
}
