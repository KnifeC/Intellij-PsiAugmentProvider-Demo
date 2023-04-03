package processor;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;

import java.util.List;

public interface IProcessor {

    <Psi extends PsiElement> void process(List<Psi> result, PsiClass psiClass, Class<Psi> type);

}
