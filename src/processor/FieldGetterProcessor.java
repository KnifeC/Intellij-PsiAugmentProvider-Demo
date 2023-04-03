package processor;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.util.Key;
import com.intellij.psi.*;
import com.intellij.psi.impl.light.LightMethodBuilder;
import impl.Myutil;

import java.util.Collection;
import java.util.List;

public class FieldGetterProcessor implements IProcessor {


    public <Psi extends PsiElement> void process(List<Psi> result, PsiClass psiClass, Class<Psi> type) {
        Collection<PsiField> fields = Myutil.collectClassFieldsIntern(psiClass);
        for (PsiField psiField : fields) {
            createGetter(result, psiField, type);
        }
    }

    static Key<PsiMethod> GETTER_KEY = Key.create("MyGetter");

    private <Psi extends PsiElement> void createGetter(List<Psi> result, PsiField psiField, Class<Psi> type) {
        PsiAnnotation[] annotations = psiField.getAnnotations();
        for (PsiAnnotation annotation : annotations) {
            String annoName = annotation.getQualifiedName();
            // 必须填完整包名
            if ("pkg1.MyAnnotation".equals(annoName)) {
                result.add((Psi) createGetterMethod(psiField));
            }
        }
    }


    public PsiMethod createGetterMethod(PsiField psiField){
        if (psiField.getUserData(GETTER_KEY) !=null) {
            PsiMethod userData = psiField.getUserData(GETTER_KEY);
            return userData;
        }
        String fieldName = psiField.getName();
        String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        String getterName = "get" + methodName;
        LightMethodBuilder methodBuilder = new LightMethodBuilder(psiField.getManager(), JavaLanguage.INSTANCE, getterName);
        methodBuilder.addModifier(PsiModifier.PUBLIC);
        methodBuilder.setMethodReturnType(psiField.getType());
        methodBuilder.setNavigationElement(psiField);
        methodBuilder.setContainingClass(psiField.getContainingClass());
        boolean isStatic = psiField.hasModifierProperty(PsiModifier.STATIC);
        if (isStatic) {
            methodBuilder.addModifier(PsiModifier.STATIC);
        }
        psiField.putUserData(GETTER_KEY,methodBuilder);
        return methodBuilder;
    }
}
