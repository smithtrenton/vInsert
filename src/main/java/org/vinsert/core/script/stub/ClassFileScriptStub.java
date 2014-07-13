package org.vinsert.core.script.stub;

import java.lang.annotation.Annotation;

/**
 * Class file script stub
 *
 * @author tomm
 */
public final class ClassFileScriptStub<T, M> implements AgnosticStub<T, M> {

    private Class<? extends T> classFile;
    private Class<? extends Annotation> manifestClass;

    private ClassFileScriptStub(Class<? extends T> classFile, Class<? extends Annotation> manifestClass) {
        this.classFile = classFile;
        this.manifestClass = manifestClass;
    }

    /**
     * Static builder method
     * <p/>
     * Note it is simple for ease of use since the constructor requires type parameters as well as parameters
     *
     * @param classFile
     * @param manifestClass
     * @param <T>
     * @param <M>
     * @return
     */
    public static <T, M> ClassFileScriptStub<T, M> create(Class<? extends T> classFile, Class<M> manifestClass) {
        return new ClassFileScriptStub<T, M>(classFile, (Class<? extends Annotation>) manifestClass);
    }

    @Override
    public T instantiate() throws InstantiationException, IllegalAccessException {
        return (T) classFile.newInstance();
    }

    @Override
    public M manifest() {
        return (M) classFile.getAnnotation(manifestClass);
    }

    public Class<? extends T> getClassFile() {
        return classFile;
    }

}
