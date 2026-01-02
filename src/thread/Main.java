package thread;

public class Main {
	
	public static void main(String[] args) {
		SampleThread thread1 = new SampleThread();
		SampleThread thread2 = new SampleThread();
		thread1.start();
		thread2.start();
	}

}


class SampleThread extends Thread{
	public void run() {
		System.out.println("Thread running");
	}
}