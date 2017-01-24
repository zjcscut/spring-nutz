package org.throwable.config;

import org.nutz.castor.Castors;
import org.nutz.ioc.*;
import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.ioc.meta.IocEventSet;
import org.nutz.ioc.meta.IocField;
import org.nutz.ioc.meta.IocObject;
import org.nutz.ioc.meta.IocValue;
import org.nutz.json.Json;
import org.nutz.lang.Mirror;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.resource.Scans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhangjinci
 * @version 2017/1/23 16:13
 * @function
 */
public class CustomNutzAnnotationIocLoader implements IocLoader {


    private static final Log log = Logs.get();

    private HashMap<String, IocObject> map = new HashMap<String, IocObject>();

    protected String[] packages;

    public CustomNutzAnnotationIocLoader(String... packages) {
        for (String packageZ : packages) {
            for (Class<?> classZ : Scans.me().scanPackage(packageZ)) {
                addClass(classZ);
            }
        }
        if (map.size() > 0) {
            if (log.isInfoEnabled())
                log.infof("Found %s classes in %s base-packages!\nbeans = %s",
                        map.size(),
                        packages.length,
                        Castors.me().castToString(map.keySet()));
        } else {
            log.warn("NONE Annotation-Class found!! Check your ioc configure!! packages="
                    + Arrays.toString(packages));
        }
        this.packages = packages;
    }


    protected void addClass(Class<?> classZ) {
        if (classZ.isInterface()
                || classZ.isMemberClass()
                || classZ.isEnum()
                || classZ.isAnnotation()
                || classZ.isAnonymousClass())
            return;
        int modify = classZ.getModifiers();
        if (Modifier.isAbstract(modify) || (!Modifier.isPublic(modify)))
            return;
        handleNutzAnnotation(classZ);

        handleSpringAnnotation(classZ);

    }

