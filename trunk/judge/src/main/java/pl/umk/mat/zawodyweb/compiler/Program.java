package pl.umk.mat.zawodyweb.compiler;

/**
 *
 * @author lukash2k
 */
public class Program {

    private String path;
    private String params;

    public Program(String path, String params) {
        this.path = path;
        this.params = params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getParams() {
        return params;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
