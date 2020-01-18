# ordersXmlProcessingApp
Process XML orders and create supplier XML files with products

The application waits for the order files to be received at location ".\Orders\src\main\resources\orders".
Once a file with the name ordersXX.xml is received (where XX is a number, containing 2 digits from 0 to 9), the application will read, process it and wait for another file to process.
If the name of the file is not valid (for e.g. ordersss.xml), the file will not be processed (the console will output an error message).
If the file name is valid, it will read it and create the supplier files at location ".\Orders\src\main\resources\supplierProducts".
Depending on the number of suppliers from the orders file, the app will create that many number of supplier files in the supplierProducts folder, named with the supplier name and the number taken from the name of the orders file (for orders23.xml and 3 suppliers Sony, Panasonic and Apple, 3 files will be created in the supplierProducts folder, named Sony23.xml, Panasonic23.xml and Apple23.xml).
Each product from the suppliers' xml file will contain the order ID, so it will be easily tracked to the original order.
