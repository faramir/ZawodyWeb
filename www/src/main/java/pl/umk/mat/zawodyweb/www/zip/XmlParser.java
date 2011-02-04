/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.www.zip;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author faramir
 */
public class XmlParser {

    private JAXBContext jc;

    public XmlParser() {
        try {
            jc = JAXBContext.newInstance("pl.umk.mat.zawodyweb.database.xml");
        } catch (JAXBException e) {
            throw new RuntimeException("XmlParser initialized incorrectly", e);
        }
    }

    public Object parseString(String src) throws JAXBException {
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return unmarshaller.unmarshal(new StringReader(src));
    }

    public String toString(Object obj) throws JAXBException {
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter sw = new StringWriter();
        marshaller.marshal(obj, sw);
        return sw.toString();
    }
}
