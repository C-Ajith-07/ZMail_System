package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;

public class SampleSpliterator {
	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
//		list.add("A");
//		list.add("J");
//		list.add("I");
//		list.add("T");
//		list.add("K");
//		list.add("U");
//		list.add("M");
//		list.add("A");
//		list.add("R");
//		
		Spliterator<String> nameList = list.spliterator();
		Spliterator<String> subList = nameList.trySplit();
		Spliterator<String> subList1 = nameList.trySplit();
		
		
		subList.forEachRemaining((ele)->{
			System.out.println(ele);
		});
		
		subList1.forEachRemaining((ele)->{
			System.out.println(ele);
		});
		
		System.out.println(nameList.tryAdvance((ele)->{
			System.out.println("hii");
		}));
		
	

		nameList.forEachRemaining((ele)->{
			System.out.println(ele);
		});
	}
}
