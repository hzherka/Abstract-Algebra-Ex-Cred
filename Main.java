import java.util.*;

public class Main {
    public static void main(String[] args) {
        Shape myShape;
        int nVertices;
        Scanner scn = new Scanner(System.in);

        System.out.println("Welcome to your own permutation generator!\nA quick note:\nThe places of the vertices will change with the operation. You will see this change printed for you. If you choose to reflect on something, it will reflect on the index of your input (i.e. if you rotate once, then reflecting on 0 will reflect on the place 0 now is, not the 0th position of the shape)\n");
        nVertices = takeInt(scn);
        myShape = new Shape(nVertices);
        System.out.println("Your initial shape is represented as " + Arrays.toString(myShape.perm));

        takeOp(myShape, scn);
        permute(myShape);
        System.out.println("Goodbye!");
    }

    public static int takeInt(Scanner scn) {
        System.out.print("Enter the number of vertices you would like your shape to have (n>2): ");
        try {
            int tr = scn.nextInt();
            if(tr < 3) {
                throw new IllegalStateException();
            } else {return tr;}
        }
        catch(IllegalStateException|InputMismatchException e) {
            System.out.println("Hey, that wasn't an integer greater than 3!");
            return takeInt(scn);
        }
    } 

    public static void takeOp(Shape myShape, Scanner scn) {
        System.out.println("\nChoose an option by typing its number:\n1. Rotate\n2. Reflect on a vertex\n3. Reflect on an edge (you may only do so if your n-gon is even)\n4. Print my permutation\n5. Print and quit");
        try {
            int i = scn.nextInt();
            if(i == 1) {
                Shape old = new Shape(myShape);
                choseRotate(myShape, scn);
                System.out.println(Arrays.toString(old.perm) + " => " + Arrays.toString(myShape.perm));
                takeOp(myShape, scn);
            } else if(i == 2) {
                Shape old = new Shape(myShape);
                choseReflectV(myShape, scn);
                System.out.println(Arrays.toString(old.perm) + " => " + Arrays.toString(myShape.perm));
                takeOp(myShape, scn);
            } /*Else chose to reflect on edge.*/
            else if(i == 3) {
                Shape old = new Shape(myShape);
                choseReflectE(myShape, scn);
                System.out.println(Arrays.toString(old.perm) + " => " + Arrays.toString(myShape.perm));
                takeOp(myShape, scn);
            } else if(i == 4) {
                permute(myShape);
                takeOp(myShape, scn);
            }
            /*else if did not choose quit, throw exception*/
            else if(i != 5) {
                throw new IllegalStateException();
            }
        } catch (IllegalStateException e) {
            System.out.println("That wasn't one of the options. Try again.");
            takeOp(myShape, scn);
        } catch (InputMismatchException e) {
            System.out.println("That wasn't one of the options. Try again.");
            scn.next();
            takeOp(myShape, scn);
        }
    }

    public static void choseRotate(Shape myShape, Scanner scn) {
        try{
        System.out.print("Note: Rotate goes counterclockwise. Enter how many times you would like to rotate (n>0): ");
        int num = scn.nextInt();
        if(num >= 0){
        myShape.rotate(num);
        } else {
            throw new IllegalStateException();
        }
        } catch (InputMismatchException e) {
            System.out.println("Your input was not a positive integer. Try again.");
            scn.next();
            choseRotate(myShape, scn);
        } catch (IllegalStateException e) {
            System.out.println("Your input was not a positive integer. Try again.");
            choseRotate(myShape, scn);
        }
    }

    public static void choseReflectV(Shape myShape, Scanner scn) {
        try {
            System.out.print("Which vertex would you like to reflect across? (Vertices begin at 0) ");
            int ver = findIndex(myShape.perm, scn.nextInt());
            /*if chosen vertex is not in correct range go back to choose option*/
            if(ver < 0 || ver > myShape.nVertices - 1) {throw new IllegalStateException();}
            else{
                myShape.flipOnV(ver);
            }
        } catch (IllegalStateException e) {
            System.out.println("That was not a vertex on your shape. Try again.");
            choseReflectV(myShape, scn);
        } catch (InputMismatchException e) {
            System.out.println("That was not a vertex on your shape. Try again.");
            scn.next();
            choseReflectV(myShape, scn);
        }
    }

    public static void choseReflectE(Shape myShape, Scanner scn) {
        try{
        /*if n-gon does not have even number of vertices go back to choose an option*/
        if(myShape.nVertices%2 != 0){
            System.out.println("Your shape has an uneven number of vertices. You cannot reflect on an edge.");
            takeOp(myShape, scn);}
        else{
            System.out.print("On what edge would you like to reflect? (Type the numbers seperated by a space. They must be adjacent vertices.) ");
            int v1 = scn.nextInt();
            int v2 = scn.nextInt();
            System.out.println("v1: " + v1 + " ; 2: " + v2);
            int a = findIndex(myShape.perm, v1);
            int b = findIndex(myShape.perm, v2);
            System.out.println("a: " + a + " ; b: " + b);
            /*if vertices entered are not on n-gon throw exception*/
            if( v1 < 0 || v1 > myShape.nVertices || v2 < 0 || v2 > myShape.nVertices) {throw new IllegalStateException();}
            /*If vertices are not adjacent throw exception */
            else if(!(a == (b - 1 + myShape.nVertices) % myShape.nVertices || b == (a - 1 + myShape.nVertices) % myShape.nVertices)) {throw new IllegalStateException();}
            else{ myShape.flipOnE(a, b);}
            }
        } catch (IllegalStateException e) {
            System.out.println("That was not a vertex on your shape. Try again.");
            choseReflectE(myShape, scn);
        }  catch (InputMismatchException e) {
            System.out.println("That was not a vertex on your shape. Try again.");
            scn.next();
            choseReflectE(myShape, scn);
        }
    }

    /*Call permute_help will return each cycle, add to permutation list*/
    public static void permute(Shape myShape){
        ArrayList<ArrayList<Integer>> permutation = new ArrayList<>();
        ArrayList<Integer> used = new ArrayList<>();
        permuteHelp(myShape.perm, used, permutation);
        System.out.println(permutation);
    }

    /*This method will find one cycle and return it*/
    public static List<Integer> permuteHelp(int[] seq, ArrayList<Integer> used, ArrayList<ArrayList<Integer>> permutation) {
        ArrayList<Integer> curr = new ArrayList<>();
        int follow = 0;
        //if we have permuted everything return null
        if(seq.length == used.size()) {return null;}
        else{
        //Find first elem not already used to begin new cycle and add it to curr and used
        for(int i = 0; i < seq.length; i++) {
            if (!used.contains(seq[i])) {
                follow = i; 
                curr.add(follow); 
                used.add(follow); 
                break;
            }
        }
        //while we have not gotten back to the element we started at, add
        //element to used and curr arrays and follow it
        while (!curr.contains(seq[follow]) ) {
            curr.add(seq[follow]);
            used.add(seq[follow]);
            follow = seq[follow];
        }
        permutation.add(curr);
        return permuteHelp(seq, used, permutation);
        }
    }

    public static int findIndex(int[] arr, int t)
    {
 
        // if array is Null
        if (arr == null) {
            return -1;
        }
 
        // find length of array
        int len = arr.length;
        int i = 0;
 
        // traverse in the array
        while (i < len) {
 
            // if the i-th element is t
            // then return the index
            if (arr[i] == t) {
                return i;
            }
            else {
                i = i + 1;
            }
        }
        return -1;
    }
 
}
