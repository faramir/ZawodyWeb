package pl.umk.mat.zawodyweb.www.zip;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
/**
 *
 * @author faramir
 */
public class XmlDescriptor {

    private static final Namespace xmlns = Namespace.getNamespace("urn:proactive:deployment:3.3");
    private static final Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
    private final Element root;
    private final Document document;
    private Element variables;
    private Element jvmSet;
    private Element jvms;
    private Element processes;

    public XmlDescriptor() {
        document = new Document();
        root = new Element("ProActiveDescriptor", xmlns);
        root.setNamespace(xmlns);
        //root.addNamespaceDeclaration(xmlns);
        root.addNamespaceDeclaration(xsi);
        root.setAttribute("schemaLocation", "urn:proactive:deployment:3.3 http://www-sop.inria.fr/oasis/ProActive/schemas/deployment/3.3/deployment.xsd", xsi);
        document.setRootElement(root);
    }

    public Document getDocument() {
        return document;
    }

    public void addVariable(String name, String value) {
        if (variables == null) {
            variables = new Element("variables", xmlns);
            root.addContent(variables);
        }
        Element variable = new Element("descriptorVariable", xmlns);
        variable.setAttribute("name", name);
        variable.setAttribute("value", value);
        variables.addContent(variable);
    }

    public void addComponentDefinition(int minNodeNumber) {
        Element componentDefinition = new Element("componentDefinition", xmlns);
        Element virtualNodesDefinition = new Element("virtualNodesDefinition", xmlns);
        Element virtualNode = new Element("virtualNode", xmlns);

        virtualNode.setAttribute("name", "VirtualNode");
        virtualNode.setAttribute("property", "multiple");
        virtualNode.setAttribute("timeout", "360000");
        virtualNode.setAttribute("waitForTimeout", "false");
        virtualNode.setAttribute("minNodeNumber", String.valueOf(minNodeNumber));

        virtualNodesDefinition.addContent(virtualNode);
        componentDefinition.addContent(virtualNodesDefinition);
        root.addContent(componentDefinition);
    }

