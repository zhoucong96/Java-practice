import java.util.ArrayList;


public class ECE103 {
	private static boolean isPrime(int num) {
	    if (num == 2) return true;
	    if (modulus(num, 2) == 0) return false;
	    for (int i = 3; i * i <= num; i += 2) {
	        if (modulus(num, i) == 0) return false;
	    }
	    return true;
	}
	/*This isRight only checks whether the congruence result is 1. And we see for 341, it's true but 341 is not prime so this proves
	 that the Fermat's Little Theorem doesn't work inversely*/
	private static boolean isRight(int number){
		if(modulus((int)(Math.pow(2, number-1)),number) == 1) return true;
		else return false;
	}
	private static int modulus(int divisor,int divised){
		int result = divisor/divised;
		int resultf = divisor - result * divised;
		return resultf;
	}
	public static int squaring(int p){
		int powerofa = p-1;
		int power = 0;
		int modresult = 1;
		//To see what is the largest power and start to find power lower than it
		while ( powerofa > 1){
			powerofa = powerofa/2;
			power++;
		}
		int []squaringmod = new int [power+1];
		//The first result will definitely be 2modp = 2. i is the power of the power
		squaringmod[0] = 2;
		//Find all squaring modulus
		for (int i =1;i<=power;i++){
			squaringmod[i] = modulus((int)Math.pow(squaringmod[i-1], 2), p);
		}
		//Get the arraylist of the composition of the value power of the value'a'
		ArrayList<Integer> powers = powercombination(p-1);
		//Multiply all the corresponded modulus together to get the final result
		for (Integer pow : powers){
			modresult = modulus(squaringmod[pow]*modresult , p);
		}
		return modresult;
	}
	private static ArrayList<Integer> powercombination (int powerofa){
		ArrayList<Integer> powers = new ArrayList<Integer>();
		int decomppower;
		while (powerofa>0){
			decomppower = 0;
			while (Math.pow(2, decomppower+1) <= powerofa){
				decomppower++;
			}
			//Update to next largest power
			powerofa = powerofa-(int)Math.pow(2, decomppower);
			powers.add(decomppower);
		}
		return powers;
	}

	public static void main(String[] args){
		int p = 2;
		int mod;
		while(p <= 345)
		{
			mod = squaring(p);
			if(isPrime(p)) System.out.println(p +" "+"prime"+" "+ mod +" "+ isRight(p));
			else System.out.println(p +" "+ mod +" "+ isRight(p));
			p++;
		}
	}
}
