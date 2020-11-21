package com.github.jximing.json2obj.actions;

import com.intellij.json.JsonFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author mingjiexian
 * @date 2020-11-20 10:51
 */
public class Obj2Json extends AnAction {

    @Override
    public void update(AnActionEvent e) {
        // Using the event, evaluate the context, and enable or disable the action.
        // Set the availability based on whether a project is open

//        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
//        boolean show = psiFile != null;
        VirtualFile virtualFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        boolean show = virtualFile != null && virtualFile.getFileType().getName().equals("JAVA");
        e.getPresentation().setEnabledAndVisible(show);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
//        IdeView view = e.getRequiredData(LangDataKeys.IDE_VIEW);

        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        assert psiFile != null;

        PsiDirectory directory = psiFile.getParent();

//        VirtualFile fixedDirectory = ProjectUtil.guessProjectDir(project);
//        PsiDirectory directory;
//        if (fixedDirectory != null) {
//            directory = PsiManager.getInstance(project).findDirectory(fixedDirectory);
//        } else {
//            directory = view.getOrChooseDirectory();
//        }
        if (directory == null) {
            return;
        }
        StringBuilder jsonStr = new StringBuilder();
        @NotNull PsiElement[] children = psiFile.getChildren();
        for (PsiElement child : children) {
            if("com.intellij.psi.impl.source.PsiClassImpl".equals(child.getClass().getName())){
                System.out.println(child.getClass());
                for (PsiElement childChild : child.getChildren()) {
                    if("com.intellij.psi.impl.source.PsiFieldImpl".equals(childChild.getClass().getName())){
                        jsonStr.append( childChild.getText());
                    }
                }
            }
        }
        PsiFileFactory factory = PsiFileFactory.getInstance(project);
        PsiFile json = factory.createFileFromText(psiFile.getName() + ".json", JsonFileType.INSTANCE, jsonStr);


        directory.add(json);
    }

}
