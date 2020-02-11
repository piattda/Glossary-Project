import static org.junit.Assert.*;

import org.junit.Test;

import components.queue.Queue;
import components.queue.Queue1L;

public class testingGlossary {

//getGlossMap
	//these fail and I don't know why. 
	
@Test
public void getGlossMap01(){
	String fileName = "data/test1.txt";
	Queue<String> orderedList = new Queue1L();
	GlossaryProject.getGlossMap(fileName, orderedList);
	Queue<String> toAssertEqual = new Queue1L();
	toAssertEqual.enqueue("I");
	toAssertEqual.enqueue("You");
	assertEquals(toAssertEqual,orderedList);
}

@Test
public void getGlossMap02(){
	String fileName = "data/test1.txt";
	Queue<String> orderedList = new Queue1L();
	GlossaryProject.getGlossMap(fileName, orderedList);
	Queue<String> toAssertEqual = new Queue1L();
	toAssertEqual.enqueue("He");
	toAssertEqual.enqueue("She");
	assertEquals(toAssertEqual,orderedList);
}
	
//makeIndexHTML
	//This outputs information and I don't know
	//how to test it.

//generateElements
	//might need test case
	//but I dobut it and am not sure how to do it. 



//nextWordOrSeparator
	//I have no idea how to test this.

//output header
	//I don't know how to test this. sorry. 
	
//supporting HTMLFiles
	//I have no idea how to test this. 
	

}
