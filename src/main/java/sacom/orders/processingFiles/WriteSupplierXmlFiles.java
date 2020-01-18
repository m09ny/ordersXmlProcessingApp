package sacom.orders.processingFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sacom.orders.entities.Product;

public class WriteSupplierXmlFiles {

	public void writeFiles(String ordersFileName, HashMap<String, List<Product>> productMap) {

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			for (Entry<String, List<Product>> entry : productMap.entrySet()) {
				// root elements
				Document doc = docBuilder.newDocument();
				Element rootElement = doc.createElement("products");
				doc.appendChild(rootElement);

				// product elements
				Element product = doc.createElement("product");
				rootElement.appendChild(product);

				String key = entry.getKey();
				List<Product> productList = new ArrayList<>();

				productList = entry.getValue();
				for (Product p : productList) {
					// description elements
					Element description = doc.createElement("description");
					description.appendChild(doc.createTextNode(p.getDescription()));
					product.appendChild(description);

					// gtin elements
					Element gtin = doc.createElement("gtin");
					gtin.appendChild(doc.createTextNode(p.getGtin()));
					product.appendChild(gtin);

					// price elements
					Element price = doc.createElement("price");
					price.appendChild(doc.createTextNode(String.valueOf(p.getPrice())));
					product.appendChild(price);

					// set attribute for price
					Attr attr = doc.createAttribute("currency");
					attr.setValue(p.getCurrency());
					price.setAttributeNode(attr);

					// orderid elements
					Element orderid = doc.createElement("orderid");
					orderid.appendChild(doc.createTextNode(p.getOrderid()));
					product.appendChild(orderid);

				}

				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				int num = Integer.valueOf(ordersFileName.replaceAll("[^0-9]", "").trim());

				StreamResult result = new StreamResult(
						new File("../orders/src/main/resources/supplierProducts/" + key + num + ".xml"));
				transformer.transform(source, result);
				System.out.println(key + num + ".xml file was saved!");

			}

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}
