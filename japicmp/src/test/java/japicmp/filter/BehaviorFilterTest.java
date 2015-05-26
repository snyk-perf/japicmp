package japicmp.filter;

import japicmp.exception.JApiCmpException;
import japicmp.util.CtClassBuilder;
import japicmp.util.CtConstructorBuilder;
import japicmp.util.CtMethodBuilder;
import javassist.*;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class BehaviorFilterTest {

    @Test
    public void testMethodTwoParamsIntLongSuccessful() throws CannotCompileException {
        BehaviorFilter filter = new BehaviorFilter("japicmp.Test#method(int,long)");
        CtClass ctClass = CtClassBuilder.create().name("japicmp.Test").addToClassPool(new ClassPool());
        CtBehavior ctBehavior = CtMethodBuilder.create().name("method").parameters(new CtClass[]{CtClass.intType, CtClass.longType}).addToClass(ctClass);
        assertThat(filter.matches(ctBehavior), is(true));
    }

    @Test
    public void testMethodOneParamsStringSuccessful() throws CannotCompileException {
        BehaviorFilter filter = new BehaviorFilter("japicmp.Test#method(japicmp.Param)");
        ClassPool classPool = new ClassPool();
        CtClass ctClass = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classPool);
        CtClass paramCtClass = CtClassBuilder.create().name("japicmp.Param").addToClassPool(classPool);
        CtBehavior ctBehavior = CtMethodBuilder.create().name("method").parameters(new CtClass[]{paramCtClass}).addToClass(ctClass);
        assertThat(filter.matches(ctBehavior), is(true));
    }

    @Test
    public void testMethodNoParamsSuccessful() throws CannotCompileException {
        BehaviorFilter filter = new BehaviorFilter("japicmp.Test#method()");
        CtClass ctClass = CtClassBuilder.create().name("japicmp.Test").addToClassPool(new ClassPool());
        CtBehavior ctBehavior = CtMethodBuilder.create().name("method").parameters(new CtClass[]{}).addToClass(ctClass);
        assertThat(filter.matches(ctBehavior), is(true));
    }

    @Test(expected = JApiCmpException.class)
    public void testMethodMissingParenthesis() throws CannotCompileException {
        BehaviorFilter filter = new BehaviorFilter("japicmp.Test#method(");
        CtClass ctClass = CtClassBuilder.create().name("japicmp.Test").addToClassPool(new ClassPool());
        CtBehavior ctBehavior = CtMethodBuilder.create().name("method").parameters(new CtClass[]{}).addToClass(ctClass);
        assertThat(filter.matches(ctBehavior), is(true));
    }

    @Test
    public void testConstructorNoParamsSuccessful() throws CannotCompileException {
        BehaviorFilter filter = new BehaviorFilter("japicmp.Test#Test()");
        ClassPool classPool = new ClassPool();
        classPool.appendSystemPath();
        CtClass ctClass = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classPool);
        CtConstructor ctConstructor = CtConstructorBuilder.create().addToClass(ctClass);
        assertThat(filter.matches(ctConstructor), is(true));
    }

    @Test
    public void testConstructorOneParamLongSuccessful() throws CannotCompileException, NotFoundException {
        BehaviorFilter filter = new BehaviorFilter("japicmp.Test#Test(java.lang.Long)");
        ClassPool classPool = new ClassPool();
        classPool.appendSystemPath();
        CtClass ctClass = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classPool);
        CtConstructor ctConstructor = CtConstructorBuilder.create().parameter(classPool.get("java.lang.Long")).addToClass(ctClass);
        assertThat(filter.matches(ctConstructor), is(true));
    }

    @Test
    public void testConstructorOneParamLongUnsuccessful() throws CannotCompileException, NotFoundException {
        BehaviorFilter filter = new BehaviorFilter("japicmp.Test#Test(java.lang.Long)");
        ClassPool classPool = new ClassPool();
        classPool.appendSystemPath();
        CtClass ctClass = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classPool);
        CtConstructor ctConstructor = CtConstructorBuilder.create().parameter(classPool.get("java.lang.Double")).addToClass(ctClass);
        assertThat(filter.matches(ctConstructor), is(false));
    }

    @Test
    public void testConstructorNoParamLongUnsuccessful() throws CannotCompileException, NotFoundException {
        BehaviorFilter filter = new BehaviorFilter("japicmp.Test#Test()");
        ClassPool classPool = new ClassPool();
        classPool.appendSystemPath();
        CtClass ctClass = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classPool);
        CtConstructor ctConstructor = CtConstructorBuilder.create().parameter(classPool.get("java.lang.Double")).addToClass(ctClass);
        assertThat(filter.matches(ctConstructor), is(false));
    }
}