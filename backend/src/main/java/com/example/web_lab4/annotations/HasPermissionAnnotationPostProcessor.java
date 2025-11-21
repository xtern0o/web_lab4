package com.example.web_lab4.annotations;

import com.example.web_lab4.entity.PermissionEntity;
import com.example.web_lab4.service.UserService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class HasPermissionAnnotationPostProcessor implements BeanPostProcessor {
    @Autowired
    private UserService userService;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return createProxyIfNeeded(bean);
    }

    private Object createProxyIfNeeded(Object bean) {
        Method[] methods = bean.getClass().getMethods();
        if (Arrays.stream(methods).noneMatch(method -> method.isAnnotationPresent(HasPermission.class))) {
            return bean;
        }
        Class<?> beanClass = bean.getClass();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(beanClass);
        enhancer.setCallback((MethodInterceptor) (obj, method, methodArgs, methodProxy) -> {
            Method implMethod = beanClass.getMethod(method.getName(), method.getParameterTypes());
            HasPermission hasPermissionAnnotation = implMethod.getAnnotation(HasPermission.class);
            if (hasPermissionAnnotation != null) {
                boolean hasPermission = userHasPermissions(hasPermissionAnnotation);
                if (!hasPermission) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Недостаточно прав для операции");
                }
            }
            return implMethod.invoke(bean, methodArgs);
        });

        return enhancer.create();
    }

    private boolean userHasPermissions(HasPermission hasPermissionAnnotation) {
        Set<String> requiredPermissionsSet = Set.of(hasPermissionAnnotation.value());

        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Set<String> userPermissions = userService.getPermissionsForUser(currentUserName)
                .stream()
                .map(PermissionEntity::getName)
                .collect(Collectors.toSet());

        System.out.println("Права пользователя: " + Arrays.toString(userPermissions.toArray()));
        System.out.println("Необходимые права:  " + Arrays.toString(requiredPermissionsSet.toArray()));

        return userPermissions.containsAll(requiredPermissionsSet);
    }

}