    private void handleNutzAnnotation(Class<?> classZ) {
        IocBean iocBean = classZ.getAnnotation(IocBean.class);
        if (iocBean != null) {
            if (log.isDebugEnabled())
                log.debugf("Found @IocBean : %s", classZ);

            // 采用 @IocBean->name
            String beanName = iocBean.name();
            if (Strings.isBlank(beanName)) {
                // 否则采用 @InjectName
                InjectName innm = classZ.getAnnotation(InjectName.class);
                if (null != innm && !Strings.isBlank(innm.value())) {
                    beanName = innm.value();
                }
                // 大哥（姐），您都不设啊!? 那就用 simpleName 吧
                else {
                    beanName = Strings.lowerFirst(classZ.getSimpleName());
                }
            }

            // 重名了, 需要用户用@IocBean(name="xxxx") 区分一下
            if (map.containsKey(beanName))
                throw new IocException(beanName,
                        "Duplicate beanName=%s, by %s !!  Have been define by %s !!",
                        beanName,
                        classZ.getName(),
                        map.get(beanName).getType().getName());

            IocObject iocObject = new IocObject();
            iocObject.setType(classZ);
            map.put(beanName, iocObject);

            iocObject.setSingleton(iocBean.singleton());
            if (!Strings.isBlank(iocBean.scope()))
                iocObject.setScope(iocBean.scope());

            // 看看构造函数都需要什么函数
            String[] args = iocBean.args();
            // if (null == args || args.length == 0)
            // args = iocBean.param();
            if (null != args && args.length > 0)
                for (String value : args)
                    iocObject.addArg(Iocs.convert(value, true));

            // 设置Events
            IocEventSet eventSet = new IocEventSet();
            iocObject.setEvents(eventSet);
            if (!Strings.isBlank(iocBean.create()))
                eventSet.setCreate(iocBean.create().trim().intern());
            if (!Strings.isBlank(iocBean.depose()))
                eventSet.setDepose(iocBean.depose().trim().intern());
            if (!Strings.isBlank(iocBean.fetch()))
                eventSet.setFetch(iocBean.fetch().trim().intern());

            // 处理字段(以@Inject方式,位于字段)
            List<String> fieldList = new ArrayList<>();
            Mirror<?> mirror = Mirror.me(classZ);
            Field[] fields = mirror.getFields(Inject.class);
            for (Field field : fields) {
                Inject inject = field.getAnnotation(Inject.class);
                // 无需检查,因为字段名是唯一的
                // if(fieldList.contains(field.getName()))
                // throw duplicateField(classZ,field.getName());
                IocField iocField = new IocField();
                iocField.setName(field.getName());
                IocValue iocValue;
                if (Strings.isBlank(inject.value())) {
                    iocValue = new IocValue();
                    iocValue.setType(IocValue.TYPE_REFER_TYPE);
                    iocValue.setValue(field);
                } else
                    iocValue = Iocs.convert(inject.value(), true);
                iocField.setValue(iocValue);
                iocField.setOptional(inject.optional());
                iocObject.addField(iocField);
                fieldList.add(iocField.getName());
            }

            //兼容spring的Autowired和Qualifier
            Field[] AutowiredFields = mirror.getFields(Autowired.class);
            for (Field field : AutowiredFields) {
                Qualifier qualifier = field.getAnnotation(Qualifier.class);
                // 无需检查,因为字段名是唯一的
                // if(fieldList.contains(field.getName()))
                // throw duplicateField(classZ,field.getName());
                IocField iocField = new IocField();
                iocField.setName(field.getName());
                IocValue iocValue;
                if (null == qualifier || Strings.isBlank(qualifier.value())) {
                    iocValue = new IocValue();
                    iocValue.setType(IocValue.TYPE_REFER_TYPE);
                    iocValue.setValue(field);
                } else {
                    iocValue = Iocs.convert("refer:" + qualifier.value(), true);
                }
                iocField.setValue(iocValue);
                iocField.setOptional(false);
                iocObject.addField(iocField);
                fieldList.add(iocField.getName());
            }


            // 处理字段(以@Inject方式,位于set方法)
            Method[] methods;
            try {
                methods = classZ.getMethods();
            } catch (Exception e) {
                // 如果获取失败,就忽略之
                log.infof("Fail to call getMethods() in Class=%s, miss class or Security Limit, ignore it",
                        classZ,
                        e);
                methods = new Method[0];
            } catch (NoClassDefFoundError e) {
                log.infof("Fail to call getMethods() in Class=%s, miss class or Security Limit, ignore it",
                        classZ,
                        e);
                methods = new Method[0];
            }

            //set方法 -- nutz
            doNutzMethodAnnotationInject(methods, classZ, beanName, fieldList, iocObject);

            //set方法 -- spring
            doSpringMethodAnnotationInject(methods, classZ, beanName, fieldList, iocObject);

            // 处理字段(以@IocBean.field方式)
            String[] flds = iocBean.fields();
            if (flds != null && flds.length > 0) {
                for (String fieldInfo : flds) {
                    if (fieldList.contains(fieldInfo))
                        throw duplicateField(beanName, classZ, fieldInfo);
                    IocField iocField = new IocField();
                    if (fieldInfo.contains(":")) { // dao:jndi:dataSource/jdbc形式
                        String[] datas = fieldInfo.split(":", 2);
                        // 完整形式, 与@Inject完全一致了
                        iocField.setName(datas[0]);
                        iocField.setValue(Iocs.convert(datas[1], true));
                        iocObject.addField(iocField);
                    } else {
                        // 基本形式, 引用与自身同名的bean
                        iocField.setName(fieldInfo);
                        IocValue iocValue = new IocValue();
                        iocValue.setType(IocValue.TYPE_REFER);
                        iocValue.setValue(fieldInfo);
                        iocField.setValue(iocValue);
                        iocObject.addField(iocField);
                    }
                    fieldList.add(iocField.getName());
                }
            }

            // 处理工厂方法
            if (!Strings.isBlank(iocBean.factory())) {
                iocObject.setFactory(iocBean.factory());
            }
        } else {
            // 这里只是检查一下@Inject,要避免抛出异常.
            try {
                if (log.isWarnEnabled()) {
                    Field[] fields = classZ.getDeclaredFields();
                    for (Field field : fields)
                        if (field.getAnnotation(Inject.class) != null || field.getAnnotation(Autowired.class) != null) {
                            log.warnf("class(%s) don't has @IocBean, but field(%s) has @Inject! or @Autowired Miss @IocBean ??",
                                    classZ.getName(),
                                    field.getName());
                            break;
                        }
                }
            } catch (Throwable e) {
                // 无需处理.
            }
        }
    }


