class XML {
	/* static Map<String, Object> parseXMLElement(String path, String tagName, String IDTag, String elementID) {
	    Map<String, Object> XMLData = new HashMap<>();
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path)).;
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName(tagName);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					NamedNodeMap attributes = eElement.getAttributes();
					boolean correctItem = false;
					for (int index = 0; index < attributes.getLength(); index++) {
						if (!correctItem) {
							correctItem = (attributes.item(index).getNodeName().equals(IDTag)
									&& attributes.item(index).getTextContent().equals(elementID));
						}
						XMLData.put(attributes.item(index).getNodeName(),
								attributes.item(index).getTextContent().trim());
					}
					if (correctItem) {
						if (eElement.hasChildNodes()) {
							NodeList nodeElements = eElement.getChildNodes();
							for (int eIndex = 0; eIndex < nodeElements.getLength() - 1; eIndex++) {
								XMLData.put(nodeElements.item(eIndex).getNodeName().trim(),
										nodeElements.item(eIndex).getTextContent().trim());
							}
							return XMLData;
						}
					}
				} else {
					System.err.println("You didn't give the parser the right info you idiot!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	} */
	private String label;
	Node[] nodes;
	private String path;
	private File file;
	
	
	XML(File file) {
	    DocumentBuilderFactory dbf = new DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document xmlFile = db.parse(file);
	    doc.getDocumentElement().normalize();
	    NodeList nList = doc.getElementsByTagName(tagName);
	    elements = new Element[nList.getLength()];
	}
	
	XML(String path) {
	    this(new File(path));
	}
	
	class Attribute {
	    private String tag;
	    private Object content;
	}
	
	class Element {
	    private String label;
	    private Object content;
	}
	
	class Node {
	    private String label;
	    Element[] elements;
	    Attribute[] attributes;
	}
	    
	    
}