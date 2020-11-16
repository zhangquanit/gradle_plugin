package com.example.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

import java.lang.reflect.Method;

class MyPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def clazz = project.getClass()
        while (clazz != null) {
            println("-------"+clazz.getName()+"----------")
            def methods = clazz.getDeclaredMethods()
            for (Method method : methods) {
                try {
                    if (method.getName().startsWith("get")) {
                        method.setAccessible(true)
                        def types = method.getParameterTypes()
                        if (null == types || types.length == 0) {
                            def value = method.invoke(project)
                            println(method.getName() + "=" + value)
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace()
                }
            }
            clazz = clazz.getSuperclass()
        }
        //创建test dsl
        def extension = project.extensions.create("test", MyExtension)
        //创建task
        project.task("say") {
            println extension.message
        }
    }
}