    private void handleSpringAnnotation(Class<?> classZ) {
        Component iocBean = AnnotationUtils.findAnnotation(classZ, Component.class);
        IocBean i = AnnotationUtils.findAnnotation(classZ, IocBean.class);
        if (null == i && null != iocBean) {
            if (log.isDebugEnabled())
                log.debugf("Found @IocBean : %s", classZ);

            // 采用 @IocBean->name
            String beanName = iocBean.value();
            if (beanName.length() == 0){
                beanName = Strings.lowerFirst(classZ.getSimpleName());  //这里必须simpleName
            }

            // 重名了, 需要用户用@IocBean(name="xxxx") 区分一下
            if (map.containsKey(beanName))
                throw new IocException(beanName,
                        "Duplicate beanName=%s, by %s !!  Have been define by %s !!",
                        beanName,
                        classZ.getName(),
                        map.get(beanName).getType().getName());

            IocObject iocObject = new IocObject();
            iocObject.setType(classZ);
            map.put(beanName, iocObject);
            iocObject.setSingleton(true);

            // 处理字段(以@Inject方式,位于字段)
            List<String> fieldList = new ArrayList<>();
            Mirror<?> mirror = Mirror.me(classZ);
            Field[] fields = mirror.getFields(Inject.class);
            for (Field field : fields) {
                Inject inject = field.getAnnotation(Inject.class);
                // 无需检查,因为字段名是唯一的
                // if(fieldList.contains(field.getName()))
                // throw duplicateField(classZ,field.getName());
                IocField iocField = new IocField();
                iocField.setName(field.getName());
                IocValue iocValue;
                if (Strings.isBlank(inject.value())) {
                    iocValue = new IocValue();
                    iocValue.setType(IocValue.TYPE_REFER_TYPE);
                    iocValue.setValue(field);
                } else
                    iocValue = Iocs.convert(inject.value(), true);
                iocField.setValue(iocValue);
                iocField.setOptional(inject.optional());
                iocObject.addField(iocField);
                fieldList.add(iocField.getName());
            }

            //兼容spring的Autowired和Qualifier
            Field[] AutowiredFields = mirror.getFields(Autowired.class);
            for (Field field : AutowiredFields) {
                Qualifier qualifier = field.getAnnotation(Qualifier.class);
                // 无需检查,因为字段名是唯一的
                // if(fieldList.contains(field.getName()))
                // throw duplicateField(classZ,field.getName());
                IocField iocField = new IocField();
                iocField.setName(field.getName());
                IocValue iocValue;
                if (null == qualifier || Strings.isBlank(qualifier.value())) {
                    iocValue = new IocValue();
                    iocValue.setType(IocValue.TYPE_REFER_TYPE);
                    iocValue.setValue(field);
                } else {
                    iocValue = Iocs.convert("refer:" + qualifier.value(), true);
                }
                iocField.setValue(iocValue);
                iocField.setOptional(false);
                iocObject.addField(iocField);
                fieldList.add(iocField.getName());
            }


            // 处理字段(以@Inject方式,位于set方法)
            Method[] methods;
            try {
                methods = classZ.getMethods();
            } catch (Exception e) {
                // 如果获取失败,就忽略之
                log.infof("Fail to call getMethods() in Class=%s, miss class or Security Limit, ignore it",
                        classZ,
                        e);
                methods = new Method[0];
            } catch (NoClassDefFoundError e) {
                log.infof("Fail to call getMethods() in Class=%s, miss class or Security Limit, ignore it",
                        classZ,
                        e);
                methods = new Method[0];
            }

            //set方法 -- nutz
            doNutzMethodAnnotationInject(methods, classZ, beanName, fieldList, iocObject);

            //set方法 -- spring
            doSpringMethodAnnotationInject(methods, classZ, beanName, fieldList, iocObject);

        } else {
            // 这里只是检查一下@Inject,要避免抛出异常.
            try {
                if (log.isWarnEnabled()) {
                    Field[] fields = classZ.getDeclaredFields();
                    for (Field field : fields)
                        if (field.getAnnotation(Inject.class) != null || field.getAnnotation(Autowired.class) != null) {
                            log.warnf("class(%s) don't has @IocBean, but field(%s) has @Inject! or @Autowired Miss @IocBean ??",
                                    classZ.getName(),
                                    field.getName());
                            break;
                        }
                }
            } catch (Throwable e) {
                // 无需处理.
            }
        }
    }


