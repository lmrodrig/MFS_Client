/* @ Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-16      34242JR  R Prechel        -Initial version
 * 2007-08-20      38768JL  R Prechel        -Extend IGSDefaultHandler
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.tree;

import java.io.PipedInputStream;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.ibm.rchland.mfgapps.client.utils.xml.IGSDefaultHandler;
import com.ibm.rchland.mfgapps.mfscommon.MFSActionable;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSPipedTransaction;

/**
 * <code>MFSXmlTreeHandler</code> parses the XML for an XML based tree using a
 * {@link javax.xml.parsers.SAXParser}.
 * @author The MFS Client Development Team
 */
public class MFSXmlTreeHandler
	extends IGSDefaultHandler
	implements Runnable
{
	/**
	 * The <code>MFSXmlParseStrategy</code> used to determine element names
	 * and create node user objects.
	 */
	private MFSXmlParseStrategy strategy;

	/** The <code>MFSPipedTransaction</code> executed to obtain the tree XML. */
	protected MFSPipedTransaction transaction;

	/** The root node of the tree. */
	private DefaultMutableTreeNode rootNode;

	/** The initially selected node of the tree. */
	private DefaultMutableTreeNode selectedNode;

	/** The current node of the tree. */
	private DefaultMutableTreeNode currentNode;

	/** The name of the current element. */
	private String elementName;

	/** Stores the data accumulated for the current element. */
	private StringBuffer elementData;

	/** Stores the data accumulated for the erms element. */
	private StringBuffer erms = new StringBuffer();

	/**
	 * Constructs a new <code>MFSXmlTreeHandler</code>.
	 * @param transaction the <code>MFSPipedTransaction</code> executed to
	 *        obtain the tree XML
	 * @param strategy the <code>MFSXmlParseStrategy</code> used to determine
	 *        the XML element names for the root, leaf, and child nodes and to
	 *        create the user objects for the nodes of the tree
	 */
	public MFSXmlTreeHandler(MFSPipedTransaction transaction, MFSXmlParseStrategy strategy)
	{
		this.transaction = transaction;
		this.strategy = strategy;
	}

	/**
	 * Parses the output of the <code>MFSPipedTransaction</code>.
	 * @param actionable the <code>MFSActionable</code> that indicates the
	 *        transaction is executing
	 * @return the <code>DefaultMutableTreeNode</code> for the root of the
	 *         tree or <code>null</code> if the output of the
	 *         <code>MFSPipedTransaction</code> could not be parsed
	 */
	public DefaultMutableTreeNode parse(MFSActionable actionable)
	{
		String message = this.transaction.getActionMessage();
		actionable.startAction(message);
		Thread t = new Thread(this);
		t.start();
		while (t.isAlive())
		{
			actionable.updateAction(message, -1);
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException ie)
			{
				//Nothing to do
			}
		}
		actionable.stopAction();
		return this.rootNode;
	}

	/** Executes and parses the output of the <code>MFSPipedTransaction</code>. */
	public void run()
	{
		try
		{
			PipedInputStream in = new PipedInputStream();
			this.transaction.connectStreams(in);

			MFSComm.getInstance().execute(this.transaction);

			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.getXMLReader().setFeature(
					"http://xml.org/sax/features/namespaces", true); //$NON-NLS-1$
			parser.parse(in, this);
		}
		catch (Exception e)
		{
			this.rootNode = null;
			this.transaction.setErms(e.toString());
		}
	}

	/**
	 * Returns the root <code>DefaultMutableTreeNode</code>.
	 * @return the <code>DefaultMutableTreeNode</code> for the root of the
	 *         tree or <code>null</code> if the output of the
	 *         <code>MFSPipedTransaction</code> could not be parsed
	 */
	public DefaultMutableTreeNode getRootNode()
	{
		return this.rootNode;
	}

	/**
	 * Returns the initially selected <code>DefaultMutableTreeNode</code>.
	 * @return the initially selected <code>DefaultMutableTreeNode</code> of
	 *         the tree or <code>null</code> if the tree does not have an
	 *         initially selected node
	 */
	public DefaultMutableTreeNode getSelectedNode()
	{
		return this.selectedNode;
	}

	/**
	 * Receives notification of the start of an element.
	 * @param uri the namespace URI
	 * @param localName the local name of the element (without namespace prefix)
	 * @param qName the qualified name of the element (with namespace prefix)
	 * @param attributes the attributes for the element
	 * @throws SAXException if the element name could not be determined
	 */
	public void startElement(String uri, String localName, String qName,
								Attributes attributes)
		throws SAXException
	{
		this.elementName = getElementName(localName, qName);

		if (this.rootNode == null
				&& this.elementName.equals(this.strategy.getRootElementName()))
		{
			MFSUserObject obj = this.strategy.createRootNodeUserObject();
			this.rootNode = new DefaultMutableTreeNode(obj);
			this.currentNode = this.rootNode;
		}
		else if (this.elementName.equals(this.strategy.getChildElementName()))
		{
			MFSUserObject obj = this.strategy.createChildNodeUserObject();
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(obj);
			this.currentNode.add(node);
			this.currentNode = node;
		}
		else if (this.elementName.equals(this.strategy.getLeafElementName()))
		{
			MFSUserObject obj = this.strategy.createLeafNodeUserObject();
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(obj);
			this.currentNode.add(node);
			this.currentNode = node;
		}
		else
		{
			this.elementData = new StringBuffer();
		}
	}

	/**
	 * Receives notification of the end of an element.
	 * @param uri the namespace URI
	 * @param localName the local name of the element (without namespace prefix)
	 * @param qName the qualified name of the element (with namespace prefix)
	 * @throws SAXException if the element name could not be determined
	 */
	public void endElement(String uri, String localName, String qName)
		throws SAXException
	{
		String endElement = getElementName(localName, qName);
		if (this.elementName.equals(this.strategy.getChildElementName())
				|| endElement.equals(this.strategy.getLeafElementName()))
		{
			//Done with a child or leaf element.
			//Check if the node should be selected.
			if (this.strategy.isSelectedNode(this.currentNode))
			{
				this.selectedNode = this.currentNode;
			}
			//The parent of the child or leaf is now the current node.
			this.currentNode = (DefaultMutableTreeNode) this.currentNode.getParent();
		}
		else if (endElement.equals(this.strategy.getRootElementName()))
		{
			//Nothing to do.
			//When the root element is done, the tree has been parsed
		}
		else if (endElement.equals("ERMS")) //$NON-NLS-1$
		{
			this.transaction.setErms(this.erms.toString());
		}
		else if (endElement.equals("RRET")) //$NON-NLS-1$
		{
			//Nothing to do.
			//MFSComm and MFSPipedTransaction set the return code
		}
		else if (this.currentNode != null)
		{
			//The element that just ended was not the end element for a node
			//nor a special element (ERMS or RRET).
			//Thus, add the element data to the current node.
			Object obj = this.currentNode.getUserObject();
			if (obj instanceof MFSUserObject)
			{
				((MFSUserObject) obj).add(endElement, this.elementData.toString());
			}
			else if (obj instanceof String)
			{
				this.currentNode.setUserObject(obj + this.elementData.toString());
			}
			else
			{
				this.currentNode.setUserObject(this.elementData.toString());
			}
		}
	}

	/**
	 * Receives notification of character data inside an element.
	 * @param ch the characters
	 * @param start the start position in the character array
	 * @param length the number of characters to use from the character array
	 * @throws SAXException
	 */
	public void characters(char[] ch, int start, int length)
		throws SAXException
	{
		if (this.elementName.equals("ERMS")) //$NON-NLS-1$
		{
			String data = new String(ch, start, length);
			this.erms.append(data);
		}
		else if (this.elementData.equals("RRET")) //$NON-NLS-1$
		{
			//Discard RRET data
			//MFSComm and MFSPipedTransaction set the return code
		}
		else
		{
			String data = new String(ch, start, length);
			this.elementData.append(data);
		}
	}

	/**
	 * Receives notification of ignorable whitespace inside an element.
	 * @param ch the characters
	 * @param start the start position in the character array
	 * @param length the number of characters to use from the character array
	 * @throws SAXException
	 */
	public void ignorableWhitespace(char[] ch, int start, int length)
		throws SAXException
	{
		characters(ch, start, length);
	}
}
