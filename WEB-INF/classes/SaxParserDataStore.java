
/*********


 http://www.saxproject.org/

 SAX is the Simple API for XML, originally a Java-only API.
 SAX was the first widely adopted API for XML in Java, and is a �de facto� standard.
 The current version is SAX 2.0.1, and there are versions for several programming language environments other than Java.

 The following URL from Oracle is the JAVA documentation for the API

 https://docs.oracle.com/javase/7/docs/api/org/xml/sax/helpers/DefaultHandler.html
 *********/

import java.io.IOException;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


////////////////////////////////////////////////////////////

/**************

 SAX parser use callback function  to notify client object of the XML document structure.
 You should extend DefaultHandler and override the method when parsin the XML document
 ***************/

////////////////////////////////////////////////////////////

public class SaxParserDataStore extends DefaultHandler {
    Product product;
    Product phone;
    Product laptop;
    Product voiceAssistant;
    Product accessory;
    static Map<Long, Product> wearableTechnologyHashMap;
    static Map<Long, Product> phoneHashMap;
    static Map<Long, Product> laptopHashMap;
    static Map<Long, Product> voiceAssistantHashMap;
    static Map<Long, Product> accessoriesHasMap;
//    HashMap<Integer, Integer> accessoryHashMap;
    String consoleXmlFileName;
    String elementValueRead;
    String currentElement = "";
    Long count = 0l;

    public SaxParserDataStore() {
    }

    public SaxParserDataStore(String consoleXmlFileName) {
        this.consoleXmlFileName = consoleXmlFileName;
        wearableTechnologyHashMap = new HashMap<>();
        phoneHashMap = new HashMap<>();
        laptopHashMap = new HashMap<>();
        voiceAssistantHashMap = new HashMap<>();
        accessoriesHasMap = new HashMap<>();
//        accessoryHashMap = new HashMap<>();
        parseDocument();
    }

    //parse the xml using sax parser to get the data
    private void parseDocument() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(consoleXmlFileName, this);
        } catch (ParserConfigurationException e) {
            System.out.println("ParserConfig error");
            e.printStackTrace();
        } catch (SAXException e) {
            System.out.println("SAXException : xml not well formed");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO error");
            e.printStackTrace();
        }
    }


////////////////////////////////////////////////////////////

    /*************

     There are a number of methods to override in SAX handler  when parsing your XML document :

     Group 1. startDocument() and endDocument() :  Methods that are called at the start and end of an XML document.
     Group 2. startElement() and endElement() :  Methods that are called  at the start and end of a document element.
     Group 3. characters() : Method that is called with the text content in between the start and end tags of an XML document element.


     There are few other methods that you could use for notification for different purposes, check the API at the following URL:

     https://docs.oracle.com/javase/7/docs/api/org/xml/sax/helpers/DefaultHandler.html
     ***************/

