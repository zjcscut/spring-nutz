package org.throwable;

import org.nutz.mvc.annotation.*;
import org.throwable.config.CustomComboIocProvider;
import org.throwable.config.SpringIocProvider;


@Fail("jsp:/error/500")
@IocBy(type = SpringIocProvider.class, args = {})
@Modules(value = {MainModule.class})
@Encoding(input = "UTF-8", output = "UTF-8")
public class MainModule {

}
