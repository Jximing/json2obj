package com.github.jximing.json2obj.services

import com.intellij.openapi.project.Project
import com.github.jximing.json2obj.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
