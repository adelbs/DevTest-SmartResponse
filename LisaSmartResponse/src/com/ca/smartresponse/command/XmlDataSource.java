package com.ca.smartresponse.command;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlDataSource {

	public static void main(String[] args) throws Exception {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><list><list><string>99887805</string><string>1868736645</string><string>2017-08-12</string><string>800</string></list><list><string>99887806</string><string>1868736645</string><string>2017-08-12</string><string>112.12</string></list><list><string>99887807</string><string>1868736645</string><string>2017-08-12</string><string>100</string></list></list>";
		XmlDataSource ds = new XmlDataSource(xml, "tid", "cid", "data", "valor");
		
		while (ds.next()) {
			System.out.println("=========================");
			System.out.println("cid = " + ds.get("tid"));
			System.out.println("tid = " + ds.get("cid"));
			System.out.println("data = " + ds.get("data"));
			System.out.println("valor_cancelado = " + ds.get("valor"));
		}
	}

	private Hashtable<String, Integer> colNameIndex = new Hashtable<String, Integer>();
	private ArrayList<Object> listData = new ArrayList<Object>();
	private int cursor = -1;
	public XmlDataSource(String xmlData, String... colNames) throws Exception {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
		    InputSource is = new InputSource(new StringReader(xmlData));
			Document document = builder.parse(is);
			
			NodeList nodeList = document.getDocumentElement().getChildNodes();
			addResultList(nodeList, listData);
			
			for (int i = 0; i < colNames.length; i++)
				colNameIndex.put(colNames[i], i);
		}
		catch (Exception x) {
			x.printStackTrace();
			throw x;
		}
	}

	@SuppressWarnings("unchecked")
	private void addResultList(NodeList nodeList, ArrayList<Object> list) {
		Node node;
		for (int i = 0; i < nodeList.getLength(); i++) {
			node = nodeList.item(i);
			
			if ("list".equalsIgnoreCase(node.getNodeName())) {
				list.add(new ArrayList<Object>());
				addResultList(node.getChildNodes(), (ArrayList<Object>) list.get(list.size() - 1));
			}
			else if ("string".equalsIgnoreCase(node.getNodeName())) {
				list.add(node.getTextContent());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public String get(String colName) {
		Integer colIndex = colNameIndex.get(colName);
		String result = null;
		if (colIndex != null)
			result = ((ArrayList<Object>) listData.get(cursor)).get(colIndex).toString();
			
		return result;
	}
	
	public boolean next() {
		cursor++;
		return (listData.size() > cursor);
	}
}
