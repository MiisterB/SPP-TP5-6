import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.Color ;
import java.io.File;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;;

public class MultiThreadedImageFilteringEngine implements IImageFilteringEngine{

	//attributs
	BufferedImage loadedImage;
	int nbrThread;
	
	public MultiThreadedImageFilteringEngine(int k)
	{
		nbrThread = k;
		
		Runnable barrier1Action = new Runnable() {
		    public void run() {
		        System.out.println("BarrierAction 1 executed ");
		    }
		};
		Runnable barrier2Action = new Runnable() {
		    public void run() {
		        System.out.println("BarrierAction 2 executed ");
		    }
		};
		
		CyclicBarrier barrier1 = new CyclicBarrier(2, barrier1Action);
		CyclicBarrier barrier2 = new CyclicBarrier(2, barrier2Action);
		
		ThreadWorker[] tabThread = new ThreadWorker[nbrThread]; //on créé un tableau qui va contenir tous nos threads
		
		//on créé les threads
		for(int i = 0;i<nbrThread;i++)
		{
			tabThread[i] = new ThreadWorker(barrier1, barrier2);
		}
		for(int i = 0;i<nbrThread;i++)
		{
			tabThread[i].start();
		}
	}
	
	static public void main(String[] args) throws Exception 
	{
		MultiThreadedImageFilteringEngine engine = new MultiThreadedImageFilteringEngine(4);
		engine.loadImage("imageGrayFilter");
		//engine.distributeWork();
	}
	
	
		
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
	public void applyFilter(IFilter someFilter) {
		//Step 1 : Distribute work among k workers
		
		//Step 2 : Unlock k worker threads
		
		//Step 3 : Wait for all worker threads to complete their iterations
		
		
	}
	
	
	/*
	 * Fonction qui répartis entre les threads le travail à éffectuer (les portions d'image et le filtre)
	 */
	public void distributeWork()
	{
		//on récupère les dimensions de l'image
		int imageHeight = loadedImage.getHeight();
		int imageWidth = loadedImage.getWidth();
		
		//on divise l'image en plusieurs bandes horyzontale de pixels 
		int reste = imageHeight%nbrThread;
		int size = imageHeight/nbrThread;

	}
	
	public class ThreadWorker extends Thread implements Runnable{
		
		//Attributs
		IFilter filtre; 
		int threadNumber; //Numéro du filtre pour savoir quelle portion de l'image on lui attribut au moment où on doit filtrer une image
		CyclicBarrier barrier1 = null;
	    CyclicBarrier barrier2 = null;
	    int width;
	    
		
		public ThreadWorker(CyclicBarrier barrier1, CyclicBarrier barrier2)
		{
		    this.barrier1 = barrier1;
	        this.barrier2 = barrier2;
		}
		
		public void run() 
		{
			while(true)
			{
				try
				{
				//Première barrière
				this.barrier1.await();
					
				//On applique le filtre sur l'image

				int size = loadedImage.getHeight()/nbrThread; //on divise la hauteur de l'image en bande, une par thread, la size est la taille de chaque bande
				int reste = loadedImage.getHeight()%nbrThread; //on récupère le reste de la division pour la dernière bande
				
				int startPixel = size*threadNumber; //on détermine le pixel de départ de la portion du thread en fonction de son numéros
				int endPixel = startPixel + size; //on détermine le pixel de fin à partir de celui de départ
				
				BufferedImage filteredImg = new BufferedImage(loadedImage.getWidth(),loadedImage.getHeight(),BufferedImage.TYPE_INT_RGB);
				for (int x = filtre.getMargin(); x < loadedImage.getWidth() - filtre.getMargin(); x++) 
				{
				      for (int y = startPixel + filtre.getMargin(); y < endPixel - filtre.getMargin(); y++) 
				      {
				    	  filtre.applyFilterAtPoint(x, y, loadedImage, filteredImg);
				      }
				}
				setImg(filteredImg);	
					
				//Deuxième barrière
				this.barrier2.await();
				}
				catch (InterruptedException | BrokenBarrierException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}


}


