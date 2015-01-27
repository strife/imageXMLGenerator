/*
* This file is part of the ImageXMLGenerator package.
*
* (c) Maciej `strife` Leśniak <ml@arivo.pl>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package pl.arivo.imageXMLGenerator.core;

import org.w3c.dom.Element;

public class Block {
    private final Container container;
    private final Element element;
    private final String textContent;

    private Integer width;
    private Integer height;
    private Integer posX;
    private Integer posY;
    private String align;
    private String path;
    private String scale;

    public Block(Element element, Container container, String textContent) {
        this.element = element;
        this.container = container;
        this.textContent = textContent;
    }

    public boolean hasAttribute(String attributeName) {
        if (! "".equals(element.getAttribute(attributeName))) return true;
        return false;
    }

    public Integer getWidth() {
        if (! "".equals(element.getAttribute("width"))) {
            return Integer.valueOf(element.getAttribute("width"));
        } else {
            return container.getWidth() - getPosX();
        }
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        if (! "".equals(element.getAttribute("height"))) {
            return Integer.valueOf(element.getAttribute("height"));
        } else {
            return container.getHeight() - getPosY();
        }
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getPosX() {
        if (! "".equals(element.getAttribute("posX")) && ! "center".equals(element.getAttribute("posX"))) {
            return Integer.valueOf(element.getAttribute("posX"));
        } else if ("center".equals(element.getAttribute("posX"))) {
            int center = container.getWidth() / 2 - Integer.valueOf(element.getAttribute("width")) / 2;
            return center;
//            return 40;
        }
        return 0;
    }

    public String getBackground() {
        if (! "".equals(element.getAttribute("background"))) {
            return String.valueOf(element.getAttribute("background"));
        }
        return null;
    }

    public void setPosX(Integer posX) {
        this.posX = posX;
    }

    public Integer getPosY() {
        if (! "".equals(element.getAttribute("posY"))) {
            return Integer.valueOf(element.getAttribute("posY"));
        }
        return 0;
    }

    public void setPosY(Integer posY) {
        this.posY = posY;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getPath() {
        return element.getAttribute("path");
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getScale() {
        return element.getAttribute("scale");
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public Element getElement() {
        return element;
    }

    public Container getContainer() {
        return container;
    }

    public String getTextContent() {
        return getElement().getTextContent();
    }

    public boolean hasTextContent() {
        if (! "".equals(getElement().getTextContent())) {
            return true;
        }
        return false;
    }
}
