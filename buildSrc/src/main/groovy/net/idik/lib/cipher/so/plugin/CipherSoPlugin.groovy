package net.idik.lib.cipher.so.plugin

import com.android.build.gradle.AppExtension
import net.idik.lib.cipher.so.extension.CipherExt
import net.idik.lib.cipher.so.task.GenerateCipherSoHeaderTask
import net.idik.lib.cipher.so.task.GenerateJavaClientFileTask
import net.idik.lib.cipher.so.utils.IOUtils
import net.idik.lib.cipher.so.utils.StringUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.project.DefaultProject
import org.gradle.api.tasks.Copy

class CipherSoPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.extensions.add("cipher", new CipherExt(project))

        createTasks(project)

        setupProjectNativeSupport(project)

    }

    static def createTasks(Project project) {
        project.afterEvaluate {

            def android = project.extensions.findByType(AppExtension)

            def copyCppTask = project.tasks.create("copyCpp", Copy) {
                group "cipher.so"
                from project.rootProject.subprojects.find {
                    it.name == "devso"
                }.file("src/main/cpp")
                exclude "include/extern-keys.h"
                into new File(project.buildDir, "cipher.so/src/main/cpp")
            }
            def copyCMakeListsTask = project.tasks.create("copyCMakeList", Copy) {
                group "cipher.so"
                from project.rootProject.subprojects.find {
                    it.name == "devso"
                }.file("CMakeLists.txt").toPath()
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
                    it.dependsOn copyCppTask
                    it.dependsOn copyCMakeListsTask
                    it.dependsOn generateCipherSoExternTask
                }
                def generateJavaClientTask = project.tasks.create("generate${StringUtils.capitalize(variant.name)}JavaClient", GenerateJavaClientFileTask)
                def outputDir = new File("${project.buildDir}/generated/source/cipher.so/${variant.name}")
                generateJavaClientTask.configure {
                    it.keyExts = keyExts
                    println(it.keyExts)
                    it.outputDir = outputDir
                }
                variant.registerJavaGeneratingTask(generateJavaClientTask, outputDir)

            }

        }
    }


    static def setupProjectNativeSupport(Project project) {
        if (project instanceof DefaultProject) {
            ((DefaultProject) project).projectEvaluationBroadcaster
        }
        project.afterEvaluate {
            project.copy {
                from project.rootProject.subprojects.find {
                    it.name == "devso"
                }.file("src/main/cpp")
                exclude "include/extern-keys.h"
                into new File(project.buildDir, "cipher.so/src/main/cpp")
            }
            project.copy {
                from project.rootProject.subprojects.find {
                    it.name == "devso"
                }.file("CMakeLists.txt").toPath()
                into new File(project.buildDir, "cipher.so")
            }
            def android = project.extensions.findByType(AppExtension)
            android.defaultConfig.externalNativeBuild {
                cmake {
                    String currentFlags = cppFlags ?: ""
                    cppFlags currentFlags
                }
            }
            android.externalNativeBuild {
                cmake {
                    path "${project.buildDir.path}/cipher.so/CMakeLists.txt"
                }
            }
        }
    }
}