package net.idik.lib.cipher.so.task

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import net.idik.lib.cipher.so.extension.KeyExt
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import javax.lang.model.element.Modifier

class GenerateJavaClientFileTask extends DefaultTask {

    @OutputDirectory
    File outputDir

    @Input
    List<KeyExt> keyExts

//    static {
//        System.loadLibrary("cipher-lib");
//        init();
//    }
//
//    public static String get(String key) {
//        return getString(key);
//    }
//
//    private static native void init();
//
//    private static native String getString(String key);

    @TaskAction
    void generate() {

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder("CipherClient")
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                .addStaticBlock(CodeBlock.of(
                "System.loadLibrary(\"cipher-lib\");\n init();\n"))
                .addMethod(
                MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .addException(IllegalAccessException.class)
                        .addStatement("throw new IllegalAccessException()")
                        .build())
                .addMethod(
                MethodSpec.methodBuilder("init").addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL, Modifier.NATIVE)
                        .build())
                .addMethod(
                MethodSpec.methodBuilder("getString").addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.NATIVE)
                        .returns(String.class)
                        .addParameter(String.class, "key")
                        .build())



        keyExts.each {
            classBuilder.addMethod(
                    MethodSpec.methodBuilder("${it.name}")
                            .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                            .returns(String.class)
                            .addStatement("return getString(\"${it.name}\")")
                            .build()
            )
        }

        JavaFile.builder("net.idik.lib.cipher.so", classBuilder.build()).build().writeTo(outputDir)

    }
}