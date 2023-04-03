package impl;

import com.intellij.ide.structureView.StructureViewExtension;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.java.JavaClassTreeElement;
import com.intellij.ide.structureView.impl.java.PsiFieldTreeElement;
import com.intellij.ide.structureView.impl.java.PsiMethodTreeElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import lombok.LombokLightClassBuilder;
import lombok.LombokLightFieldBuilder;
import lombok.LombokLightMethodBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class StructureViewExtensionImpl implements StructureViewExtension {
    @Override
    public void filterChildren(@NotNull Collection<StructureViewTreeElement> baseChildren, @NotNull List<StructureViewTreeElement> extensionChildren) {

    }

    @Override
    public Class<? extends PsiElement> getType() {
        return PsiClass.class;
    }

    @Override
    public StructureViewTreeElement[] getChildren(PsiElement parent) {
        final PsiClass parentClass = (PsiClass) parent;

        final Stream<PsiFieldTreeElement> lombokFields = Arrays.stream(parentClass.getFields())
                .filter(LombokLightFieldBuilder.class::isInstance)
                .map(psiField -> new PsiFieldTreeElement(psiField, false));

        final Stream<PsiMethodTreeElement> lombokMethods = Arrays.stream(parentClass.getMethods())
                .filter(LombokLightMethodBuilder.class::isInstance)
                .map(psiMethod -> new PsiMethodTreeElement(psiMethod, false));

        final Stream<JavaClassTreeElement> lombokInnerClasses = Arrays.stream(parentClass.getInnerClasses())
                .filter(LombokLightClassBuilder.class::isInstance)
                .map(psiClass -> new JavaClassTreeElement(psiClass, false));

        return Stream.concat(Stream.concat(lombokFields, lombokMethods), lombokInnerClasses)
                .toArray(StructureViewTreeElement[]::new);
    }

    @Nullable
    @Override
    public Object getCurrentEditorElement(Editor editor, PsiElement psiElement) {
        return null;
    }
}
