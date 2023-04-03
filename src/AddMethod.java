import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiExtensibleClass;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class AddMethod extends AnAction {

    private <T extends PsiElement> Collection<T> filterPsiElements(@NotNull PsiClass psiClass, @NotNull Class<T> desiredClass) {
        return Arrays.stream(psiClass.getChildren()).filter(desiredClass::isInstance).map(desiredClass::cast).collect(Collectors.toList());
    }

    public Collection<PsiField> collectClassFieldsIntern(@NotNull PsiClass psiClass) {
        if (psiClass instanceof PsiExtensibleClass) {
            return ((PsiExtensibleClass) psiClass).getOwnFields();
        } else {
            return filterPsiElements(psiClass, PsiField.class);
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiElement psiFile = e.getData(LangDataKeys.PSI_ELEMENT);
        PsiClass clazz = (PsiClass) psiFile;
        Collection<PsiField> psiFields = collectClassFieldsIntern(clazz);
        PsiField[] fields = clazz.getFields();
        PsiElementFactory elementFactory = JavaPsiFacade.getInstance(e.getProject()).getElementFactory();
        for (PsiField psiField : fields) {
            PsiManager manager = psiField.getManager();
            String fieldName = psiField.getName();
            PsiAnnotation[] annotations = psiField.getAnnotations();
            for (PsiAnnotation annotation : annotations) {
                String annoName = annotation.getQualifiedName();
                if ("MyAnnotation".equals(annoName)) {
                    String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
                    String getterName = "get" + methodName;
                    String methodText = String.format("public %s %s() { return this.%s; }", psiField.getType().getCanonicalText(), getterName, fieldName);
                    PsiMethod methodFromText = elementFactory.createMethodFromText(methodText, psiField.getContainingClass());
                    WriteCommandAction.runWriteCommandAction(clazz.getProject(), new Runnable() {
                        @Override
                        public void run() {
                            clazz.addAfter(methodFromText,psiField);
                        }
                    });
//                    CommandProcessor.getInstance().executeCommand(clazz.getProject(), () -> clazz.addAfter(psiField,methodFromText),"",null);
                }
            }
            System.out.println("");
        }
    }
}