    public void addVirtualMachine(String name, String host, int askedNodes, String[] jvmParams, String[] classpaths, String java, String policy, String log4j, String username) {
        if (host == null) {
            host = "";
        }
        if (jvmParams == null) {
            jvmParams = new String[0];
        }
        if (classpaths == null) {
            classpaths = new String[0];
        }
        if (policy == null) {
            policy = "";
        }
        if (log4j == null) {
            log4j = "";
        }
        if (username == null) {
            username = "";
        }

        if (jvmSet == null) {
            Element deployment = new Element("deployment", xmlns);
            root.addContent(deployment);

            Element mapping = new Element("mapping", xmlns);
            deployment.addContent(mapping);

            Element map = new Element("map", xmlns);
            mapping.addContent(map);

            map.setAttribute("virtualNode", "VirtualNode");

            jvmSet = new Element("jvmSet", xmlns);
            map.addContent(jvmSet);

            // ----

            jvms = new Element("jvms", xmlns);
            deployment.addContent(jvms);

            // ----

            Element infrastructure = new Element("infrastructure", xmlns);
            root.addContent(infrastructure);

            processes = new Element("processes", xmlns);
            infrastructure.addContent(processes);
        }

        Element vmName = new Element("vmName", xmlns);
        vmName.setAttribute("value", name);
        jvmSet.addContent(vmName);

        Element jvm = new Element("jvm", xmlns);
        jvm.setAttribute("name", name);
        jvm.setAttribute("askedNodes", String.valueOf(askedNodes));
        jvms.addContent(jvm);

        Element creation = new Element("creation", xmlns);
        jvm.addContent(creation);

        Element processReference = new Element("processReference", xmlns);
        if (host.isEmpty()) {
            processReference.setAttribute("refid", "local_" + name);
        } else {
            processReference.setAttribute("refid", "ssh_" + name);
        }
        creation.addContent(processReference);

        // ---
        Element jvmParameters = new Element("jvmParameters", xmlns);
        for (String param : jvmParams) {
            Element parameter = new Element("parameter", xmlns);
            parameter.setAttribute("value", param);
            jvmParameters.addContent(parameter);
        }
        // ---

        Element processDefinition = new Element("processDefinition", xmlns);
        processes.addContent(processDefinition);
        if (host.isEmpty()) {
            processDefinition.setAttribute("id", "local_" + name);

            Element jvmProcess = new Element("jvmProcess", xmlns);
            jvmProcess.setAttribute("class", "org.objectweb.proactive.core.process.JVMNodeProcess");
            jvmProcess.addContent(jvmParameters);

            processDefinition.addContent(jvmProcess);
        } else {
            processDefinition.setAttribute("id", "ssh_" + name);

            Element sshProcess = new Element("sshProcess", xmlns);
            sshProcess.setAttribute("class", "org.objectweb.proactive.core.process.ssh.SSHProcess");
            sshProcess.setAttribute("hostname", host);
            sshProcess.setAttribute("username", username);
            processDefinition.addContent(sshProcess);

            Element processSshReference = new Element("processReference", xmlns);
            processSshReference.setAttribute("refid", "jvm_" + name);
            sshProcess.addContent(processSshReference);

            Element processSshDefinition = new Element("processDefinition", xmlns);
            processSshDefinition.setAttribute("id", "jvm_" + name);
            processes.addContent(processSshDefinition);

            Element jvmProcess = new Element("jvmProcess", xmlns);
            jvmProcess.setAttribute("class", "org.objectweb.proactive.core.process.JVMNodeProcess");

            Element parameter = new Element("parameter", xmlns);
            parameter.setAttribute("value", "-Dproactive.hostname=" + host);
            jvmParameters.addContent(parameter);

            jvmProcess.addContent(jvmParameters);
            processSshDefinition.addContent(jvmProcess);

            Element classpath = new Element("classpath", xmlns);
            jvmProcess.addContent(classpath);
            for (String cp : classpaths) {
                Element absolutePath = new Element("absolutePath", xmlns);
                absolutePath.setAttribute("value", cp);
                classpath.addContent(absolutePath);
            }

            Element javaPath = new Element("javaPath", xmlns);
            jvmProcess.addContent(javaPath);
            {
                Element absolutePath = new Element("absolutePath", xmlns);
                absolutePath.setAttribute("value", java);
                javaPath.addContent(absolutePath);
            }

            Element policyFile = new Element("policyFile", xmlns);
            jvmProcess.addContent(policyFile);
            {
                Element absolutePath = new Element("absolutePath", xmlns);
                absolutePath.setAttribute("value", policy);
                policyFile.addContent(absolutePath);
            }

            Element log4jFile = new Element("log4jpropertiesFile", xmlns);
            jvmProcess.addContent(log4jFile);
            {
                Element absolutePath = new Element("absolutePath", xmlns);
                absolutePath.setAttribute("value", log4j);
                log4jFile.addContent(absolutePath);
            }
        }
    }

    public byte[] generateXml(Format format) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            XMLOutputter output = new XMLOutputter();
            output.setFormat(format);
            output.output(document, baos);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return baos.toByteArray();
    }

    public byte[] generateXml() {
        return generateXml(Format.getPrettyFormat());
    }

    public void generateXml(Format format, OutputStream os) throws IOException {
        XMLOutputter output = new XMLOutputter();
        output.setFormat(format);
        output.output(document, os);
    }

    public void generateXml(OutputStream os) throws IOException {
        generateXml(Format.getPrettyFormat(), os);
    }

    public void generateXml(Format format, Writer writer) throws IOException {
        XMLOutputter output = new XMLOutputter();
        output.setFormat(format);
        output.output(document, writer);

    }

    public void generateXml(Writer writer) throws IOException {
        generateXml(Format.getPrettyFormat(), writer);
    }

    @Override
    public String toString() {
        return new String(generateXml());
    }

}
