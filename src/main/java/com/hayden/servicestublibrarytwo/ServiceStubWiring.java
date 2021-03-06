package com.hayden.servicestublibrarytwo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceStubWiring implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private List<Object> servicesStubs;

    @Value("${spring.profiles.active}")
    String[] profiles;

    @Override
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setSpringObjects(List<Object> objects)
    {
        this.servicesStubs = objects.stream()
                .filter(obj -> Arrays.stream(obj.getClass().getAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().equals(ServiceStub.class))
                )
                .collect(Collectors.toList());
    }

    @PostConstruct
    public void configureBeanFactoryWithServiceStubs()
    {
        if (this.applicationContext instanceof ConfigurableApplicationContext) {
            var ctx = (ConfigurableApplicationContext) this.applicationContext;
            System.out.println("is instance of!");
            ConfigurableListableBeanFactory beanFactory = ctx.getBeanFactory();
            servicesStubs.stream()
                    .peek(obj -> {
                              ServiceStub annotation = obj.getClass().getAnnotation(ServiceStub.class);
                              if (Arrays.stream(profiles)
                                      .anyMatch(str -> str.equalsIgnoreCase(annotation.profile()))
                              )
                                  beanFactory.registerResolvableDependency((annotation).value(), obj);
                          }
                    )
                    .map(Object::getClass)
                    .forEach(objs -> beanFactory.getBeansOfType(objs.getAnnotation(ServiceStub.class).value())
                            .forEach((key, toDestroy) -> {
                                if (!objs.getSimpleName().equals(toDestroy.getClass().getSimpleName()))
                                    beanFactory.destroyBean(toDestroy);
                            })
                    );
            servicesStubs.forEach(obj -> beanFactory.registerSingleton(obj.getClass().getSimpleName(), obj));
        }
    }
}
