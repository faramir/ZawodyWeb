/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.pdf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import pl.umk.mat.zawodyweb.database.PDFDAO;

/**
 *
 * @author faramir
 */
public class PdfToImg {

    public static BufferedImage process(InputStream pdfFile) {
        PDDocument pdf = null;
        BufferedImage output = null;
        try {
            pdf = PDDocument.load(pdfFile, true);
            if (pdf.isEncrypted()) {
                pdf.decrypt("");
            }

            List<PDPage> pdfPages = pdf.getDocumentCatalog().getAllPages();
            if (pdfPages.isEmpty() == false) {
                Iterator<PDPage> it = pdfPages.iterator();
                PDPage page = it.next();

                BufferedImage bi = page.convertToImage(BufferedImage.TYPE_USHORT_565_RGB, 72 * 2);
                if (pdfPages.size() == 1) {
                    output = bi;
                } else {
                    int width = bi.getWidth();
                    int height = bi.getHeight();

                    output = new BufferedImage(width, height * pdfPages.size(),
                            BufferedImage.TYPE_USHORT_565_RGB);

                    Graphics2D g = output.createGraphics();
                    g.drawImage(bi, 0, 0, null);
                    g.setColor(Color.red);

                    int pageNo = 0;
                    while (it.hasNext()) {
                        ++pageNo;

                        page = it.next();
                        bi = page.convertToImage(BufferedImage.TYPE_USHORT_565_RGB, 72 * 2);

                        g.drawImage(bi, 0, pageNo * height, null);
                        g.drawLine(0, pageNo * height, width, pageNo * height);
                    }
                    g.dispose();
                }
            }
        } catch (Exception ex) {
            // TODO: log
        } finally {
            if (pdf != null) {
                try {
                    pdf.close();
                } catch (IOException ex) {
                    // TODO: log
                }
            }
        }
        return output;
    }

    public static void main(String[] args) throws IOException {
        for (String fn : new String[]{"c_comm", "lu", "pcj-ispa_12", "pcj-para_12", "pcj-apmm_12"}) {
            System.out.println("Processing file: " + fn);
            try {
                long time = System.currentTimeMillis();
                InputStream is = new BufferedInputStream(new FileInputStream("C:\\Users\\faramir\\Desktop\\" + fn + ".pdf"));
                BufferedImage output = PdfToImg.process(is);
                ImageIO.write(output, "jpg", new File("C:\\Users\\faramir\\Desktop\\" + fn + ".jpg"));
                System.out.println("finished after: " + (System.currentTimeMillis() - time) / 10e3 + "s");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
