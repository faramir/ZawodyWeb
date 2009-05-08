
package pl.umk.mat.zawodyweb.judge;

/**
 *
 * @author lukash2k
 */
public class CompiledClassLoader extends ClassLoader{

    public Class<?> loadCompiledClass(String name, byte[] code) {
        Class<?> result = this.defineClass(name, code, 0, code.length);
        this.resolveClass(result);
        return result;
    }

}
