package com.yndongyong.autogenerator;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * Created by ad15 on 2017/9/25.
 */

public class ArgField {

    private VariableElement fieldElement;
    private String fieldName;
    private String extra_key;
    private TypeKind typeKind;

    private String type;

    public static Map<String, String> ARGUMENT_TYPES = new HashMap<>();

    public ArgField(Element element) {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(
                    String.format("%s can be annotated with Field,%s is not a field!"
                            , BindView.class.getSimpleName(), element.getSimpleName().toString()));
        }
        fieldElement = (VariableElement) element;
        fieldName = fieldElement.getSimpleName().toString();
        Arg annotation = fieldElement.getAnnotation(Arg.class);
        if (annotation.value().equals("") || annotation.value() == null) {
            extra_key = element.getSimpleName().toString();

        } else {
            extra_key = annotation.value();
        }
        TypeMirror typeMirror = fieldElement.asType();
        typeKind = typeMirror.getKind();
        type = typeMirror.toString();
    }

    public VariableElement getFieldElement() {
        return fieldElement;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getKey() {
        return extra_key;
    }

    public TypeMirror getType() {
        return fieldElement.asType();
    }

    public TypeKind getTypeKind() {
        return typeKind;
    }

    public boolean isArray() {
        return (type.endsWith("[]"));
    }

    public String getOp() {
        String op = ARGUMENT_TYPES.get(type);
        if (op != null) {
            if (isArray()) {
                return op + "Array";
            } else {
                return op;
            }

        }
        return "";
    }

    static {
        ARGUMENT_TYPES.put("java.lang.String", "String");
        ARGUMENT_TYPES.put("int", "Int");
        ARGUMENT_TYPES.put("java.lang.Integer", "Int");
        ARGUMENT_TYPES.put("long", "Long");
        ARGUMENT_TYPES.put("java.lang.Long", "Long");
        ARGUMENT_TYPES.put("double", "Double");
        ARGUMENT_TYPES.put("java.lang.Double", "Double");
        ARGUMENT_TYPES.put("short", "Short");
        ARGUMENT_TYPES.put("java.lang.Short", "Short");
        ARGUMENT_TYPES.put("float", "Float");
        ARGUMENT_TYPES.put("java.lang.Float", "Float");
        ARGUMENT_TYPES.put("byte", "Byte");
        ARGUMENT_TYPES.put("java.lang.Byte", "Byte");
        ARGUMENT_TYPES.put("boolean", "Boolean");
        ARGUMENT_TYPES.put("java.lang.Boolean", "Boolean");
        ARGUMENT_TYPES.put("char", "Char");
        ARGUMENT_TYPES.put("java.lang.Character", "Char");
        ARGUMENT_TYPES.put("java.lang.CharSequence", "CharSequence");
        ARGUMENT_TYPES.put("android.os.Bundle", "Bundle");
        ARGUMENT_TYPES.put("android.os.Parcelable", "Parcelable");
    }
}
