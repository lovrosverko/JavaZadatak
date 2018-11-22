
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner; 

public class Threadexample 
{ 
	public static void main(String[] args) 
						throws InterruptedException 
	{ 

		final ProducerConsumer ProducerConsumer = new ProducerConsumer(); 


		Thread t1 = new Thread(new Runnable() 
		{ 
			@Override
			public void run() 
			{ 
				try
				{ 
					ProducerConsumer.produce(); 
				} 
				catch(InterruptedException e) 
				{ 
					e.printStackTrace(); 
				} 
			} 
		}); 


		Thread t2 = new Thread(new Runnable() 
		{ 
			@Override
			public void run() 
			{ 
				try
				{ 
					ProducerConsumer.consume(); 
				} 
				catch(InterruptedException e) 
				{ 
					e.printStackTrace(); 
				} 
			} 
		}); 

		// Start both threads 
		t1.start(); 
		t2.start(); 

		// t1 finishes before t2 
		t1.join(); 
		t2.join(); 
	} 

	public static class ProducerConsumer 
	{ 
		 
		LinkedList<String> transakcija = new LinkedList<>(); 
		int kapacitet = 2; 
		String datoteka = "//home//lovro//eclipse-workspace//testMAI//docs//HOCIDC.001";
		String token;

		
		public void produce() throws InterruptedException 
		{ 
			
			try (Scanner scanner = new Scanner(new File(datoteka));) 
			{
				while (scanner.hasNext()) 
				{ 
					synchronized (this) 
					{ 
						token = scanner.nextLine();
						while (transakcija.size()==kapacitet) 
							wait();
						System.out.println("Producer produced-"
								+ token);
						transakcija.add(token);
						notify(); 
					}
				}
			} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		} 

		// Function called by consumer thread 
		public void consume() throws InterruptedException 
		{ 
			int brojac = 0;
			while (true) 
			{ 
				synchronized (this) 
				{ 

					while (transakcija.size()==0) 
						wait(); 
					String val = transakcija.removeFirst(); 
/*
 * 
 * ovdje sada trebam dodati konverziju u xml
 * pogledati JAXB
 * 
 */
					// debug
					brojac++;
					System.out.println(brojac + " Consumer consumed-"
													+ val);

					notify(); 

				} 
			} 
		} 
	} 
} 
