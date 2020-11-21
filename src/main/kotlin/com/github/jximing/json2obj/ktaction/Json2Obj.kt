package com.github.jximing.json2obj.ktaction

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys

/**
 *
 * @author mingjiexian
 * @date 2020-11-20 11:29
 *
 */
class Json2Obj : AnAction() {

    override fun update(e: AnActionEvent) {
        val virtualFile = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        val show = virtualFile != null && virtualFile.name.contains(".json")
        e.presentation.isEnabledAndVisible = show
    }

    override fun actionPerformed(e: AnActionEvent) {
//        TODO("Not yet implemented")
        val data = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        println(data)

    }

}