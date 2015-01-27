/*
* This file is part of the ImageXMLGenerator package.
*
* (c) Maciej `strife` Leśniak <ml@arivo.pl>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/

package pl.arivo.imageXMLGenerator;

import pl.arivo.imageXMLGenerator.core.TemplateReader;
import org.junit.Test;
import org.xml.sax.SAXException;
import pl.arivo.imageXMLGenerator.exception.ContainerNoSuchAttributeHeight;
import pl.arivo.imageXMLGenerator.exception.ContainerNoSuchAttributeWidth;
import pl.arivo.imageXMLGenerator.exception.ContainerNotFoundException;
import pl.arivo.imageXMLGenerator.exception.ImageXMLGeneratorException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class TemplateReaderTest  {
    @Test
    public void testInstance() {
        TemplateReader templateReader = new TemplateReader(new String());
        assertNotNull(templateReader);
    }

    @Test(expected = ContainerNotFoundException.class)
    public void testContainerNotFoundException () throws ParserConfigurationException, IOException, SAXException, ImageXMLGeneratorException {
        String xmlOutput = "<test></test>";

        TemplateReader templateReader = new TemplateReader(xmlOutput);
        templateReader.build();

    }

    @Test
    public void testContainerElementInstance() throws ParserConfigurationException, SAXException, IOException, ImageXMLGeneratorException {
        String xmlOutput = "<container width=\"100\" height=\"100\"><block></block></container>";

        TemplateReader templateReader = new TemplateReader(xmlOutput);
        templateReader.build();

        assertNotNull(templateReader.getContainer());

    }

    @Test(expected = ContainerNoSuchAttributeWidth.class)
    public void testContainerNoSuchAttributeWidth() throws ParserConfigurationException, SAXException, IOException, ImageXMLGeneratorException {
        String xmlOutput = "<container height=\"100\"><block></block></container>";

        TemplateReader templateReader = new TemplateReader(xmlOutput);
        templateReader.build();

    }

    @Test(expected = ContainerNoSuchAttributeHeight.class)
    public void testContainerNoSuchAttributeHeight() throws ParserConfigurationException, SAXException, IOException, ImageXMLGeneratorException {
        String xmlOutput = "<container width=\"100\"><block></block></container>";

        TemplateReader templateReader = new TemplateReader(xmlOutput);
        templateReader.build();
    }

}
