package processor;

import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClassProcessor implements IProcessor {

    @NotNull
    private Collection<PsiField> filterSetterFields(@NotNull PsiClass psiClass) {
        FieldSetterProcessor fieldProcessor = new FieldSetterProcessor();
        final Collection<PsiField> setterFields = new ArrayList<>();
        for (PsiField psiField : psiClass.getFields()) {
            boolean createSetter = true;
            PsiModifierList modifierList = psiField.getModifierList();
            if (null != modifierList) {
                //1.Skip final fields.
                createSetter = !modifierList.hasModifierProperty(PsiModifier.FINAL);
                //2.Skip static fields.
                createSetter &= !modifierList.hasModifierProperty(PsiModifier.STATIC);
                //3.Skip fields having Setter annotation already
//                createSetter &= PsiAnnotationSearchUtil.isNotAnnotatedWith(psiField, fieldProcessor.getSupportedAnnotationClasses());
                //4.Skip fields that start with $
//                createSetter &= !psiField.getName().startsWith(LombokUtils.LOMBOK_INTERN_FIELD_MARKER);
                //5.Skip fields if a method with same name already exists
//                final Collection<String> methodNames = fieldProcessor.getAllSetterNames(psiField, PsiType.BOOLEAN.equals(psiField.getType()));
//                for (String methodName : methodNames) {
//                    createSetter &= !PsiMethodUtil.hasSimilarMethod(classMethods, methodName, 1);
//                }
            }
            if (createSetter) {
                setterFields.add(psiField);
            }
        }
        return setterFields;
    }

    @Override
    public <Psi extends PsiElement> void process(List<Psi> result, PsiClass psiClass, Class<Psi> type) {
//        Collection<PsiField> psiFields = filterSetterFields(psiClass);
        PsiField[] fields = psiClass.getFields();
        PsiAnnotation[] annotations = psiClass.getAnnotations();
        for (PsiAnnotation annotation : annotations) {
            if ("pkg1.ClassAnnotation".equals(annotation.getQualifiedName())) {
                for (PsiField psiField : fields) {
                    new FieldSetterProcessor().createSetter(result, psiField, type);
                }
            }
        }

    }
}
