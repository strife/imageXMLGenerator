/*
* This file is part of the ImageXMLGenerator package.
*
* (c) Maciej `strife` Leśniak <ml@arivo.pl>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/

package pl.arivo.imageXMLGenerator.renderers;

import pl.arivo.imageXMLGenerator.core.Block;
import pl.arivo.imageXMLGenerator.core.VariableManager;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.awt.*;

abstract class AbstractRenderer implements BaseRenderer {
    protected final Graphics graphics;
    protected final VariableManager variableManager;
    protected final Block block;

    protected NamedNodeMap attributes;

    public AbstractRenderer (Block block, VariableManager variableManager, Graphics graphics) {
        attributes = getNamedNodeMap(variableManager, block.getElement());
        this.variableManager = variableManager;
        this.graphics = graphics;
        this.block = block;
    }

    private NamedNodeMap getNamedNodeMap(VariableManager variableManager, Element blockElement) {
        NamedNodeMap attributes = blockElement.getAttributes();

        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                Node node = attributes.item(i);

                if (variableManager != null && variableManager.hasVariable(node.getNodeValue())) {
                    node.setNodeValue(variableManager.getVariable(node.getNodeValue()));
                }
            }
        }
        return attributes;
    }
}
