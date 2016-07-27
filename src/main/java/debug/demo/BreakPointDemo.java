package debug.demo;

import java.util.Random;

public class BreakPointDemo {

	Random random = new Random();
	
	int value = -1;
	
	public BreakPointDemo(){
		
	}
	
	@SuppressWarnings("unused")
	public BreakPointDemo( int i, long l ){
		
		int ii = i;
		
		long ll = l;
		
	}
	
	void setValue( int count ){
		
		System.out.println("entering setValue method....");
		
		for( int i = 0; i < count; i++ ){
			
			value = random.nextInt(10);
			
		}
		
		System.out.println("leaving setValue method....");
		
	}
	
	void printValue( int count ){
		
		setValue( count );
		
		if( value % 3 == 0 ){
			
			throw new IllegalArgumentException("value is illegal");
			
		}
		
		System.out.println( value );
		
	}
	
	public static void main( String[] args ){
		
		BreakPointDemo bp = new BreakPointDemo();
		
		bp.printValue( 10 );
		
		// for testing the <step into selection>
		bp = new BreakPointDemo( new Integer(10), new Long(11) );
		
		
		
	}
	
	
}
