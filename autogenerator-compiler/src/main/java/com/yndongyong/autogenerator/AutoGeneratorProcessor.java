package com.yndongyong.autogenerator;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by ad15 on 2017/9/21.
 */

@AutoService(Processor.class)
public class AutoGeneratorProcessor extends AbstractProcessor {

    private Map<String, AnnotatedClass> mAnnotatedClassMap = new HashMap<>();

    private Filer mFiler; //文件相关的辅助类
    private Elements mElementUtils; //元素相关的辅助类
    private Messager mMessager; //日志相关的辅助类

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        mElementUtils = env.getElementUtils();
        mFiler = env.getFiler();
        mMessager = env.getMessager();

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mAnnotatedClassMap.clear();
        try {
            processBindView(roundEnvironment);
            processArg(roundEnvironment);
            processOnClickMethod(roundEnvironment);
        } catch (IllegalArgumentException e) {
            errorMsg(e.getMessage());
            return true;
        }

        for (AnnotatedClass annotatedClass : mAnnotatedClassMap.values()) {
            try {
                infoMsg("DZY  %s file starting",annotatedClass.getSimpleName());
                annotatedClass.generatorCode().writeTo(mFiler);
                infoMsg("DZY  %s file done",annotatedClass.getSimpleName());
            } catch (IOException e) {
                errorMsg("DZY %s file failed ,reason: %S",annotatedClass.getSimpleName(),e.getMessage());
                return true;
            }
        }
        return false;
    }



    // TODO: 2017/9/22 verify super class
    private void  verifyModifier(Element element , Class<? extends Annotation> annotation) {
        Set<Modifier> modifiers = element.getModifiers();
        Iterator<Modifier> iterator = modifiers.iterator();
        while (iterator.hasNext()) {
            Modifier modifier = iterator.next();
            if (modifier == Modifier.PRIVATE) {
                throw new IllegalArgumentException(
                        String.format("%s can be annotated with Method or View or Field %s must not be private!"
                                , annotation.getSimpleName(), element.getSimpleName().toString()));
            }
        }
    }

    private void processOnClickMethod(RoundEnvironment env) {
        Set<? extends Element> onclickElement = env.getElementsAnnotatedWith(OnClick.class);
        for (Element element : onclickElement) {
            verifyModifier(element,OnClick.class);
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            OnClickMethod onclickMethod = new OnClickMethod(element);
            annotatedClass.addOnClickMethod(onclickMethod);
        }
    }

    private void processBindView(RoundEnvironment env) {
        Set<? extends Element> bindViewElement = env.getElementsAnnotatedWith(BindView.class);
        for (Element element : bindViewElement) {
            verifyModifier(element, BindView.class);
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            BindViewField viewField = new BindViewField(element);
            annotatedClass.addBindViewField(viewField);
        }
    }

    private void processArg(RoundEnvironment env) {
        Set<? extends Element> bindViewElement = env.getElementsAnnotatedWith(Arg.class);
        for (Element element : bindViewElement) {
            verifyModifier(element, Arg.class);
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            ArgField viewField = new ArgField(element);
            annotatedClass.addArgFieldField(viewField);
        }
    }

    private AnnotatedClass getAnnotatedClass(Element element) {
        TypeElement parentElement = (TypeElement) element.getEnclosingElement();
        String fullClassName = parentElement.getQualifiedName().toString();
        AnnotatedClass annotatedClass = mAnnotatedClassMap.get(fullClassName);
        if (annotatedClass == null) {
            TypeMirror superclass = parentElement.getSuperclass();
            String simpleName = parentElement.getSimpleName().toString();
            int indexOf = fullClassName.indexOf(simpleName);
            AnnotatedClass clasz = new AnnotatedClass(parentElement, fullClassName.substring(0,indexOf-1));
            mAnnotatedClassMap.put(fullClassName, clasz);
            return clasz;
        }
        return annotatedClass;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supports = new LinkedHashSet<>();
        supports.add(BindView.class.getCanonicalName());
        supports.add(OnClick.class.getCanonicalName());
        supports.add(Arg.class.getCanonicalName());
        return supports;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    public void errorMsg(String msg, Object... params) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, params));
    }

    public void infoMsg(String msg, Object... params) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, params));
    }
}
