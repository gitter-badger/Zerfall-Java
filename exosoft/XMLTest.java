import java.io.File;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

import com.piyush.STDMP;

public class XMLTest {
    List<Object> parseXML(String xmlFile) {       
        try {
            DOMParser parser = new DOMParser();
            parser.parse("mydocument.xml");
            Document doc = parser.getDocument();
 
            // Get the document's root XML node
            NodeList root = doc.getChildNodes();
 
            // Navigate down the hierarchy to get to the CEO node
            Node comp = getNode("Company", root);
            Node exec = getNode("Executive", comp.getChildNodes() );
            String execType = getNodeAttr("type", exec);
 
            // Load the executive's data from the XML
            NodeList nodes = exec.getChildNodes();
            String lastName = getNodeValue("LastName", nodes);
            String firstName = getNodeValue("FirstName", nodes);
            String street = getNodeValue("street", nodes);
            String city = getNodeValue("city", nodes);
            String state = getNodeValue("state", nodes);
            String zip = getNodeValue("zip", nodes);
 
            System.out.println("Executive Information:");
            System.out.println("Type: " + execType);
            System.out.println(lastName + ", " + firstName);
            System.out.println(street);
            System.out.println(city + ", " + state + " " + zip);
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}