////////////////////////////////////////////////////////////

    // when xml start element is parsed store the id into respective hashmap for console,games etc
    @Override
    public void startElement(String str1, String str2, String elementName, Attributes attributes) throws SAXException {

        if (elementName.equalsIgnoreCase(ProductCategory.WearableTechnology.toString())) {
            currentElement = "wearableTechnology";
            product = new Product();
            product.setCategory(ProductCategory.WearableTechnology);
//            product.setProductId(Long.parseLong(attributes.getValue("id")));
            product.setProductId(count++);
        }
        if (elementName.equalsIgnoreCase(ProductCategory.Laptop.toString())) {
            currentElement = "laptop";
            laptop = new Product();
            laptop.setCategory(ProductCategory.Phone);
//            laptop.setProductId(Long.parseLong(attributes.getValue("id")));
            laptop.setProductId(count++);
        }
        if (elementName.equalsIgnoreCase(ProductCategory.Phone.toString())) {
            currentElement = "phone";
            phone = new Product();
            phone.setCategory(ProductCategory.Laptop);
//            phone.setProductId(Long.parseLong(attributes.getValue("id")));
            phone.setProductId(count++);
        }
        if (elementName.equalsIgnoreCase("voiceAssistant")) {
            currentElement = "voiceAssistant";
            voiceAssistant = new Product();
            voiceAssistant.setCategory(ProductCategory.VoiceAssistant);
//            voiceAssistant.setProductId(Long.parseLong(attributes.getValue("id")));
            voiceAssistant.setProductId(count++);
        }
        if (elementName.equals("accessory") && !currentElement.equals("wearableTechnology")) {
            currentElement = "accessory";
            accessory = new Product();
            accessory.setCategory(ProductCategory.Accessories);
//            accessory.setProductId(Long.parseLong(attributes.getValue("id")));
            accessory.setProductId(count++);
        }


    }

    // when xml end element is parsed store the data into respective hashmap for console,games etc respectively
    @Override
    public void endElement(String str1, String str2, String element) throws SAXException {

        if (element.equals("wearableTechnology")) {
            wearableTechnologyHashMap.put(product.getProductId(), product);
            return;
        }

        if (element.equals("laptop")) {
            laptopHashMap.put(laptop.getProductId(), laptop);
            return;
        }
        if (element.equals("phone")) {
            phoneHashMap.put(phone.getProductId(), phone);
            return;
        }

        if (element.equals("voiceAssistant")) {
            voiceAssistantHashMap.put(voiceAssistant.getProductId(), voiceAssistant);
            return;
        }

        if (element.equals("accessory") && currentElement.equals("accessory")) {
            accessoriesHasMap.put(accessory.getProductId(), accessory);
            return;
        }
//        if (element.equals("accessory") && currentElement.equals("wearableTechnology")) {
//            accessoryHashMap.put(Integer.parseInt(elementValueRead), Integer.parseInt(elementValueRead));
//        }
//        if (element.equalsIgnoreCase("accessories") && currentElement.equals("wearableTechnology")) {
//            product.setAccessories(accessoryHashMap);
//            accessoryHashMap = new HashMap<>();
//            return;
//        }
        if (element.equalsIgnoreCase("image")) {
            if (currentElement.equals("wearableTechnology"))
                product.setImage(elementValueRead);
            if (currentElement.equals("phone"))
                phone.setImage(elementValueRead);
            if (currentElement.equals("laptop"))
                laptop.setImage(elementValueRead);
            if (currentElement.equals("voiceAssistant"))
                voiceAssistant.setImage(elementValueRead);
            if (currentElement.equals("accessory"))
                accessory.setImage(elementValueRead);
            return;
        }


        if (element.equalsIgnoreCase("discount")) {
            if (currentElement.equals("wearableTechnology"))
                product.setDiscount(Double.parseDouble(elementValueRead));
            if (currentElement.equals("phone"))
                phone.setDiscount(Double.parseDouble(elementValueRead));
            if (currentElement.equals("laptop"))
                laptop.setDiscount(Double.parseDouble(elementValueRead));
            if (currentElement.equals("voiceAssistant"))
                voiceAssistant.setDiscount(Double.parseDouble(elementValueRead));
            if (currentElement.equals("accessory"))
                accessory.setDiscount(Double.parseDouble(elementValueRead));
            return;
        }


        if (element.equalsIgnoreCase("condition")) {
            if (currentElement.equals("wearableTechnology"))
                product.setCondition(elementValueRead);
            if (currentElement.equals("phone"))
                phone.setCondition(elementValueRead);
            if (currentElement.equals("laptop"))
                laptop.setCondition(elementValueRead);

            if (currentElement.equals("voiceAssistant"))
                voiceAssistant.setCondition(elementValueRead);
            if (currentElement.equals("accessory"))
                accessory.setCondition(elementValueRead);
            return;
        }

        if (element.equalsIgnoreCase("manufacturer")) {
            if (currentElement.equals("wearableTechnology"))
                product.setManufacturer(ProductManufacturers.getEnum(elementValueRead));
            if (currentElement.equals("phone"))
                phone.setManufacturer(ProductManufacturers.getEnum(elementValueRead));
            if (currentElement.equals("laptop"))
                laptop.setManufacturer(ProductManufacturers.getEnum(elementValueRead));
            if (currentElement.equals("voiceAssistant"))
                voiceAssistant.setManufacturer(ProductManufacturers.getEnum(elementValueRead));
            if (currentElement.equals("accessory"))
                accessory.setManufacturer(ProductManufacturers.getEnum(elementValueRead));
            return;
        }

        if (element.equalsIgnoreCase("name")) {
            if (currentElement.equals("wearableTechnology"))
                product.setName(elementValueRead);
            if (currentElement.equals("phone"))
                phone.setName(elementValueRead);
            if (currentElement.equals("laptop"))
                laptop.setName(elementValueRead);

            if (currentElement.equals("voiceAssistant"))
                voiceAssistant.setName(elementValueRead);
            if (currentElement.equals("accessory"))
                accessory.setName(elementValueRead);
            return;
        }

        if (element.equalsIgnoreCase("price")) {
            if (currentElement.equals("wearableTechnology"))
                product.setPrice(Double.parseDouble(elementValueRead));
            if (currentElement.equals("phone"))
                phone.setPrice(Double.parseDouble(elementValueRead));
            if (currentElement.equals("laptop"))
                laptop.setPrice(Double.parseDouble(elementValueRead));
            if (currentElement.equals("voiceAssistant"))
                voiceAssistant.setPrice(Double.parseDouble(elementValueRead));
            if (currentElement.equals("accessory"))
                accessory.setPrice(Double.parseDouble(elementValueRead));
            return;
        }

        if (element.equalsIgnoreCase("description")) {
            if (currentElement.equals("wearableTechnology"))
                product.setDescription(elementValueRead);
            if (currentElement.equals("phone"))
                phone.setDescription(elementValueRead);
            if (currentElement.equals("laptop"))
                laptop.setDescription(elementValueRead);
            if (currentElement.equals("voiceAssistant"))
                voiceAssistant.setDescription(elementValueRead);
            if (currentElement.equals("accessory"))
                accessory.setDescription(elementValueRead);
            return;
        }

        if (element.equalsIgnoreCase("rebate")) {
            if (currentElement.equals("wearableTechnology"))
                product.setRebate(Double.parseDouble(elementValueRead));
            if (currentElement.equals("phone"))
                phone.setRebate(Double.parseDouble(elementValueRead));
            if (currentElement.equals("laptop"))
                laptop.setRebate(Double.parseDouble(elementValueRead));
            if (currentElement.equals("voiceAssistant"))
                voiceAssistant.setRebate(Double.parseDouble(elementValueRead));
            if (currentElement.equals("accessory"))
                accessory.setRebate(Double.parseDouble(elementValueRead));
            return;
        }

    }

    //get each element in xml tag
    @Override
    public void characters(char[] content, int begin, int end) throws SAXException {
        elementValueRead = new String(content, begin, end);
    }


    /////////////////////////////////////////
    // 	     Kick-Start SAX in main       //
    ////////////////////////////////////////

    //call the constructor to parse the xml and get product details
    public static void addHashmap() {
        String TOMCAT_HOME = System.getProperty("catalina.home");
        new SaxParserDataStore(TOMCAT_HOME + "/webapps/Tutorial_1/ProductCatalog.xml");
    }
}
