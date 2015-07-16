/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.pdf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * @author faramir
 */
public class PdfToImage {

    public static BufferedImage process(InputStream pdfFile) {
        BufferedImage output = null;
        try (PDDocument pdfDocument = PDDocument.load(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(pdfDocument);

            int pageCount = pdfDocument.getNumberOfPages();

            if (pageCount > 0) {
                int width = 0;
                int height = -1;

                BufferedImage[] pdfPages = new BufferedImage[pageCount];
                for (int pageNo = 0; pageNo < pageCount; ++pageNo) {
                    pdfPages[pageNo] = renderer.renderImageWithDPI(pageNo, 200.0f, ImageType.RGB);
                    if (pdfPages[pageNo].getWidth() > width) {
                        width = pdfPages[pageNo].getWidth();
                    }
                    height += pdfPages[pageNo].getHeight() + 1;
                }

                output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                Graphics2D g = output.createGraphics();
                g.setColor(Color.red);
                int currentHeight = 0;
                for (int pageNo = 0; pageNo < pageCount; ++pageNo) {
                    if (pageNo > 0) {
                        g.drawLine(0, currentHeight, width, currentHeight);
                        ++currentHeight;
                    }
                    g.drawImage(pdfPages[pageNo],
                            (width - pdfPages[pageNo].getWidth()) / 2,
                            currentHeight,
                            null);
                    currentHeight += pdfPages[pageNo].getHeight();
                }
                g.dispose();
            }
        } catch (Exception ex) {
            throw new RuntimeException("Exception converting pdf to image: ", ex);
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
