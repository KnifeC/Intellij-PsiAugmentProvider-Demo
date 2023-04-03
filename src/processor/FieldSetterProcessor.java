package processor;

import com.intellij.psi.*;
import impl.Myutil;
import lombok.LombokLightMethodBuilder;

import java.util.Collection;
import java.util.List;

public class FieldSetterProcessor implements IProcessor {

    @Override
    public <Psi extends PsiElement> void process(List<Psi> result, PsiClass psiClass, Class<Psi> type) {
        Collection<PsiField> fields = Myutil.collectClassFieldsIntern(psiClass);
        for (PsiField psiField : fields) {
            createSetter(result, psiField, type);
        }

    }

    public <Psi extends PsiElement> void createSetter(List<Psi> result, PsiField psiField, Class<Psi> type) {
        result.add((Psi) createSetterMethod(psiField));
    }

    public PsiMethod createSetterMethod(PsiField psiField) {
        String fieldName = psiField.getName();
        String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
        String setterName = "set" + methodName;
//        final PsiAnnotation setterAnnotation = PsiAnnotationSearchUtil.findAnnotation(psiField, LombokClassNames.SETTER);
//        final String methodName = LombokUtils.getSetterName(psiField);

        PsiType returnType = PsiType.VOID;
        LombokLightMethodBuilder methodBuilder = new LombokLightMethodBuilder(psiField.getManager(), setterName)
                .withMethodReturnType(returnType)
                .withContainingClass(psiField.getContainingClass())
                .withParameter(fieldName, psiField.getType())
                .withNavigationElement(psiField);
//        if (StringUtil.isNotEmpty(methodModifier)) {
        methodBuilder.withModifier(PsiModifier.PUBLIC);
//        }
        boolean isStatic = psiField.hasModifierProperty(PsiModifier.STATIC);
        if (isStatic) {
            methodBuilder.withModifier(PsiModifier.STATIC);
        }
        PsiParameter methodParameter = methodBuilder.getParameterList().getParameters()[0];
        final String thisOrClass = isStatic ? psiField.getContainingClass().getName() : "this";
        String blockText = String.format("%s.%s = %s; ", thisOrClass, psiField.getName(), methodParameter.getName());

        String codeBlockText = blockText;
        if (!isStatic && !PsiType.VOID.equals(returnType)) {
            codeBlockText += "return this;";
        }
        PsiCodeBlock codeBlockFromText = Myutil.createCodeBlockFromText(codeBlockText, psiField);
        methodBuilder.withBody(codeBlockFromText);
        return methodBuilder;
    }

}
