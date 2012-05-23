/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.pdf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

/**
 *
 * @author faramir
 */
public class PdfToImage {

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
            throw new RuntimeException("Exception converting pdf to image: ", ex);
        } finally {
            if (pdf != null) {
                try {
                    pdf.close();
                } catch (IOException ex) {
                    throw new RuntimeException("Exception when closing pdf: ", ex);
                }
            }
        }
        return output;
    }

    public static byte[] convertPdf(byte[] pdf) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(pdf);
            BufferedImage bi = process(bais);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageWriter converter = ImageIO.getImageWritersByMIMEType("image/jpeg").next();

            IIOImage image = new IIOImage(bi, null, null);
            ImageOutputStream output = ImageIO.createImageOutputStream(baos);
            converter.setOutput(output);

            ImageWriteParam jpegParams = converter.getDefaultWriteParam();
            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(0.3f);

            converter.write(null, image, jpegParams);
            converter.dispose();

            return baos.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("Exception in converting pdf", ex);
        }
    }

//    public static void main(String[] args) throws IOException {
//        for (String fn : new String[]{"jb"}) {
//            System.out.println("Processing file: " + fn);
//            try {
//                long time = System.currentTimeMillis();
//                InputStream is = new BufferedInputStream(new FileInputStream("C:\\Users\\faramir\\Desktop\\" + fn + ".pdf"));
//                BufferedImage output = PdfToImage.process(is);
//                ImageIO.write(output, "jpg", new File("C:\\Users\\faramir\\Desktop\\" + fn + ".jpg"));
//                System.out.println("finished after: " + (System.currentTimeMillis() - time) / 10e3 + "s");
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
}
