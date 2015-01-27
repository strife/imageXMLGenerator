ImageXMLGenerator for Java
==========================

**ImageXMLGenerator** generates images according to XML document. It allows you to specify layers in the XML file
which one layer can be hidden with another one. It also supports transparency.

## Features

  * Generates JPEG images according to XML document
  * Supports transparency and layers

## Requirements

 * Java 1.6+
 * Maven 3.0+

 ### Getting started

 You can use jars or Maven.

Usage
-----

To generate image with your XML you need to create an XML document. Below you will find XML example.
You can use XMl file or XML as the String passing through the constructor.

```xml
<container width="500" height="500">
    <block height="250" background="#D92F34"></block>
    <block posX="250" width="250" height="250" background="#7D95DB"></block>
    <block posY="300" posX="50" width="400" height="150" background="#FCD628"></block>
    <block posY="10" posX="300" height="250" path="bugs.png"></block>
    <block posX="20" path="coyote.png"></block>
</container>
```


 ```java
import pl.arivo.imageXMLGenerator.*;

// Build objects and parses variables
String xml = new Scanner(new File("test.xml")).useDelimiter("\\Z").next();
TemplateReader templateReader = new TemplateReader(xml, new VariableManager("path_to_external_images"));
templateReader.build();

// Generates image according to objects that were built
ImageXMLGenerator imageXMLGenerator = new ImageXMLGenerator(templateReader);
imageXMLGenerator.build();

// we can have BufferedImage or ...
BufferedImage bufferedImage = imageXMLGenerator.getImage();

// ... save it to file
try {
    imageXMLGenerator.writeToFile(PATH, FILENAME);
} catch (IOException e) {
    e.printStackTrace();
}
```

### Example of generated image

[[link]]

Author
------

Maciej `strife` Leśniak - <ml@arivo.pl>

License
-------

ImageXMLGenerator for Java is licensed under the MIT License - see the `LICENSE` file for details

