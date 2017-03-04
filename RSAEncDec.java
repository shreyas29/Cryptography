import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

/**
 *
 * @Shreyas
 */
public class RSAEncDec {
    
    static BufferedReader br;
    static BufferedWriter bw;    
    static int blockSize;           

    public static void main(String[] args) throws FileNotFoundException, IOException {

        BigInteger bPlainText,p,q,N,phiN,d;
        BigInteger j;
        String bLine;
        Scanner sc;
        File inputFile,plainFile,cipherFile,decryptFile;
        
        SecureRandom secureRandom=new SecureRandom();
        sc = new Scanner(System.in);
        
        p = BigInteger.probablePrime(128,secureRandom );
        q = BigInteger.probablePrime(128,secureRandom);
        
        N = p.multiply(q);
        phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger e=new BigInteger("2");
        while(phiN.gcd(e).intValue()>1){
            e=e.add(BigInteger.ONE);
        }
        d = e.modInverse(phiN);
        
        int c;
        inputFile = new File("./input.txt");
        cipherFile = new File("./cipher.txt");
        decryptFile = new File("./decrypt.txt");
        
        System.out.println("Enter block size . . ");
        blockSize = sc.nextInt();
        
        plainFile = generatePlainText(inputFile);
        
        //encryption
        br = new BufferedReader(new FileReader(plainFile));
        bw = new BufferedWriter(new FileWriter(cipherFile));
        for(int i=0;i<(plainFile.length()/blockSize);i++)
        {
            j = new BigInteger(Integer.toString(blockSize-1));
            bPlainText = BigInteger.ZERO;
            while(j.compareTo(BigInteger.ZERO)>=0)
            {
                c=br.read();
                if(c>='a' && c<='z')
                    c -= 97;
                else if(c>='0' && c<='9')
                    c = c - 48 + 26;
                int t = (int) (Math.pow(36,j.intValue())*c);
                bPlainText = bPlainText.add(new BigInteger(Integer.toString(t)));
                j = j.subtract(BigInteger.ONE);
            }
            //System.out.println(bCipher);
            encrypt(bPlainText,e,N,cipherFile,bw);                        
        }        
        br.close();
        bw.close();
        
        //decryption
        br = new BufferedReader(new FileReader(cipherFile));
        bw = new BufferedWriter(new FileWriter(decryptFile));

        BigInteger C,P;
        while((bLine=br.readLine())!=null)
        {
            C = new BigInteger(bLine);
            P = C.modPow(d, N);
            
            j = new BigInteger(Integer.toString(blockSize-1));     
            
            char[] orgChar = new char[blockSize];
            while(j.compareTo(BigInteger.ZERO)>=0)
            {
                BigInteger t = new BigInteger("36");
                c=P.mod(t).intValue();
                P = P.divide(t);                
                if(c>=0 && c<=25)
                    c += 97;
                else if(c>=26 && c<=35)
                    c = c + 48 - 26;                
                orgChar[j.intValue()] = (char) c;
                j = j.subtract(BigInteger.ONE);
            }
            for(int i=0;i<blockSize;i++)
                bw.write(orgChar[i]);
        }
        br.close();
        bw.close();
    }   
    
    public static void encrypt(BigInteger P, BigInteger e, BigInteger n, File cipherFile, BufferedWriter bw)
    {
        try {
            BigInteger C = P.modPow(e, n);
            bw.write(C.toString()+"\r\n");
            bw.flush();
        } catch (IOException ex) {
            System.out.println("In encrypt :"+ex);
        }
    }
    
   public static File generatePlainText(File file)
   {
        File newFile = new File("./plain.txt");
        try {  
            br = new BufferedReader(new FileReader(file));
            bw = new BufferedWriter(new FileWriter(newFile));
            int c;
            while((c=br.read())!=(-1))
            {
                if((c>='A' && c<='Z')||(c>='a' && c<='z')||(c>='0' && c<='9'))
                {
                    if(Character.isUpperCase(c))
                        c = Character.toLowerCase(c);
                    bw.write(c);
                    bw.flush();
                }
            }
            int r = (int) (newFile.length() % blockSize);            
            if(r!= 0)
            {
                r = blockSize - r;
                while((r--)!=0)
                {
                    bw.write('x');
                    bw.flush();
                }
            }
            br.close();
            bw.close();
        } catch (FileNotFoundException ex) {
            System.out.println("In generatePlainText:"+ex);
        } catch (IOException ex) {
            System.out.println("In generatePlainText:"+ex);
        }
        return newFile;
   }
}
