package net.idik.lib.cipher.so.task

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import net.idik.lib.cipher.so.extension.KeyExt
import net.idik.lib.cipher.so.utils.StringUtils
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

    @TaskAction
    void generate() {

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder("CipherClient")
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                .addMethod(
                MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .addException(IllegalAccessException.class)
                        .addStatement("throw new IllegalAccessException()")
                        .build())

        def androidBase64ClassName = ClassName.get("android.util", "Base64")
        keyExts.each {
            classBuilder.addMethod(
                    MethodSpec.methodBuilder("${it.name}")
                            .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                            .returns(String.class)
                            .addStatement('return new $T($T.decode(CipherCore.get("$L"), $T.DEFAULT))', String.class, androidBase64ClassName, StringUtils.md5(it.name), androidBase64ClassName)
                            .build()
            )
        }

        JavaFile.builder("net.idik.lib.cipher.so", classBuilder.build()).build().writeTo(outputDir)

    }
}