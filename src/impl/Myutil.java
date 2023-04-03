package impl;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiExtensibleClass;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class Myutil {

    private static <T extends PsiElement> Collection<T> filterPsiElements(@NotNull PsiClass psiClass, @NotNull Class<T> desiredClass) {
        return Arrays.stream(psiClass.getChildren()).filter(desiredClass::isInstance).map(desiredClass::cast).collect(Collectors.toList());
    }

    public static Collection<PsiField> collectClassFieldsIntern(@NotNull PsiClass psiClass) {
        if (psiClass instanceof PsiExtensibleClass) {
            return ((PsiExtensibleClass) psiClass).getOwnFields();
        } else {
            return filterPsiElements(psiClass, PsiField.class);
        }
    }

    public static PsiCodeBlock createCodeBlockFromText(String blockText, PsiElement psiElement){
        final PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiElement.getProject());
        return elementFactory.createCodeBlockFromText("{" + blockText + "}", psiElement);
    }
}
