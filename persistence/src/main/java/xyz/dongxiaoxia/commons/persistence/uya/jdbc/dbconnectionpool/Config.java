package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Config {

	public static List<Server> getConfig(String xmlPath) throws Exception {
		List<Server> serverList = new ArrayList<Server>();
		
        Element xmlDoc = getXmlDoc(xmlPath);
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        NodeList nServers = (NodeList) xpath.evaluate("//servers/server", xmlDoc, XPathConstants.NODESET);
        int moduleCount = nServers.getLength();
        for (int i = 0; i < moduleCount; i++) {
            Node node = nServers.item(i);
            Node masterAddressNode = (Node)xpath.evaluate("Address", node, XPathConstants.NODE);
            Node slaverAddressNode = (Node)xpath.evaluate("BackAddress", node, XPathConstants.NODE);
            Node rangeNode = (Node)xpath.evaluate("Range", node, XPathConstants.NODE);
            Node driversClassNode = (Node)xpath.evaluate("DriversClass", node, XPathConstants.NODE);
            Node userNameNode = (Node)xpath.evaluate("UserName", node, XPathConstants.NODE);
            Node passWordNode = (Node)xpath.evaluate("PassWord", node, XPathConstants.NODE);
            Node minPoolSizeNode = (Node)xpath.evaluate("MinPoolSize", node, XPathConstants.NODE);
            Node maxPoolSizeNode = (Node)xpath.evaluate("MaxPoolSize", node, XPathConstants.NODE);
            Node idleTimeoutNode = (Node)xpath.evaluate("IdleTimeout", node, XPathConstants.NODE);
            Node autoShrinkNode = (Node)xpath.evaluate("AutoShrink", node, XPathConstants.NODE);
            Node slaverForbid = (Node)xpath.evaluate("SlaverForbid", node, XPathConstants.NODE);
            
            Server server = new Server();
            server.setMasterAddress(masterAddressNode.getTextContent());
            server.setSlaverAddress(slaverAddressNode.getTextContent());
			String[] ranges = rangeNode.getTextContent().split("-");
			int minRange = Integer.parseInt(ranges[0]);
			int maxRange = Integer.parseInt(ranges[1]);
			server.setMinRange(minRange);
			server.setMaxRange(maxRange);
			server.setDriverClass(driversClassNode.getTextContent());
			server.setUserName(userNameNode.getTextContent());
			server.setPassWord(passWordNode.getTextContent());
			server.setMinPoolSize(Integer.parseInt(minPoolSizeNode.getTextContent()));
			server.setMaxPoolSize(Integer.parseInt(maxPoolSizeNode.getTextContent()));
			server.setIdleTimeout(Integer.parseInt(idleTimeoutNode.getTextContent()));
			server.setAutoShrink(Boolean.parseBoolean(autoShrinkNode.getTextContent()));
			server.setSlaverForbid(Boolean.parseBoolean(slaverForbid.getTextContent()));
			
			Node mfNode = masterAddressNode.getAttributes().getNamedItem("forbid");
			if(mfNode != null) {
				String forbid = mfNode.getTextContent();
				if(forbid != null && forbid.equalsIgnoreCase("true")) {
					server.setMasterForbid(true);
				}
			}
			
			Node sfNode = slaverAddressNode.getAttributes().getNamedItem("forbid");
			if(sfNode != null) {
				String forbid = sfNode.getTextContent();
				if(forbid != null && forbid.equalsIgnoreCase("true")) {
					server.setSlaverForbid(true);
				}
			}
			
			serverList.add(server);
        }
        
        return serverList;
	}

    private static Element getXmlDoc(String filePath) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db ;
		Document doc = null;
        try {
            db = dbf.newDocumentBuilder();
			doc = db.parse(filePath);
        } catch (ParserConfigurationException pce) {
        	pce.printStackTrace();
        } catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return doc == null? null:doc.getDocumentElement();
    }
	
	public static class Server {
		private String masterAddress;
		private String slaverAddress;
		private int minRange;
		private int maxRange;
		private String driverClass;
		private String userName;
		private String passWord;
		private int minPoolSize;
		private int maxPoolSize;
		private int idleTimeout;
		private boolean autoShrink;
		private boolean masterForbid;
		private boolean slaverForbid;
		
		
		public Server() {
			
		}


		public String getMasterAddress() {
			return masterAddress;
		}


		public String getSlaverAddress() {
			return slaverAddress;
		}


		public int getMinRange() {
			return minRange;
		}


		public int getMaxRange() {
			return maxRange;
		}


		public String getDriverClass() {
			return driverClass;
		}


		public String getUserName() {
			return userName;
		}


		public String getPassWord() {
			return passWord;
		}


		public int getMinPoolSize() {
			return minPoolSize;
		}


		public int getMaxPoolSize() {
			return maxPoolSize;
		}


		public int getIdleTimeout() {
			return idleTimeout;
		}


		public boolean isAutoShrink() {
			return autoShrink;
		}


		public boolean isMasterForbid() {
			return masterForbid;
		}


		public boolean isSlaverForbid() {
			return slaverForbid;
		}


		public void setMasterAddress(String masterAddress) {
			this.masterAddress = masterAddress;
		}


		public void setSlaverAddress(String slaverAddress) {
			this.slaverAddress = slaverAddress;
		}


		public void setMinRange(int minRange) {
			this.minRange = minRange;
		}


		public void setMaxRange(int maxRange) {
			this.maxRange = maxRange;
		}


		public void setDriverClass(String driverClass) {
			this.driverClass = driverClass;
		}


		public void setUserName(String userName) {
			this.userName = userName;
		}


		public void setPassWord(String passWord) {
			this.passWord = passWord;
		}


		public void setMinPoolSize(int minPoolSize) {
			this.minPoolSize = minPoolSize;
		}


		public void setMaxPoolSize(int maxPoolSize) {
			this.maxPoolSize = maxPoolSize;
		}


		public void setIdleTimeout(int idleTimeout) {
			this.idleTimeout = idleTimeout;
		}


		public void setAutoShrink(boolean autoShrink) {
			this.autoShrink = autoShrink;
		}


		public void setMasterForbid(boolean masterForbid) {
			this.masterForbid = masterForbid;
		}


		public void setSlaverForbid(boolean slaverForbid) {
			this.slaverForbid = slaverForbid;
		}
 
	}
}