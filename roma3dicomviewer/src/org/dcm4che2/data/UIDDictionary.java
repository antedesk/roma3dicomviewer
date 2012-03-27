package org.dcm4che2.data;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class UIDDictionary implements Serializable {

	private static final long serialVersionUID = 3258135738985296181L;

	private static final String RESOURCE_NAME =
			"org/dcm4che2/data/UIDDictionary.ser";

	private static final String FILE_NAME = "UIDDictionary.ser";

	private static final String USAGE =
			"Usage: mkuiddic <xml-file>...\n" +
					"         (Create UID Dictionary resource from XML files).\n";

	public static final String UNKOWN = "?";

	private static UIDDictionary inst;

	public static void main(String args[]) {
		if (args.length == 0) {
			System.out.println(USAGE);
			System.exit(1);
		}
		UIDDictionary dict = new UIDDictionary(250);
		try {
			for (int i = 0; i < args.length; i++) {
				dict.loadXML(new File(args[i]));
			}
			File ofile = new File(FILE_NAME);
			ResourceLocator.serializeTo(dict, ofile);
			System.out.println("Create Dictionary Resource - " + ofile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadDictionary() {
		UIDDictionary.inst = (UIDDictionary) ResourceLocator
				.loadResource(RESOURCE_NAME);
	}

	public static UIDDictionary getDictionary() {
		if (inst == null)
			loadDictionary();
		return inst;
	}

	private Hashtable<String, String> table;

	private UIDDictionary() {
		this(11);
	}

	private UIDDictionary(int initialCapacity) {
		this.table = new Hashtable<String, String>(initialCapacity);
	}

	public String prompt(String uid) {
		return uid + "/" + nameOf(uid);
	}

	public String nameOf(String uid) {
		if (table == null)
			return UNKOWN;
		String name = table.get(uid);
		return name != null ? name : UNKOWN;
	}

	public void loadXML(File f) throws IOException, SAXException {
		try {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(f, new SAXAdapter());
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (FactoryConfigurationError e) {
			throw new RuntimeException(e);
		}
	}

	private final class SAXAdapter extends DefaultHandler {
		String uid;

		StringBuffer name = new StringBuffer(80);

		@Override
		public void characters(char[] ch, int start, int length) {
			if (uid != null) {
				name.append(ch, start, length);
			}
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) {
			if ("uid".equals(qName)) {
				uid = attributes.getValue("value");
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) {
			if ("uid".equals(qName)) {
				table.put(uid, name.toString());
				name.setLength(0);
				uid = null;
			}
		}
	}

}

