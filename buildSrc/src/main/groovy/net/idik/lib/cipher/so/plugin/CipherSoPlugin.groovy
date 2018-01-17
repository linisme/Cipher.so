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

    private String originCmakeListPath

    @Override
    void apply(Project project) {

        project.extensions.add("cipher", new CipherExt(project))

        setupProjectNativeSupport(project)

    }

    private def createTasks(Project project, AppExtension android) {

        def generateCmakeListFileTask = project.tasks.create("generateCmakeListFile") {
            group "cipher.so"
            doLast {
                generateCMakeListsFile(project, originCmakeListPath)
            }
        }

        def archiveFile = getNativeArchiveFile(project)
        def copyNativeArchiveTask = project.tasks.create("copyNativeArchive", Copy) {
            group "cipher.so"
            from archiveFile
            include "src/main/cpp/**"
            include "CMakeLists.txt"
            exclude "src/main/cpp/include/extern-keys.h"
            into new File(project.buildDir, "cipher.so")
        }

        copyNativeArchiveTask.dependsOn generateCmakeListFileTask

        android.applicationVariants.all { variant ->

            def configs = project.cipher.so
            def keys = configs.keys.asList()
            def generateCipherSoExternTask = project.tasks.create("generate${StringUtils.capitalize(variant.name)}CipherSoHeader", GenerateCipherSoHeaderTask)
            generateCipherSoExternTask.configure {
                it.keyExts = keys
                it.outputDir = IOUtils.getNativeHeaderDir(project)
                it.signature = configs.signature
            }
            project.getTasksByName("generateJsonModel${StringUtils.capitalize(variant.name)}", false).each {
                it.dependsOn copyNativeArchiveTask
                it.dependsOn generateCipherSoExternTask
            }
            def outputDir = new File(project.buildDir, "/generated/source/cipher.so/${variant.name}")
            def generateJavaClientTask = project.tasks.create("generate${StringUtils.capitalize(variant.name)}JavaClient", GenerateJavaClientFileTask)
            generateJavaClientTask.configure {
                it.keyExts = keys
                it.outputDir = outputDir
            }
            variant.registerJavaGeneratingTask(generateJavaClientTask, outputDir)

            def copyJavaArchiveTask = project.tasks.create("copyJavaArchive${StringUtils.capitalize(variant.name)}", Copy) {
                group "cipher.so"
                from archiveFile
                include "src/main/java/**"
                exclude "src/main/java/net/idik/lib/cipher/so/devso/**"
                exclude "META-INF/**"
                exclude "net/idik/lib/cipher/so/**"
                exclude "CMakeLists.txt"
                eachFile {
                    it.path = it.path.replaceFirst("src/main/java/", "")
                }
                into outputDir
            }

            generateJavaClientTask.dependsOn copyJavaArchiveTask
        }
    }


    private def setupProjectNativeSupport(Project project) {
        project.afterEvaluate {
            unzipNativeArchive(project)
            def android = project.extensions.findByType(AppExtension)
            originCmakeListPath = android.externalNativeBuild.cmake.path?.canonicalPath
            File targetFile = generateCMakeListsFile(project, originCmakeListPath)
            android.externalNativeBuild {
                cmake {
                    path targetFile.canonicalPath
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
            return project.rootProject.file("devso").canonicalPath
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
            File file = config.files.find {
                it.name.toUpperCase(Locale.getDefault()).contains("CIPHER.SO")
            }
            if (file != null) {
                archiveZip = project.zipTree(file)
            }
        }
        return archiveZip
    }

    private
    static File generateCMakeListsFile(Project project, String originCMakeListsPath) {
        def outputDir = new File(project.buildDir, "/cipher.so/cmake")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        def targetFile = new File(outputDir, "CMakeLists.txt")
        def writer = new FileWriter(targetFile)
        new CMakeListsBuilder("${project.buildDir.canonicalPath}/cipher.so/CMakeLists.txt").setOriginCMakePath(originCMakeListsPath).build().each {
            writer.append(it)
        }
        writer.flush()
        writer.close()
        targetFile
    }
}