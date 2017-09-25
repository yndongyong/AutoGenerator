package com.yndongyong.autogenerator;

import java.util.Iterator;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by ad15 on 2017/9/21.
 */

public class BindViewField {

    private VariableElement fieldElement;
    private String fieldName;
    private int viewId;

    public BindViewField(Element element) {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(
                    String.format("%s can be annotated with Field,%s is not a field!"
                    , BindView.class.getSimpleName(), element.getSimpleName().toString()));
        }
        fieldElement = (VariableElement) element;
        fieldName = fieldElement.getSimpleName().toString();
        BindView annotation = fieldElement.getAnnotation(BindView.class);
        if (annotation.value() <0) {
            throw new IllegalArgumentException(
                    String.format("value() in %s for filed %s is  not valid!"
                            , BindView.class.getSimpleName(), element.getSimpleName().toString()));

        }

        Set<Modifier> modifiers = fieldElement.getModifiers();
        Iterator<Modifier> iterator = modifiers.iterator();
        while (iterator.hasNext()) {
            Modifier modifier = iterator.next();
            if (modifier == Modifier.PRIVATE) {
                throw new IllegalArgumentException(
                        String.format("%s can be annotated with Method and %s must not be private!"
                                , BindView.class.getSimpleName(), element.getSimpleName().toString()));
            }
        }
        viewId = annotation.value();

    }

    public int getViewId() {
        return viewId;
    }

    public VariableElement getFieldElement() {
        return fieldElement;
    }

    public String getFieldName() {
        return fieldName;
    }

    public TypeMirror getType() {
        return fieldElement.asType();
    }
}
