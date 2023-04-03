package impl;

import com.intellij.psi.*;
import com.intellij.psi.augment.PsiAugmentProvider;
import com.intellij.psi.impl.source.PsiExtensibleClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import processor.ClassProcessor;
import processor.FieldGetterProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PsiAugmentProviderImpl extends PsiAugmentProvider {

    public PsiAugmentProviderImpl() {
        super();
    }

    @NotNull
    @Override
    protected <Psi extends PsiElement> List<Psi> getAugments(@NotNull PsiElement element, @NotNull Class<Psi> type, @Nullable String nameHint) {
//        System.out.println("进入到PsiAugmentProviderImpl  getAugments");
        final List<Psi> emptyResult = Collections.emptyList();
        final List<Psi> result = new ArrayList<>();
        if ((type != PsiClass.class && type != PsiField.class && type != PsiMethod.class) || !(element instanceof PsiExtensibleClass)) {
            return emptyResult;
        }
        PsiClass psiClass = (PsiClass) element;
        // 可以通过psiClass.getModifierList()获取字段修饰
        boolean isMethod = type.isAssignableFrom(PsiMethod.class);
        if (isMethod){
            new FieldGetterProcessor().process(result,psiClass,type);
            new ClassProcessor().process(result,psiClass,type);
        }
        return result;
    }

    @Nullable
    @Override
    protected PsiType inferType(@NotNull PsiTypeElement typeElement) {
        return super.inferType(typeElement);
    }

    @Override
    protected boolean canInferType(@NotNull PsiTypeElement typeElement) {
        return super.canInferType(typeElement);
    }

    @NotNull
    @Override
    protected Set<String> transformModifiers(@NotNull PsiModifierList modifierList, @NotNull Set<String> modifiers) {
        return super.transformModifiers(modifierList, modifiers);
    }
}
