package com.yndongyong.autogenerator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * Created by ad15 on 2017/9/21.
 */

public class AnnotatedClass {

    private TypeElement classElement;
    private String packageName;
    private String simpleName;

    public List<BindViewField> mViewField = new ArrayList<>();
    public List<OnClickMethod> mOnClickMethod = new ArrayList<>();
    public List<ArgField> mArgField = new ArrayList<>();

    public AnnotatedClass(TypeElement typeElement, String packageName) {
        this.packageName = packageName;
        this.classElement = typeElement;
        this.simpleName = classElement.getSimpleName() + "$$Inject";
    }

    public void addOnClickMethod(OnClickMethod method) {
        this.mOnClickMethod.add(method);
    }

    public void addBindViewField(BindViewField viewField) {
        this.mViewField.add(viewField);
    }

    public void addArgFieldField(ArgField viewField) {
        this.mArgField.add(viewField);
    }


    public String getPackageName() {
        return packageName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public JavaFile generatorCode() {
        MethodSpec.Builder injectMethodBuilder =
                MethodSpec.methodBuilder("inject")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(TypeName.get(classElement.asType()), "host", Modifier.FINAL)
                        .addParameter(TypeUtil.ANDROID_VIEW, "source")
                        .addParameter(TypeUtil.MAGIC, "magic");
        //Bind view
        for (BindViewField viewField : mViewField) {
            injectMethodBuilder
                    .addStatement("host.$N = ($T)(magic.findView(source,$L))",viewField.getFieldName()
                            , ClassName.get(viewField.getType()),viewField.getViewId());
        }
        //Arg
        if (mArgField.size() > 0) {
            injectMethodBuilder
                    .addStatement("$T $L = magic.getArgs(host)",TypeUtil.ANDROID_BUNDLE,"args");

            injectMethodBuilder.beginControlFlow("if($L !=null)", "args");

            for (ArgField argField : mArgField) {

                injectMethodBuilder.beginControlFlow("if($L.containsKey($S))", "args", argField.getKey());

                injectMethodBuilder
                        .addStatement("host.$N = args.get$L($S);", argField.getFieldName()
                                , argField.getOp(),argField.getKey());

                injectMethodBuilder.endControlFlow();
            }

            injectMethodBuilder.endControlFlow();


        }
        //Bind onClik
        if (mOnClickMethod.size() > 0) {
            injectMethodBuilder.addStatement("$T listener",TypeUtil.DEBOUNCING_ON_CLICK_LISTENER);
        }
        for (OnClickMethod onClickMethod : mOnClickMethod) {
            MethodSpec onClick = MethodSpec.methodBuilder("doClick")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(TypeUtil.ANDROID_VIEW, "view")
                    .addStatement("host.$L(view)", onClickMethod.getMethodName())
                    .build();

            TypeSpec listener = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(TypeUtil.DEBOUNCING_ON_CLICK_LISTENER)
                    .addMethod(onClick)
                    .build();
            injectMethodBuilder.addStatement("listener = $L",listener);
            for (int id : onClickMethod.getIds()) {
                injectMethodBuilder.addStatement("magic.findView(source,$L).setOnClickListener(listener)", id);
            }
        }

        // TODO: 2017/9/22 bind onLongClick
        //bind UiThread
        /*if (mOnUiThreadMethod.size() > 0) {
            injectMethodBuilder.addStatement("$T runnable",TypeUtil.RUNNABLE);
        }

        for (OnUiThreadMethod uiThreadMethod : mOnUiThreadMethod) {
            MethodSpec runMethod = MethodSpec.methodBuilder("run")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addStatement("host.$L()", uiThreadMethod.getMethodName()).build();

            TypeSpec runnableClass = TypeSpec.anonymousClassBuilder("")
                    .superclass(TypeUtil.RUNNABLE)
                    .addMethod(runMethod).build();
            injectMethodBuilder.addStatement("runnable = $L", runnableClass);
            injectMethodBuilder.addStatement("int $L = $L","delayMills", uiThreadMethod.getDelayMills());
            injectMethodBuilder
                    .beginControlFlow("if($T.myLooper() != $T.getMainLooper())",TypeUtil.ANDROID_LOOPER,TypeUtil.ANDROID_LOOPER)
                    .addStatement("source.postDelayed(runnable, $L)", uiThreadMethod.getDelayMills())
                    .nextControlFlow("else")
                        .addCode(CodeBlock.builder().beginControlFlow("if($L ==0)","delayMills")
                            .addStatement("host.$L()", uiThreadMethod.getMethodName())
                        .nextControlFlow("else")
                        .addStatement("source.postDelayed(runnable, $L)", uiThreadMethod.getDelayMills())
                        .endControlFlow().build())

                    .endControlFlow();
        }
        //bind background
        if (mOnBackgroundMethod.size() > 0) {
            injectMethodBuilder.addStatement("$T timerTask",TypeUtil.JAVA_TIMERTASK);
        }
        for ( OnBackgroundMethod backgroundMethod:mOnBackgroundMethod){
            MethodSpec runInTimerTask = MethodSpec.methodBuilder("run")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("host.$L()",backgroundMethod.getMethodName())
                            .build();
            TypeSpec timerTaskInnerClass = TypeSpec.anonymousClassBuilder("")
                    .superclass(TypeUtil.JAVA_TIMERTASK)
                    .addMethod(runInTimerTask)
                    .build();

            injectMethodBuilder.addStatement("$L = $L","timerTask", timerTaskInnerClass);
            injectMethodBuilder
                    .addStatement(" $T.getInstance().schedule($L,$L);",TypeUtil.BACKGROUND_EXECUTORS,"timerTask", backgroundMethod.getDelayMills());
        }*/

        TypeSpec magicClass =
                TypeSpec.classBuilder(simpleName)//classElement.getSimpleName() + "$$Inject"
                        .addModifiers(Modifier.PUBLIC)
                        .addSuperinterface(ParameterizedTypeName.get(TypeUtil.INJECT,TypeName.get(classElement.asType())))
                        .addMethod(injectMethodBuilder.build())
                        .build();
        JavaFile javaFile = JavaFile.builder(packageName, magicClass)
                .addFileComment(" This codes are generated automatically. Do not modify!").build();

        return javaFile;
    }


}
