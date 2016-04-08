package exosoft.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class XML {
	/*
	 * static Map<String, Object> parseXMLElement(String path, String tagName,
	 * String IDTag, String elementID) { Map<String, Object> XMLData = new
	 * HashMap<>(); try { Document doc =
	 * DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new
	 * File(path)).; doc.getDocumentElement().normalize(); NodeList nList =
	 * doc.getElementsByTagName(tagName); for (int temp = 0; temp <
	 * nList.getLength(); temp++) { Node nNode = nList.item(temp); if
	 * (nNode.getNodeType() == Node.ELEMENT_NODE) { Element eElement = (Element)
	 * nNode; NamedNodeMap attributes = eElement.getAttributes(); boolean
	 * correctItem = false; for (int index = 0; index < attributes.getLength();
	 * index++) { if (!correctItem) { correctItem =
	 * (attributes.item(index).getNodeName().equals(IDTag) &&
	 * attributes.item(index).getTextContent().equals(elementID)); }
	 * XMLData.put(attributes.item(index).getNodeName(),
	 * attributes.item(index).getTextContent().trim()); } if (correctItem) { if
	 * (eElement.hasChildNodes()) { NodeList nodeElements =
	 * eElement.getChildNodes(); for (int eIndex = 0; eIndex <
	 * nodeElements.getLength() - 1; eIndex++) {
	 * XMLData.put(nodeElements.item(eIndex).getNodeName().trim(),
	 * nodeElements.item(eIndex).getTextContent().trim()); } return XMLData; } }
	 * } else {
	 * System.err.println("You didn't give the parser the right info you idiot!"
	 * ); } } } catch (Exception e) { e.printStackTrace(); } return null; }
	 */
	private String label;
	Node[] nodes;
	private String path;
	private File file;

	XML(File file) {
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db;
	    Document xmlFile = null;
		try {
			db = dbf.newDocumentBuilder();
			xmlFile = db.parse(file);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    xmlFile.getDocumentElement().normalize();
	    NodeList nList = xmlFile.getElementsByTagName("");
	    nodes = new Node[nList.getLength()];
	    for (int i = 0; i < nList.getLength(); i++) {
	    	nodes[i] = new Node();
	    }
	}

	XML(String path) {
		this(new File(path));
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	class Attribute {
		private String tag;
		private Object content;

		Attribute(String tag, Object content) {
			this.setTag(tag);
			this.setContent(content);
		}

		public Object getContent() {
			return content;
		}

		public void setContent(Object content) {
			this.content = content;
		}

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}
	}

	class Element {
		private String label;
		private Object content;

		Element(String label, Object content) {
			this.setLabel(label);
			this.setContent(content);
		}

		public Object getContent() {
			return content;
		}

		public void setContent(Object content) {
			this.content = content;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}
	}

	class Node {
		private String label;
		List<Element> elements;
		List<Attribute> attributes;

		void addAttribute(Attribute a) {
			attributes.add(a);
		}
		
		void addElement(Element e) {
			elements.add(e);
		}

		public String getLabel() {
			return label;
		}
	}
}