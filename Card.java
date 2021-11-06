
import java.util.*;
import java.io.*;

/**
 *
 * @author usquare
 */

class Problem {

    static int mod = (int) (1e9+7);
    static InputReader in;
    static PrintWriter out;
    
    static class Treap {
    
        int y;
        int value;
        Treap l,r;
        boolean reversed;
        int size;

        public Treap(int y, int value,Treap l, Treap r){
            this.y = y;
            this.value = value;
            this.l = l;
            this.r = r;
            this.size = size(l) + size(r) + 1;
            this.reversed = false;
        }

        void fix(){
            if(reversed){
                reversed = false;
                if(l != null){
                    l.reversed ^= true;
                }
                if(r != null){
                    r.reversed ^= true;
                }
                Treap tmp = l;
                l = r;
                r = tmp;
            }
        }

        static int size(Treap t){
            return t == null ? 0 : t.size;
        }

        static Treap merge(Treap left, Treap right){
            if(left == null)
                return right;
            if(right == null)
                return left;
            left.fix();
            right.fix();
            if(left.y <= right.y){
                left.r = merge(left.r, right);
                left.size = size(left.l) + size(left.r) + 1;
                return left;
            }
            else{
                right.l = merge(left, right.l);
                right.size = size(right.l) + size(right.r) + 1;
                return right;
            }
        }

        static Treap splitL, splitR;

        static void splitRecursive(Treap t, int x){
            if(t == null){
                splitL = null;
                splitR = null;
                return;
            }
            t.fix();
            if(size(t.l) < x){
                splitRecursive(t.r, x - size(t.l) - 1);
                t.r = splitL;
                splitL = t;
            }
            else{
                splitRecursive(t.l, x);
                t.l = splitR;
                splitR = t;
            }
            t.size = size(t.l) + size(t.r) + 1;
        }

        static Random rng = new Random(1);

        static Treap build(int n){
            Stack<Treap> st = new Stack<>();
            Treap first = new Treap(rng.nextInt(), 1, null, null);
            st.add(first);
            Treap root = first;

            for(int i = 2; i <= n; i++){
                Treap cur = new Treap(rng.nextInt(), i, null, null);
                if(cur.y < root.y){
                    cur.l = root;
                    root = cur;
                    st.clear();
                }
                else{
                    while(st.peek().y > cur.y) st.pop();
                    Treap t = st.peek();
                    cur.l = t.r;
                    t.r = cur;
                }
                st.add(cur);
            }
            root.calcSize();
            return root;
        }

        void calcSize(){
            if(l != null)
                l.calcSize();
            if(r != null)
                r.calcSize();
            size = size(l) + size(r) + 1;
        }

        int[] toArray() {
            int[] array = new int[size(this)];
            toArray(array, 0);
            return array;
        }

        void toArray(int[] array, int off) {
            fix();
            if (l != null) {
                    l.toArray(array, off);
                    off += l.size;
            }
            array[off++] = value;
            if (r != null) {
                    r.toArray(array, off);
            }
        }
    }
 
    static void solve(){
        
        InputReader in = new InputReader(System.in);
        PrintWriter out = new PrintWriter(System.out);
        
        int n = in.nextInt();
        int m = in.nextInt();
        
        Treap deck = Treap.build(n);
        
        for(int i = 0; i < m; i++){
            int a = in.nextInt();
            int b = in.nextInt();
            int c = in.nextInt();
            
            Treap.splitRecursive(deck, a);
            Treap apart = Treap.splitL;
            deck = Treap.splitR;
            
            Treap.splitRecursive(deck, b);
            Treap bpart = Treap.splitL;
            deck = Treap.splitR;
            
            deck = Treap.merge(apart, deck);
            
            Treap.splitRecursive(deck, c);
            Treap cpart = Treap.splitL;
            deck = Treap.splitR;
            
            /*debug(apart.toArray());
            debug(bpart.toArray());
            debug(cpart.toArray());
            debug(deck.toArray());
            */
            if(b != 0)
                bpart.reversed ^= true;
            
            deck = Treap.merge(bpart, deck);
            deck = Treap.merge(cpart, deck);
        }
        
        int[] ans = deck.toArray();
        for(int i = 0; i < n; i++)
            out.print(ans[i] + " ");
        
        out.close();
    }
    
