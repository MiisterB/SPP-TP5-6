import java.awt.image.BufferedImage;

public class BasicContourExtractorFilter implements IFilter {

	@Override
	public int getMargin() 
	{
		return 1;
	}

	@Override
	public void applyFilterAtPoint(int x, int y, BufferedImage imgIn, BufferedImage imgOut) 
	{
		int deltaX = ((exctractBlue(x+1,y,imgIn) - exctractBlue(x-1,y,imgIn)))/2;
		int deltaY = ((exctractBlue(x,y+1,imgIn) - exctractBlue(x,y-1,imgIn)))/2;
		
		double gradientNorm = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
		
		int newRgb = Math.max(0, 255 - 8*(int)( Math.abs(gradientNorm)));
		imgOut.setRGB(x,y,newRgb & 0x000000FF);
	}
	
	public int exctractBlue(int x,int y, BufferedImage imgIn)
	{
		int rgb    = imgIn.getRGB(x,y);
		int blue   = (rgb      ) & 0x000000FF;
		return blue;
	}
}
