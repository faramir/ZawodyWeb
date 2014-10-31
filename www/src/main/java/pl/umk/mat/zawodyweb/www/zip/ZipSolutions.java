/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.www.zip;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import pl.umk.mat.zawodyweb.database.pojo.Submits;

/**
 *
 * @author faramir
 */
public class ZipSolutions {

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private byte[] data;
    private ZipOutputStream out;

    public ZipSolutions() {
        out = new ZipOutputStream(baos);
        out.setMethod(ZipOutputStream.DEFLATED);
        out.setLevel(java.util.zip.Deflater.BEST_COMPRESSION);
    }

    public void addSubmit(Submits s) throws IOException {
        if (s == null) {
            return;
        }

        String filename = s.getProblems().getAbbrev() + "_"
                + s.getUsers().getLogin()
                + "." + s.getLanguages().getExtension();
        ZipEntry entry = new ZipEntry(filename);
        out.putNextEntry(entry);
        out.write(s.getCode());
        out.closeEntry();
    }

    public byte[] finish() throws IOException, JAXBException {
        out.close();

        data = baos.toByteArray();
        baos = null;

        return data;
    }

    public byte[] getData() {
        return data;
    }
}
