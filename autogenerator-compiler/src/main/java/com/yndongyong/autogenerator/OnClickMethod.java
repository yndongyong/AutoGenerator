package com.yndongyong.autogenerator;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * Created by ad15 on 2017/9/21.
 */

public class OnClickMethod {

    private ExecutableElement methodElement;
    private String methodName;
    public int[] ids;

    public OnClickMethod(Element element) {

        if (element.getKind() != ElementKind.METHOD) {
            throw new IllegalArgumentException(
                    String.format("%s can be annotated with Method,%s is not a Method!"
                            , BindView.class.getSimpleName(), element.getSimpleName().toString()));
        }
        methodElement = (ExecutableElement) element;
        ids = methodElement.getAnnotation(OnClick.class).value();
        if (ids.length == 0) {
            throw new IllegalArgumentException(
                    String.format("%s can be annotated with Method and it is ids must be valid!"
                            , BindView.class.getSimpleName(), element.getSimpleName().toString()));
        }

        methodElement = (ExecutableElement) element;
        Set<Modifier> modifiers = methodElement.getModifiers();
        Iterator<Modifier> iterator = modifiers.iterator();
        while (iterator.hasNext()) {
            Modifier modifier = iterator.next();
            if (modifier != Modifier.PUBLIC) {
                throw new IllegalArgumentException(
                        String.format("%s can be annotated with Method and %s must be public!"
                                , BindView.class.getSimpleName(), element.getSimpleName().toString()));
            }
        }
        methodName = methodElement.getSimpleName().toString();

        List<? extends VariableElement> parameters = methodElement.getParameters();
        int size = parameters.size();
        if (size != 1) {
            throw new IllegalArgumentException(
                    String.format("Method %s annotated with %s must have a parameters and type is View  !"
                            , element.getSimpleName().toString(),OnClick.class.getSimpleName()));
        }
        VariableElement variableElement = parameters.get(0);

        if (!variableElement.asType().toString().contains("View")) {
            throw new IllegalArgumentException(
                    String.format("Method %s annotated with %s must have a parameters and type is View  !!"
                            , element.getSimpleName().toString(),OnClick.class.getSimpleName()));
        }
    }

    public String getMethodName() {
        return methodName;
    }
    public TypeMirror getType() {
        return methodElement.asType();
    }

    public int[] getIds() {
        return ids;
    }
}
