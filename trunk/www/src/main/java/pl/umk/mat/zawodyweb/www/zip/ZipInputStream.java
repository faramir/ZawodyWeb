/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.www.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;

/**
 *
 * @author faramir
 */
public class ZipInputStream extends java.util.zip.ZipInputStream {

    private static final int BUFFER = 2048;
    private Map<String, byte[]> entries;

    public ZipInputStream(byte[] bytes) throws IOException {
        this(new ByteArrayInputStream(bytes));
    }

    public ZipInputStream(InputStream in) throws IOException {
        super(in);

        fillEntries();
    }

    public ZipInputStream(InputStream in, Charset charset) throws IOException {
        super(in, charset);

        fillEntries();
    }

    private void fillEntries() throws IOException {
        entries = new HashMap<String, byte[]>();

        ZipEntry entry;

        while ((entry = this.getNextEntry()) != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] data = new byte[BUFFER];
            int count;
            while ((count = in.read(data, 0, BUFFER)) != -1) {
                baos.write(data, 0, count);
            }
            baos.flush();
            baos.close();

            entries.put(entry.getName(), baos.toByteArray());
        }
    }

    public Set<String> getFilenames() {
        return entries.keySet();
    }

    public boolean containsFile(String filename) {
        return entries.containsKey(filename);
    }
    
    public byte[] getFile(String filename) {
        return entries.get(filename);
    }
}
