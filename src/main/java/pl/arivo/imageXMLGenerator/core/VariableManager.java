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

import java.util.HashMap;
import java.util.Map;

public class VariableManager {
    private TemplateReader templateReader;
    private Map<String, String> variables;

    private String imagesPath = "";

    public VariableManager(String imagesPath) {
        this.variables = new HashMap<String, String>();

        this.imagesPath = imagesPath;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    public void setVariable(String name, String value) {
        variables.put("${" + name + "}", value);

        refreshDocument();
    }

    private void refreshDocument() {
        Document document = templateReader.getDocument();
        NodeList nodeList = document.getElementsByTagName("param");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String variable = getVariable(element.getAttribute("var"));

                if (! "".equals(variable) && variable != null) {
                    element.setTextContent(variable);
                }

            }
        }
    }

    public String getVariable(String name) {
        name = name.replace("$", "");
        name = name.replace("{", "");
        name = name.replace("}", "");

        return variables.get("${" + name + "}");
    }

    public boolean hasVariable(String name) {
        return variables.get("${" + name + "}") != null ? true : false;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }

    public void setTemplateReader(TemplateReader templateReader) {
        this.templateReader = templateReader;
    }
}
