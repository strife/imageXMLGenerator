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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SimpleRenderer extends AbstractRenderer {

    public SimpleRenderer(Block block, VariableManager variableManager, Graphics graphics) {
        super(block, variableManager, graphics);
    }

    @Override
    public void render() {
        try {
            renderImage(block, variableManager, attributes, graphics);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        renderBackground(block, graphics);
        renderText(block);
    }

    public void renderText(Block block) {
        Element blockElement = block.getElement();
        if (block.hasTextContent()) {
            String textContent = block.getTextContent();

            Integer fontSize = 20;
            if (block.hasAttribute("font-size")) {
                fontSize = Integer.valueOf(blockElement.getAttribute("font-size"));
            }

            String fontFamily = "Arial";
            if (block.hasAttribute("font-family")) {
                fontFamily = String.valueOf(blockElement.getAttribute("font-family"));
            }

            int fontWeight = Font.PLAIN;
            if (block.hasAttribute("font-weight") && blockElement.getAttribute("font-weight").equals("bold")) {
                fontWeight = Font.BOLD;
            }

            Color fontColor = Color.black;
            if (block.hasAttribute("color")) {
                fontColor = getColorByName(blockElement.getAttribute("color"));
            }

            Font f = new Font(fontFamily, fontWeight, fontSize);
            Graphics2D g1 = (Graphics2D) graphics;
            g1.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g1.setColor(fontColor);
            g1.setFont(f);

            int paddingTop = 0;
            if (block.hasAttribute("padding-top")) {
                paddingTop = (Integer.valueOf(blockElement.getAttribute("padding-top")));
            }

            g1.drawString(textContent, block.getPosX(), block.getPosY() + paddingTop);
        }
    }

    public void renderBackground(Block block, Graphics g) {
        if (block.getBackground() != null) {
            g.setColor(getColorByName(block.getBackground()));
            g.fillRect(block.getPosX(), block.getPosY(), block.getWidth(), block.getHeight());
        }
    }

    public void renderImage(Block block, VariableManager variableManager, NamedNodeMap attributes, Graphics g) throws FileNotFoundException {
        if (block.hasAttribute("path")) {
            String nodeValue = attributes.getNamedItem("path").getNodeValue();
            String filename = variableManager.getVariable(nodeValue);

            // if it's variable
            if (! nodeValue.contains("${")) {
                filename = variableManager.getImagesPath() + File.separator + nodeValue;
            }

            BufferedImage originalImage = null;
            try {
                originalImage = ImageIO.read(new File(filename));
            } catch (IOException e) {
                e.printStackTrace();
                throw new FileNotFoundException("Filename: " + filename + " " + e.getMessage());
            }

            if ("yes".equals(block.getScale())) {
                Image scaledImage = originalImage.getScaledInstance(block.getWidth(), block.getHeight(), Image.SCALE_SMOOTH);
                g.drawImage(scaledImage, block.getPosX(), block.getPosY(), null);
            } else {
                int newWidth, newHeight;

                double ratio = 1.0 * originalImage.getWidth() / originalImage.getHeight();

                if (block.hasAttribute("width") && ! block.hasAttribute("height")) {
                    newWidth = block.getWidth();
                    newHeight = (int) (block.getWidth() / ratio);

                } else if (! block.hasAttribute("width") && block.hasAttribute("height")) {
                    newHeight = block.getHeight();
                    newWidth = (int) (block.getHeight() * ratio);

                } else if (block.hasAttribute("width") && block.hasAttribute("height")) {
                    newWidth  = block.getWidth();
                    newHeight = block.getHeight();

                } else {
                    // keep original values
                    newWidth  = originalImage.getWidth();
                    newHeight = originalImage.getHeight();
                }

                Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                g.drawImage(scaledImage, block.getPosX(), block.getPosY(), null);

                // g.drawImage(originalImage, block.getPosX(), block.getPosY(), null);
            }

        }
    }

    private Color getColorByName(String name) {
        Integer op1;
        try {
            op1 = Integer.parseInt(name);
        } catch (NumberFormatException e) {
//            System.out.println("Wrong number");
            op1 = 0;
        }

        if (op1 == 0) {
            return new Color(
                    Integer.valueOf(name.substring( 1, 3 ), 16 ),
                    Integer.valueOf(name.substring( 3, 5 ), 16 ),
                    Integer.valueOf(name.substring(5, 7), 16) );
        } else {
            return new Color(op1);
        }

    }

}
