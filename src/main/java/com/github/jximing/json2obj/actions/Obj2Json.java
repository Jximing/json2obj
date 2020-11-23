package com.github.jximing.json2obj.actions;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
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

        if (directory == null) {
            return;
        }
        StringBuilder jsonStr = new StringBuilder("{");
        TypeDeclaration<?> type = StaticJavaParser.parse(psiFile.getText()).getType(0);
        String name = type.getName().getIdentifier() + ".json";
        type.getMembers().forEach(f -> {
            if (f instanceof FieldDeclaration) {
                VariableDeclarator field = ((FieldDeclaration) f).getVariable(0);
                String fieldName = field.getName().getIdentifier();
                jsonStr.append("\"").append(fieldName).append("\":");
                Type type1 = field.getType();
                if (type1 instanceof ClassOrInterfaceType && "List".equals(((ClassOrInterfaceType) type1).getName().getIdentifier())) {
                    jsonStr.append("[]");
                } else {
                    jsonStr.append("\"\"");
                }
                jsonStr.append(",");
            }
        });
        jsonStr.deleteCharAt(jsonStr.length() - 1);
        jsonStr.append("}");

        PsiFileFactory factory = PsiFileFactory.getInstance(project);
        PsiFile json = factory.createFileFromText(name, JsonFileType.INSTANCE, jsonStr);
        ApplicationManager.getApplication().runWriteAction(()->{
            PsiFile oldfile = directory.findFile(name);
            if (oldfile != null) {
                oldfile.delete();
            }
            directory.add(json);
        });
    }

}
