
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author admin
 */
public class DiffieHellmanFinal {
            public static void main(String[] args) {
    
            SecureRandom secureRandom;
            Scanner scanner;
            int choice;
            BigInteger p = null,g;     
            BigInteger Xa,Xb;   //private key
            BigInteger Ya,Yb;   //public key
            BigInteger K;       //shared secret key
            int bitLength = 16; // 128 bits

            secureRandom = new SecureRandom();
            scanner = new Scanner(System.in);
            
            //ask user for choice
            System.out.println("Enter prime no.(p)");
            System.out.println("Press 1 for using Computer generated No.");
            System.out.println("Press 2 for Manually enter No. ");
            choice = scanner.nextInt();
            switch(choice)
            {
                case 1:p = BigInteger.probablePrime(bitLength, secureRandom);                       
                    break;
                case 2:p = scanner.nextBigInteger();
                    //if(!check_prime(p))
                    if(!p.isProbablePrime(bitLength))
                    {
                        System.out.println("Not a prime no.");
                        System.out.println("Using next probable prime");
                        p = p.nextProbablePrime();
                    }
            }            
            System.out.println("p="+p);                      

            //primality testing
/*
            if(!check_prime(p))
                    System.out.println("\tresult - Not a prime number");
            else
            {
                System.out.println("\tresult - Prime No.");
*/
                //find primitive root
                System.out.println("\nFinding primitive root . . .");
                g = primitive_root(p);
                
                System.out.println("\np="+p);                      
                System.out.println("g="+g);        
                
                //Private key for user A
                System.out.print("Enter private key(<p) for user A . . .");
                Xa = scanner.nextBigInteger();                
//                Xa = check_and_correct_private_key(Xa,p);            
                
                //Private key for user A
                System.out.print("Enter private key(<p) for user B . . .");
                Xb = scanner.nextBigInteger();                
//                Xb = check_and_correct_private_key(Xb,p);            
                
                System.out.println("\np="+p);                      
                System.out.println("g="+g);        

                //display private key
                System.out.println("Private keys . . .");
                System.out.println("Xa = "+Xa);
                System.out.println("Xb = "+Xb);
                
                //Calculating public key
                Ya = g.modPow(Xa,p);
                Yb = g.modPow(Xb,p);
                
                System.out.println("Public keys . . .");
                System.out.println("Ya = "+Ya);
                System.out.println("Yb = "+Yb);
                
                //Calculating shared secret key
                K = Ya.modPow(Xb,p);
                
                System.out.println("Shared Secret key . . .");
                System.out.println("K = "+K);
//              }
            }
        
    public static boolean check_prime(BigInteger p)
    {
        System.out.print("\tPerforming primality testing . . .");
        
        boolean flag = true;
        BigInteger bi,sqrtp;
        
        sqrtp = newton_raphson(p);
        sqrtp = sqrtp.add(BigInteger.ONE);
        
        for (bi = BigInteger.valueOf(2);
                bi.compareTo(sqrtp) <= 0;   
                ) {      
                
                if(p.mod(bi).equals(BigInteger.ZERO))
                {
                    flag = false;
                    break;
                }
                bi = bi.nextProbablePrime();
        }    
        System.out.println("\tCompleted");
        return flag;        
    }
         
    public static BigInteger newton_raphson(BigInteger N)
    {
        BigInteger n1,n2;
        if(N.equals(BigInteger.ZERO))
            return BigInteger.ZERO;
        n1 = N.shiftRight(1).add(BigInteger.ONE);
        n2 = n1.add(N.divide(n1));
        n2 = n2.shiftRight(1);
    
        while(n2.compareTo(n1)==(-1))
        {
            n1 = n2;
            n2 = n1.add(N.divide(n1));
            n2 = n2.shiftRight(1);
         }
        return n1;
    }

    public static BigInteger primitive_root(BigInteger p)
    {
        BigInteger g;
        boolean flag=true;
        List<String> al = new ArrayList<>();
        
        g = new BigInteger("2");        
        while(flag)
        {
            System.out.println("For g="+g);
            System.out.println("\tgenerating all powers . . .");
            for (BigInteger bi = BigInteger.valueOf(1);
                    bi.compareTo(p.subtract(BigInteger.ONE)) <= 0;   
                    bi = bi.add(BigInteger.ONE)) {      

                al.add(g.modPow(bi, p)+"");                
            }

            System.out.println("\tchecking all elements are generated or not ??");
            for (BigInteger bj = BigInteger.valueOf(1);
                    bj.compareTo(p.subtract(BigInteger.ONE)) <= 0;   
                    bj = bj.add(BigInteger.ONE)) {      
                if(al.contains(bj+"")){}
                else {
                    g = g.add(BigInteger.ONE);                    
                    flag = false;
                    break;
                }                
            }    

            if(flag==false)
            {
                flag=true;
                al.removeAll(al);
                System.out.println("\tresult = NO\n\n");
            }
            else 
            {
                System.out.println("\tresult = YES");
                break;
            }
        }
        return g;
    }
    public static BigInteger check_and_correct_private_key(BigInteger X, BigInteger p)
    {
        Scanner scanner = new Scanner(System.in);

        //CompareTO() method returns -1, 0 or 1 as this BigInteger is numerically less than, equal to, or greater than val.
        while(!(X.compareTo(p)==(-1)))
            {
                System.out.println("You have entered private key >= p . . .");                
                System.out.print("\nEnter private key(<p) for user A again. . .");
                X = scanner.nextBigInteger();
            }
            
            while(!check_prime(X)) {
                System.out.println("You have entered non-prime number . . .");                
                System.out.print("\nEnter private key(<p) for user A again. . .");
                X = scanner.nextBigInteger();        
            }                        
        return X;
    }
}