public class Shape {
    int nVertices;
    int[] perm;

    public Shape(int nVertices) {
        this.nVertices = nVertices;
        perm = new int[nVertices];
        /*set n-gon vertices with 0 indexing*/
        for(int i=0; i<nVertices; i++){
            perm[i] = i;
        }
    }
    public Shape(Shape s) {
        this.nVertices = s.nVertices;
        this.perm = s.perm.clone();
    }

    public void flipOnV(int n) {
        int v1 = (n + 1 + nVertices) % nVertices;
        int v2 = (n -1 + nVertices) % nVertices;
        swap(v1, v2);
    }

    public void flipOnE(int a, int b){
        int v1, v2;
        /*force v1 to be left of v2 */
        if(a > b || a == 0) {
            v1 = a;
            v2 = b;
        } else {
            v1 = b;
            v2 = a;
        }
        swap(v1,v2);
    }

    /*go left from v1 and right from v2, swapping each corresponding element with the other
    nVertices/2 times*/
    private void swap(int v1, int v2) {
        for(int i = 1; i <= nVertices/2; i++){
            int ph = perm [(v1 + nVertices) % nVertices];
            perm[((v1++) + nVertices) % nVertices] = perm[(v2 + nVertices) % nVertices];
            perm[((v2--) + nVertices) % nVertices] = ph;
        }
    }

    public void rotate(int n){
        int rotateNum = 0;
        /*if n is greater than or equal to number of vertices, make rotateNum = n mod nVertices*/
        if(n >= nVertices){
            rotateNum = (n % nVertices) % nVertices;
        }
        while (rotateNum < n) {
            int i, temp;
            temp = perm[0];
            for(i = 0; i < perm.length - 1; i++) {
                perm[i] = perm[i + 1];
            }
            perm[i] = temp;
            rotateNum++;
        }
    }
}