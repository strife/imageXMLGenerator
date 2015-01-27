/*
* This file is part of the ImageXMLGenerator package.
*
* (c) Maciej `strife` Leśniak <ml@arivo.pl>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/

package pl.arivo.imageXMLGenerator.core;

import pl.arivo.imageXMLGenerator.renderers.BaseRenderer;
import pl.arivo.imageXMLGenerator.renderers.SimpleRenderer;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

public class ImageXMLGenerator {
    private final TemplateReader templateReader;
//    private final BaseRenderer renderer;

    private BufferedImage image;

    public ImageXMLGenerator(TemplateReader templateReader) {
        this.templateReader = templateReader;
//        this.renderer = renderer;
    }


    public void build() throws FileNotFoundException {
        initializeGraphicsContainer();
        for (Block block : templateReader.getBlockList()) {
            prepareBlock(block);
        }
    }

    private void prepareBlock(Block block) throws FileNotFoundException {
        VariableManager variableManager = templateReader.getVariableManager();

        BaseRenderer simpleRenderer = getSimpleRenderer(block, variableManager);
        simpleRenderer.render();

    }

    public SimpleRenderer getSimpleRenderer(Block block, VariableManager variableManager) {
        return new SimpleRenderer(block, variableManager, image.getGraphics());
    }

    private void initializeGraphicsContainer() {
        Container container = getTemplateReader().getContainer();
        image = new BufferedImage(container.getWidth(), container.getHeight(), BufferedImage.TYPE_INT_RGB);
    }

//    public void writeToFile(String path, Slot slot) throws IOException {
    public void writeToFile(String path, String filename) throws IOException {
        File directory = new File(path);
        if (! directory.exists()) directory.mkdirs();

        // File image = new File("myimage.jpg");
        File compressedImageFile = new File(path + "/" + filename + ".jpg");

        ByteArrayOutputStream os1 = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", os1);

        InputStream is = new ByteArrayInputStream(os1.toByteArray());;
        OutputStream os = new FileOutputStream(compressedImageFile);

        float quality = 1.0f;

        // create a BufferedImage as the result of decoding the supplied InputStream
        image = ImageIO.read(is);

        // get all image writers for JPG format
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");

        if (!writers.hasNext())
            throw new IllegalStateException("No writers found");

        ImageWriter writer = (ImageWriter) writers.next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();

        // compress to a given quality
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        // appends a complete image stream containing a single image and
        //associated stream and image metadata and thumbnails to the output
        writer.write(null, new IIOImage(image, null, null), param);

        // close all streams
        is.close();
        os.close();
        ios.close();
        writer.dispose();

//        ImageIO.writeToFile(image, "jpeg", new File(path + "/" + slot.getSlotId() + ".jpg"));

    }

    public TemplateReader getTemplateReader() {
        return templateReader;
    }

    public BufferedImage getImage() {
        return image;
    }

}
