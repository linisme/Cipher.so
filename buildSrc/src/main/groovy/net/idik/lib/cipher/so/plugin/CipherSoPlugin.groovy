package net.idik.lib.cipher.so.plugin

import com.android.build.gradle.AppExtension
import net.idik.lib.cipher.so.extension.CipherExt
import net.idik.lib.cipher.so.generater.CMakeListsBuilder
import net.idik.lib.cipher.so.task.GenerateCipherSoHeaderTask
import net.idik.lib.cipher.so.task.GenerateJavaClientFileTask
import net.idik.lib.cipher.so.utils.IOUtils
import net.idik.lib.cipher.so.utils.StringUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy

class CipherSoPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.extensions.add("cipher", new CipherExt(project))

        setupProjectNativeSupport(project)

    }

    private static def createTasks(Project project, AppExtension android) {

        def archiveFile = getNativeArchiveFile(project)
        def copyNativeArchiveTask = project.tasks.create("copyNativeArchive", Copy) {
            group "cipher.so"
            from archiveFile
            include "src/main/cpp/**"
            include "CMakeLists.txt"
            exclude "src/main/cpp/include/extern-keys.h"
            into new File(project.buildDir, "cipher.so")
        }
        android.applicationVariants.all { variant ->
            def keyExts = project.cipher.soExt.keys.asList()
            def generateCipherSoExternTask = project.tasks.create("generate${StringUtils.capitalize(variant.name)}CipherSoHeader", GenerateCipherSoHeaderTask)
            generateCipherSoExternTask.configure {
                it.keyExts = keyExts
                it.outputDir = IOUtils.getNativeHeaderDir(project)
            }
            project.getTasksByName("generateJsonModel${StringUtils.capitalize(variant.name)}", false).each {
                it.dependsOn copyNativeArchiveTask
                it.dependsOn generateCipherSoExternTask
            }
            def generateJavaClientTask = project.tasks.create("generate${StringUtils.capitalize(variant.name)}JavaClient", GenerateJavaClientFileTask)
            def outputDir = new File("${project.buildDir}/generated/source/cipher.so/${variant.name}")
            generateJavaClientTask.configure {
                it.keyExts = keyExts
                it.outputDir = outputDir
            }
            variant.registerJavaGeneratingTask(generateJavaClientTask, outputDir)
        }
    }


    private static def setupProjectNativeSupport(Project project) {
        project.afterEvaluate {
            unzipNativeArchive(project)
            def android = project.extensions.findByType(AppExtension)
            File targetFile = generateCMakeListsFile(project, android)
            android.externalNativeBuild {
                cmake {
                    path targetFile.path
                }
            }
            createTasks(project, android)
        }
    }

    private static def unzipNativeArchive(Project project) {
        def archiveFile = getNativeArchiveFile(project)
        project.copy {
            from archiveFile
            include "src/main/cpp/**"
            include "CMakeLists.txt"
            exclude "src/main/cpp/include/extern-keys.h"
            into new File(project.buildDir, "cipher.so")
        }

    }

    private static def getNativeArchiveFile(Project project) {
        if (project.rootProject.subprojects.find { it.name == "devso" } != null) {
            return project.rootProject.file("devso").path
        } else {
            def archiveZip = findNativeArchiveFromBuildscript(project)
            if (archiveZip == null) {
                archiveZip = findNativeArchiveFromBuildscript(project.rootProject)
            }
            archiveZip
        }
    }

    private static def findNativeArchiveFromBuildscript(Project project) {
        def archiveZip = null
        project.buildscript.configurations.findAll {
            project.gradle.gradleVersion >= '4.0' ? it.isCanBeResolved() : true
        }.each { config ->
            File file = config.files.find { it.name.contains("cipher.so") }
            if (file != null) {
                archiveZip = project.zipTree(file)
            }
        }
        return archiveZip
    }

    private static File generateCMakeListsFile(Project project, AppExtension android) {
        def outputDir = new File("${project.buildDir.path}/cipher.so/cmake")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        def targetFile = new File(outputDir, "CMakeLists.txt")
        def writer = new FileWriter(targetFile)
        new CMakeListsBuilder("${project.buildDir.path}/cipher.so/CMakeLists.txt").setOriginCMakePath(android.externalNativeBuild.cmake.path?.path).build().each {
            writer.append(it)
        }
        writer.flush()
        writer.close()
        targetFile
    }
}