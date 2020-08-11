import com.sun.xml.internal.ws.resources.WsdlmodelMessages;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * xml4种生成方式
 */
public class XMLUtils {
    public static void main(String[] args) {
        //genXMLByDom();
        //genXMLByDom4j();
        //genXMLByJDOM();
        genXMLBySax();
    }


    /**
     * dom写入
     */
    public static void genXMLByDom(){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            document.setXmlStandalone(true);

            //创建根节点
            Element root = document.createElement("user");
            //创建子节点
            Element uname = document.createElement("uname");
            uname.setTextContent("张三");
            root.appendChild(uname);
            document.appendChild(root);

            //创建TransformerFactory对象
            TransformerFactory tf = TransformerFactory.newInstance();
            //创建Transformer对象
            try {
                Transformer f = tf.newTransformer();
                f.setOutputProperty("encoding","gbk");
                f.transform(new DOMSource(document),new StreamResult(new File("d:/dom/user.xml")));
                System.out.println("xml文件生成成功");
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }


    /**
     * dom4j写入
     */
    public static void genXMLByDom4j(){
        //创建Document对象
        org.dom4j.Document document = DocumentHelper.createDocument();
        //创建根节点
        org.dom4j.Element root = document.addElement("user");

        //生成子节点
        org.dom4j.Element uname = root.addElement("uname");
        uname.setText("张三");

        //设置xml的生成格式
        OutputFormat of = OutputFormat.createCompactFormat();
        of.setNewlines(true);
        of.setEncoding("gbk");

        //生成xml文件
        try {
            XMLWriter writer = new XMLWriter(new FileOutputStream(new File("d:/dom/user1.xml")),of);
            //设置是否转义，默认是使用转义字符
            writer.setEscapeText(false);
            writer.write(document);
            writer.close();
            System.out.println("xml文件生成成功");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * jdom写入
     */
    public static void genXMLByJDOM(){
        //生成一个根节点
        org.jdom2.Element root = new org.jdom2.Element("user");

        //生成一个document对象
        org.jdom2.Document document = new org.jdom2.Document(root);
        org.jdom2.Element uname = new org.jdom2.Element("uname");
        uname.setText("张三");
        root.addContent(uname);

        //设置xml格式
        Format format = Format.getCompactFormat();
        //设置换行tab
        format.setIndent(" ");
        format.setEncoding("gbk");

        //创建XMLOutputter对象
        XMLOutputter outputter = new XMLOutputter(format);
        //利用outputter将document转换成xml文档
        try {
            outputter.output(document,new FileOutputStream(new File("d:/dom/user2.xml")));
            System.out.println("xml文件生成成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * sax写入
     */
    public static void genXMLBySax(){
        //创建一个SaxTransformerFactory对象
        SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

        try {
            //创建TransformerHandler对象
            TransformerHandler handler = saxTransformerFactory.newTransformerHandler();
            //创建Transformer对象
            Transformer transformer = handler.getTransformer();
            //设置编码格式
            transformer.setOutputProperty(OutputKeys.ENCODING,"gbk");
            //设置是否换行
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");

            //创建一个result对象
            try {
                Result result = new StreamResult(new FileOutputStream(new File("d:/dom/user3.xml")));
                //关联handler
                handler.setResult(result);

                //打开document对象
                handler.startDocument();
                //创建根节点
                handler.startElement("","","user",null);
                //创建uname
                handler.startElement("","","uname",null);
                handler.characters(new String("张三").toCharArray(),0,new String("张三").length());
                handler.endElement("","","uname");

                handler.endElement("","","user");

                //关闭document
                handler.endDocument();

                System.out.println("xml文件生成成功");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }


    }
}
