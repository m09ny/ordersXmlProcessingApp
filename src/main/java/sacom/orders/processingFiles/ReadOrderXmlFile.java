package sacom.orders.processingFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sacom.orders.entities.Product;

@Component
public class ReadOrderXmlFile {

	private static String fileName = "";
	private static String filePath = "";

	public void getFileNameFromDir(File file) {

		filePath = file.getAbsolutePath();
		fileName = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1);

		if (!fileName.isEmpty() && fileName != null) {
			process();
		}
	}

	public void process() {

		try {
			File fXmlFile = new File(filePath);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			HashMap<String, List<Product>> productMap = new HashMap<>();

			NodeList nListOrders = doc.getElementsByTagName("order");

			for (int i = 0; i < nListOrders.getLength(); i++) {

				Node nNodeOrders = nListOrders.item(i);

				if (nNodeOrders.getNodeType() == Node.ELEMENT_NODE) {

					NodeList nListProduct = doc.getElementsByTagName("product");

					for (int tempProduct = 0; tempProduct < nListProduct.getLength(); tempProduct++) {

						Node nNodeProduct = nListProduct.item(tempProduct);
						if (nNodeProduct.getNodeType() == Node.ELEMENT_NODE) {
							if (nNodeProduct.getParentNode().getAttributes().getNamedItem("ID")
									.equals(nNodeOrders.getAttributes().getNamedItem("ID"))) {

								Element eElementProduct = (Element) nNodeProduct;

								Product prod = new Product(
										eElementProduct.getElementsByTagName("description").item(0).getTextContent(),
										eElementProduct.getElementsByTagName("gtin").item(0).getTextContent(),
										Double.parseDouble(
												eElementProduct.getElementsByTagName("price").item(0).getTextContent()),
										eElementProduct.getElementsByTagName("price").item(0).getAttributes().item(0)
												.getTextContent(),
										nNodeProduct.getParentNode().getAttributes().getNamedItem("ID").getNodeValue()
												.toString());

								String supplierKey = eElementProduct.getElementsByTagName("supplier").item(0)
										.getTextContent();

								if (productMap.containsKey(supplierKey)) {
									productMap.get(supplierKey).add(prod);
								} else {
									ArrayList<Product> newProductList = new ArrayList<Product>();
									newProductList.add(prod);

									productMap.put(supplierKey, newProductList);
								}

							}
						}
					}
				}
			}
			new WriteSupplierXmlFiles().writeFiles(fXmlFile.getName(), productMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}