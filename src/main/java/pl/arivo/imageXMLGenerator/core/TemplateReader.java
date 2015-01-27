/*
* This file is part of the ImageXMLGenerator package.
*
* (c) Maciej `strife` Leśniak <ml@arivo.pl>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/

package pl.arivo.imageXMLGenerator.core;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import pl.arivo.imageXMLGenerator.exception.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TemplateReader {
    private List<Block> blockList;
    private Container container;
    private VariableManager variableManager;
    private Document document;
    private String xmlOutput;

    public TemplateReader(String xmlOutput) {
        this.xmlOutput = xmlOutput;
    }

    public TemplateReader(String xmlOutput, VariableManager variableManager) {
        variableManager.setTemplateReader(this);
        this.variableManager = variableManager;
        this.xmlOutput = xmlOutput;
    }

    public void build() throws ImageXMLGeneratorException, SAXException, ParserConfigurationException, IOException {
        container = getDrawContainer();
        setBlockList(getPopulatedBlockList(container));

    }

    public ArrayList getPopulatedBlockList(Container container) throws BlocksNotFoundException {
        NodeList nList = getBlocks();
        ArrayList blockList = new ArrayList<>();

        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                Block block = new Block(element, container, element.getTextContent());

                blockList.add(block);

            }
        }
        return blockList;
    }

    private Container getDrawContainer() throws ParserConfigurationException, SAXException, IOException, ContainerNotFoundException, ContainerNoSuchAttributeHeight, ContainerNoSuchAttributeWidth {
        buildDocumentBuilder();
        Node containerElement = getContainerElement();

        return getContainer(containerElement);
    }

    private NodeList getBlocks() throws BlocksNotFoundException {
        NodeList block = document.getElementsByTagName("block");
        if (block == null || block.getLength() <= 0) throw new BlocksNotFoundException();
        return block;
    }

    private Container getContainer(Node containerElement) throws ContainerNoSuchAttributeWidth, ContainerNoSuchAttributeHeight {
        Integer containerWidth = getContainerWidth(containerElement);
        Integer containerHeight = getContainerHeight(containerElement);

        return new Container(containerWidth, containerHeight);
    }

    private Integer getContainerHeight(Node containerElement) throws ContainerNoSuchAttributeHeight {
        Node containerHeightNode = containerElement.getAttributes().getNamedItem("height");
        if (containerHeightNode == null) throw new ContainerNoSuchAttributeHeight();
        return Integer.valueOf(containerHeightNode.getTextContent());
    }

    private Integer getContainerWidth(Node containerElement) throws ContainerNoSuchAttributeWidth {
        Node containerWidthNode = containerElement.getAttributes().getNamedItem("width");
        if (containerWidthNode == null) throw new ContainerNoSuchAttributeWidth();
        return Integer.valueOf(containerWidthNode.getTextContent());
    }

    private Node getContainerElement() throws ContainerNotFoundException {
        Node container1 = document.getElementsByTagName("container").item(0);
        if (container1 == null) throw new ContainerNotFoundException();
        return container1;
    }

    private void buildDocumentBuilder() throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder documentBuilder = getDocumentBuilder();

        document = documentBuilder.parse(new InputSource(new StringReader(xmlOutput)));
        document.normalizeDocument();
    }

    private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        return documentBuilderFactory.newDocumentBuilder();
    }

    public List<Block> getBlockList() {
        return blockList;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public VariableManager getVariableManager() {
        return variableManager;
    }

    public void setVariableManager(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    public Document getDocument() {
        return document;
    }

    public void setBlockList(List<Block> blockList) {
        this.blockList = blockList;
    }
}
