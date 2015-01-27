/*
* This file is part of the ImageXMLGenerator package.
*
* (c) Maciej `strife` Leśniak <ml@arivo.pl>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/

package pl.arivo.imageXMLGenerator;

import pl.arivo.imageXMLGenerator.core.ImageXMLGenerator;
import pl.arivo.imageXMLGenerator.core.TemplateReader;
import pl.arivo.imageXMLGenerator.core.VariableManager;
import pl.arivo.imageXMLGenerator.exception.ImageXMLGeneratorException;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class ImageXMLGeneratorTest {
    public static final String PATH = "/home/maciej/Temp/z2";
    public static final String FILENAME = "test";

    private TemplateReader templateReader;
    private ImageXMLGenerator imageXMLGenerator;

    @Before
    public void setUp() throws SAXException, ImageXMLGeneratorException, ParserConfigurationException, IOException {
        String xmlOutput = "<container width=\"500\" height=\"500\">" +
                "<block height=\"250\" background=\"#D92F34\"></block>" +
                "<block posX=\"250\" width=\"250\" height=\"250\" background=\"#7D95DB\"></block>" +
                "<block posY=\"300\" posX=\"50\" width=\"400\" height=\"150\" background=\"#FCD628\"></block>" +
                "<block posY=\"10\" posX=\"300\" height=\"250\" path=\"bugs.png\"></block>" +
                "<block posX=\"20\" path=\"coyote.png\">Test</block>" +
                "</container>";

        // Build objects and parses variables
        templateReader = new TemplateReader(xmlOutput, new VariableManager(PATH));
        templateReader.build();

        // Generates image according to objects that were built
        imageXMLGenerator = new ImageXMLGenerator(templateReader);

    }

    @Test
    public void testBuilderSupportInstance() {
        assertNotNull(imageXMLGenerator);
    }

    @Test
    public void testIfImageGenerated() throws FileNotFoundException {
        imageXMLGenerator.build();

        assertNotNull(imageXMLGenerator.getImage());

    }

   /* @Test
    public void testSaveToFile() throws FileNotFoundException {
        imageXMLGenerator.build();

        BufferedImage bufferedImage = imageXMLGenerator.getImage();

        try {
            imageXMLGenerator.writeToFile(PATH, FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File f = new File("test.jpg");
        assertThat(true, is(f.exists()));

    }*/

}
