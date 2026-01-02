package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Executor extends Thread{

	
	public static void main(String args[]) throws InterruptedException, ExecutionException
	{
		
		ExecutorService pool=Executors.newFixedThreadPool(5);
		
		Runnable run=()->{
			System.out.println(Thread.currentThread().getId()+" =="+Thread.currentThread().getName());
		};
		
		
		Callable<Integer> call=()->{
			System.out.println(Thread.currentThread().getId()+" =="+Thread.currentThread().getName());
			return 123;
		};
		
		
		
		pool.submit(run);
		Future<Integer> result=pool.submit(call);
		
		System.out.println(result.get());
		
		pool.shutdown();
	}
	

}