    private void doNutzMethodAnnotationInject(Method[] methods, Class<?> classZ, String beanName, List<String> fieldList,
                                              IocObject iocObject) {
        for (Method method : methods) {
            Inject inject = method.getAnnotation(Inject.class);
            if (inject == null)
                continue;
            // 过滤特殊方法
            int m = method.getModifiers();
            if (Modifier.isAbstract(m) || (!Modifier.isPublic(m)) || Modifier.isStatic(m))
                continue;
            String methodName = method.getName();
            if (methodName.startsWith("set")
                    && methodName.length() > 3
                    && method.getParameterTypes().length == 1) {
                IocField iocField = new IocField();
                iocField.setName(Strings.lowerFirst(methodName.substring(3)));
                if (fieldList.contains(iocField.getName()))
                    throw duplicateField(beanName, classZ, iocField.getName());
                IocValue iocValue;
                if (Strings.isBlank(inject.value())) {
                    iocValue = new IocValue();
                    iocValue.setType(IocValue.TYPE_REFER_TYPE);
                    iocValue.setValue(Strings.lowerFirst(methodName.substring(3)) + "#" + method.getParameterTypes()[0].getName());
                } else
                    iocValue = Iocs.convert(inject.value(), true);
                iocField.setValue(iocValue);
                iocObject.addField(iocField);
                fieldList.add(iocField.getName());
            }
        }
    }

    private void doSpringMethodAnnotationInject(Method[] methods, Class<?> classZ, String beanName, List<String> fieldList,
                                                IocObject iocObject) {
        for (Method method : methods) {
            Autowired autowired = method.getAnnotation(Autowired.class);
            if (autowired == null)
                continue;
            // 过滤特殊方法
            int m = method.getModifiers();
            if (Modifier.isAbstract(m) || (!Modifier.isPublic(m)) || Modifier.isStatic(m))
                continue;
            String methodName = method.getName();
            if (methodName.startsWith("set")
                    && methodName.length() > 3
                    && method.getParameterTypes().length == 1) {
                IocField iocField = new IocField();
                iocField.setName(Strings.lowerFirst(methodName.substring(3)));
                if (fieldList.contains(iocField.getName()))
                    throw duplicateField(beanName, classZ, iocField.getName());
                IocValue iocValue;
                Qualifier qualifier = method.getAnnotation(Qualifier.class);
                if (null == qualifier || Strings.isBlank(qualifier.value())) {
                    iocValue = new IocValue();
                    iocValue.setType(IocValue.TYPE_REFER_TYPE);
                    iocValue.setValue(Strings.lowerFirst(methodName.substring(3)) + "#" + method.getParameterTypes()[0].getName());
                } else {
                    iocValue = Iocs.convert("refer:" + qualifier.value(), true);
                }

                iocField.setValue(iocValue);
                iocObject.addField(iocField);
                fieldList.add(iocField.getName());
            }
        }
    }


    public String[] getName() {
        return map.keySet().toArray(new String[map.size()]);
    }

    public boolean has(String name) {
        return map.containsKey(name);
    }

    public IocObject load(IocLoading loading, String name) throws ObjectLoadException {
        if (has(name))
            return map.get(name);
        throw new ObjectLoadException("Object '" + name + "' without define! Pls check your ioc configure");
    }

    private static final IocException duplicateField(String beanName, Class<?> classZ, String name) {
        return new IocException(beanName,
                "Duplicate filed defined! Class=%s,FileName=%s",
                classZ,
                name);
    }

    public String toString() {
        return "/*CustomNutzAnnotationIocLoader*/\n" + Json.toJson(map);
    }

    public String[] getPackages() {
        return packages;
    }
}
