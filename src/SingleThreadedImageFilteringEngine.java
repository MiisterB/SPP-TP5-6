import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.Color ;
import java.io.File;


public class SingleThreadedImageFilteringEngine implements IImageFilteringEngine{

	static public void main(String[] args) throws Exception 
	{
		SingleThreadedImageFilteringEngine engine = new SingleThreadedImageFilteringEngine();
		engine.loadImage("imageGrayFilter");
		IFilter grayFilter = new GrayLevelFilter();
		IFilter contourFilter = new BasicContourExtractorFilter();
		//engine.applyFilter(grayFilter);
		engine.applyFilter(contourFilter);
		engine.writeOutPngImage("imageContourFilter");
	}
	
	//attribut 
	BufferedImage loadedImage;
	
	//Foncion qui load l'image pour qu'on puisse y appliquer les filtres
	@Override
	public void loadImage(String inputImage) throws Exception {
		 loadedImage  = ImageIO.read(new File("TEST_IMAGES/" + inputImage + ".png"));
	}

	@Override
	public void writeOutPngImage(String outFile) throws Exception {
		File f = new File("TEST_IMAGES/"+outFile+".png");
	    ImageIO.write(getImg(), "png", f);
	}

	@Override
	public void setImg(BufferedImage newImg) {
		loadedImage = newImg;
	}

	@Override
	public BufferedImage getImg() {
		return loadedImage;
	}

	@Override
	public void applyFilter(IFilter someFilter) 
	{
		BufferedImage filteredImg = new BufferedImage(loadedImage.getWidth(),loadedImage.getHeight(),BufferedImage.TYPE_INT_RGB);
		for (int x = someFilter.getMargin(); x < loadedImage.getWidth() - someFilter.getMargin(); x++) 
		{
		      for (int y = someFilter.getMargin(); y < loadedImage.getHeight() - someFilter.getMargin(); y++) 
		      {
		    	  someFilter.applyFilterAtPoint(x, y, loadedImage, filteredImg);
		      }
		}
		setImg(filteredImg);
	}

}
