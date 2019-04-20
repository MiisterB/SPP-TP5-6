import java.awt.image.BufferedImage;

public class GrayLevelFilter implements IFilter {

	@Override
	public int getMargin() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*Fonction qui applique le filtre
	 * 
	 */
	@Override
	public void applyFilterAtPoint(int x, int y, BufferedImage imgIn, BufferedImage imgOut) 
	{
		int rgb    = imgIn.getRGB(x,y);
		// extracting red, green and blue components from rgb integer
		int red    = (rgb >> 16) & 0x000000FF;
		int green  = (rgb >>  8) & 0x000000FF;
		int blue   = (rgb      ) & 0x000000FF;
		// computing new color from extracted components
		int gray = (red + green + blue)/3;
		int newRgb = ( ( (gray << 8) | gray ) << 8 ) | gray ; // rotating RGB values
		imgOut.setRGB(x,y,newRgb);
	}

}
