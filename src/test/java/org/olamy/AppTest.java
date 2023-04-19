package org.olamy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    private static int request;

    Server server = new Server(0);

    @Before
    public void startJetty() throws Exception
    {
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        context.addServlet(MyServlet.class, "/*");
        server.setHandler(context);
        server.start();

    }

    public static class MyServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            request++;
            super.doGet(req, resp);
        }
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws Exception
    {
        int port = ((ServerConnector)server.getConnectors()[0]).getLocalPort();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n" +
                "    <!DOCTYPE root:propfind [\n" +
                "    <!ENTITY % curl SYSTEM 'http://127.0.0.1:" + port + "'>\n" +
                "    %curl;\n" +
                "    ]>\n" +
                "    <root:propfind xmlns:root=\"DAV:\" >\n" +
                "        <t1:prop xmlns:t1=\"DAV:\">  <t1:tt xmlns:t1=\"123\">123</t1:tt></t1:prop>\n" +
                "    </root:propfind>";


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Path tmp = Files.createTempFile("tmp", "xml");
        Files.write(tmp, xml.getBytes(StandardCharsets.UTF_8));

        try {
            Document document = documentBuilder.parse(tmp.toFile());
        } catch (SAXException|IOException e) {
            // ignore here we just want to validate there were no http request
        }

        assertEquals("Http listener has received a request:", 0, request);
    }
}