    public static void main(String[] args) {

        
        new Thread(null,new Runnable() {
            @Override
            public void run() {
                try{
                    solve();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        },"1",1<<26).start();
        
    }
    
    static class Pair implements Comparable<Pair>{

        int x,y;
        long c,k;
        
	Pair (int x,int y,long c){
		this.x=x;
		this.y=y;
		this.c=c;
	}

	Pair (int x,int y){
		this.x=x;
		this.y=y;
	}
        
	public int compareTo(Pair o) {
               return Integer.compare(this.x,o.x);
		//return 0;
	}

        public boolean equals(Object o) {
            if (o instanceof Pair) {
                Pair p = (Pair)o;
                return p.x == x && p.y == y;
            }
            return false;
        }

        @Override
        public String toString() {
            return x+" "+y+" "+c+" "+k;
        }
        
        public int hashCode() {
            return new Integer(x).hashCode() * 31 + new Integer(y).hashCode();
        }

    } 
    public static class Merge {
        
        public static void sort(int inputArr[]) {
            int length = inputArr.length;
            doMergeSort(inputArr,0, length - 1);
        }
        
        private static void doMergeSort(int[] arr,int lowerIndex, int higherIndex) {        
            if (lowerIndex < higherIndex) {
                int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
                doMergeSort(arr,lowerIndex, middle);
                doMergeSort(arr,middle + 1, higherIndex);
                mergeParts(arr,lowerIndex, middle, higherIndex);
            }
        }
        
        private static void mergeParts(int[]array,int lowerIndex, int middle, int higherIndex) {
            int[] temp=new int[higherIndex-lowerIndex+1];
            for (int i = lowerIndex; i <= higherIndex; i++) {
                temp[i-lowerIndex] = array[i];
            }
            int i = lowerIndex;
            int j = middle + 1;
            int k = lowerIndex;
            while (i <= middle && j <= higherIndex) {
                if (temp[i-lowerIndex] < temp[j-lowerIndex]) {
                    array[k] = temp[i-lowerIndex];
                    i++;
                } else {
                    array[k] = temp[j-lowerIndex];
                    j++;
                }
                k++;
            }
            while (i <= middle) {
                array[k] = temp[i-lowerIndex];
                k++;
                i++;
            }
            while(j<=higherIndex){
                array[k]=temp[j-lowerIndex];
                k++;
                j++;
            }
        }

    }
    
    static long[] shuffle(long[] a, Random gen){ 
        for(int i = 0, n = a.length;i < n;i++){ 
            int ind = gen.nextInt(n-i)+i; 
            long d = a[i]; 
            a[i] = a[ind]; 
            a[ind] = d; 
        } 
        return a; 
    }

        
    public static long add(long a,long b){
        long x=(a+b);
        while(x>=mod) x-=mod;
        return x;
    }

    public static long sub(long a,long b){
        long x=(a-b);
        while(x<0) x+=mod;
        return x;
    }
    
    public static long mul(long a,long b){
        a%=mod;
        b%=mod;
        long x=(a*b);
        return x%mod;
    }
    
    static boolean isPal(String s){
        for(int i=0, j=s.length()-1;i<=j;i++,j--){
                if(s.charAt(i)!=s.charAt(j)) return false;
        }
        return true;
    }
    static String rev(String s){
            StringBuilder sb=new StringBuilder(s);
            sb.reverse();
            return sb.toString();
    }

    static long gcd(long x,long y){
	if(y==0)
		return x;
	else
		return gcd(y,x%y);
    }
    
    static int gcd(int x,int y){
	if(y==0)
		return x;
	else 
		return gcd(y,x%y);
    }

    static long gcdExtended(long a,long b,long[] x){

        if(a==0){
            x[0]=0;
            x[1]=1;
            return b;
        }
        long[] y=new long[2];
        long gcd=gcdExtended(b%a, a, y);

        x[0]=y[1]-(b/a)*y[0];
        x[1]=y[0];

        return gcd;
    }

    static int abs(int a,int b){
    return (int)Math.abs(a-b);
    }

    static long abs(long a,long b){
    return (long)Math.abs(a-b);
    }

    static int max(int a,int b){
    if(a>b)
            return a;
    else
            return b;
    }

    static int min(int a,int b){
    if(a>b)
            return b;
    else 
            return a;
    }

    static long max(long a,long b){
    if(a>b)
            return a;
    else
            return b;
    }

    static long min(long a,long b){
    if(a>b)
            return b;
    else 
            return a;
    }


    public static long pow(long n,long p,long m){
	 long  result = 1;
	  if(p==0)
	    return 1;
          
	while(p!=0)
	{
	    if(p%2==1)
	        result *= n;
	    if(result>=m)
	    result%=m;
	    p >>=1;
	    n*=n;
	    if(n>=m)
	    n%=m;
	}
	return result;
    }
    
    public static long pow(long n,long p){
	long  result = 1;
	  if(p==0)
	    return 1;
          
	while(p!=0)
	{
	    if(p%2==1)
	        result *= n;	    
	    p >>=1;
	    n*=n;	    
	}
	return result;
    }
    
     static void debug(Object... o) {
            System.out.println(Arrays.deepToString(o));
    }

    static class InputReader {

            private final InputStream stream;
            private final byte[] buf = new byte[8192];
            private int curChar, snumChars;
            private SpaceCharFilter filter;

            InputReader(InputStream stream) {
                    this.stream = stream;
            }

            int snext() {
                    if (snumChars == -1)
                            throw new InputMismatchException();
                    if (curChar >= snumChars) {
                            curChar = 0;
                            try {
                                    snumChars = stream.read(buf);
                            } catch (IOException e) {
                                    throw new InputMismatchException();
                            }
                            if (snumChars <= 0)
                                    return -1;
                    }
                    return buf[curChar++];
            }

            int nextInt() {
                    int c = snext();
                    while (isSpaceChar(c)) {
                            c = snext();
                    }
                    int sgn = 1;
                    if (c == '-') {
                            sgn = -1;
                            c = snext();
                    }
                    int res = 0;
                    do {
                            if (c < '0' || c > '9')
                                    throw new InputMismatchException();
                            res *= 10;
                            res += c - '0';
                            c = snext();
                    } while (!isSpaceChar(c));
                    return res * sgn;
            }

            long nextLong() {
                    int c = snext();
                    while (isSpaceChar(c)) {
                            c = snext();
                    }
                    int sgn = 1;
                    if (c == '-') {
                            sgn = -1;
                            c = snext();
                    }
                    long res = 0;
                    do {
                            if (c < '0' || c > '9')
                                    throw new InputMismatchException();
                            res *= 10;
                            res += c - '0';
                            c = snext();
                    } while (!isSpaceChar(c));
                    return res * sgn;
            }

            int[] nextIntArray(int n) {
                    int a[] = new int[n];
                    for (int i = 0; i < n; i++) {
                            a[i] = nextInt();
                    }
                    return a;
            }

            long[] nextLongArray(int n) {
                    long a[] = new long[n];
                    for (int i = 0; i < n; i++) {
                            a[i] = nextLong();
                    }
                    return a;
            }
            
            String readString() {
                    int c = snext();
                    while (isSpaceChar(c)) {
                            c = snext();
                    }
                    StringBuilder res = new StringBuilder();
                    do {
                            res.appendCodePoint(c);
                            c = snext();
                    } while (!isSpaceChar(c));
                    return res.toString();
            }

            String nextLine() {
                    int c = snext();
                    while (isSpaceChar(c))
                            c = snext();
                    StringBuilder res = new StringBuilder();
                    do {
                            res.appendCodePoint(c);
                            c = snext();
                    } while (!isEndOfLine(c));
                    return res.toString();
            }

            boolean isSpaceChar(int c) {
                    if (filter != null)
                            return filter.isSpaceChar(c);
                    return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
            }

            private boolean isEndOfLine(int c) {
                    return c == '\n' || c == '\r' || c == -1;
            }

            interface SpaceCharFilter {
                    boolean isSpaceChar(int ch);
            }
    }
}    

