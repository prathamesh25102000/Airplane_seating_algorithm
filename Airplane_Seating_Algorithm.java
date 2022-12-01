// given condition in problem :
// 1. fill seats from
//   a. from left to right
//   b. go from front to back
// 2. first fill aisle then window and the center

// assumptions I made-
// if there is only 1 block of seats given - we will consider that aisle is on the right end of row
// and only leftmost column will be window
// At max 2 window columns can be present (on both ends of the rows)
// here I have followed 0-based indexing on rows and columns
// I have considered a seat as window if the same seat is both an aisle and a window seat

// handling boundary conditions-
// if there are no seats available - we will get "no_seat" in return
// if there isn't any middle column present that case will be automatically handled


import java.util.*;
import java.lang.*;
import java.io.*;


// CODE -

// created a class Column where each object will store the characterstics of a column
// which are its type, limit, list of passengers

class Column{
        String type = "";
        int limit = 0;
        ArrayList<Integer>list=new ArrayList<Integer>(); 
}


class Solution 
{
    // Creating queues based on categories ( aisle / window / center )
    // every element of queue will store the < index_of_column >
    // so that we will have the index of the column to be filled always at the peek of queue
    // and only those columns with available seats will be present in the queue

    static Queue<Integer>aisle=new LinkedList<>();
    static Queue<Integer>center=new LinkedList<>();
    static Queue<Integer>window=new LinkedList<>();
    
	public static void main (String[] args) throws java.lang.Exception
	{
		Scanner sc=new Scanner(System.in);

        // take input of number of blocks of seats
		int num_of_blocks=sc.nextInt();

        // store info of blocks
		ArrayList<ArrayList<Integer>> blocks = new ArrayList<ArrayList<Integer>>();
		
		int total_rows = 0;
        int total_cols = 0;
        for(int i=0; i<num_of_blocks; i++)
       {
        int col, row;
        col=sc.nextInt();
        row=sc.nextInt();
        total_cols += col;
        total_rows += row;
        ArrayList<Integer>input=new ArrayList<Integer>();
        input.add(col);
        input.add(row);
        blocks.add(input);
      }

        // creating a 2d array to store details of columns
      Column columns[]=new Column[total_cols];
      
      for(int i=0;i<total_cols;i++){
          columns[i]=new Column();
      }

        // index to point the front or peek of a block and use it to categorize
       int index = 0;

        // differentiating the columns based on their types (aisle / window / center)
        // from the blocks of seats provided and adding them to respective queues

        // processing blocks one by one
      for(int i=0; i<num_of_blocks; i++)
      {
        int cols = blocks.get(i).get(0);
        int rows = blocks.get(i).get(1);

        // window column of first block
        if(i==0)
        {
            window.add(0);
            columns[0].type = "window";
            columns[0].limit = rows;
        }

        // window column of last block
        if(i==num_of_blocks-1 && i!=0)
        {
            window.add(total_cols-1);
            columns[total_cols-1].type = "window";
            columns[total_cols-1].limit = rows;
        }
        
        // aisle columns on the left of a block
        if(i!=0 && cols>1)
        {
            aisle.add(index);
            columns[index].type = "aisle";
            columns[index].limit = rows;
        }
        
        // aisle column on the right of a block
        if(cols>1)
        {
            if(i!=num_of_blocks - 1 || num_of_blocks==1)
            {
                aisle.add(index+cols-1);
                columns[index+cols-1].type = "aisle";
                columns[index+cols-1].limit = rows;
            }
        }
        
        // all columns of a block except the start and end will be the center ones
        for(int mid=index+1; mid<index+cols-1; mid++)
        {
            //System.out.println(mid);
            center.add(mid);
            columns[mid].type = "center";
            columns[mid].limit = rows;
        }
        
        // jumping index pointer to next block
        index+=cols;
    }

      // taking input of total passengers
    int total_pass=sc.nextInt();
    
    int pass_no[]=new int[total_pass];


    // giving them nos;
        // this loop can be used to take input and provide an id instead of sequenced numbers.
    for(int i=0; i<total_pass; i++)
    {

        pass_no[i] = i+1;
    }

    // allotting seats to the passengers
    System.out.println("Passenger no. "+"Seat position "+"Column no. "+"Row no.");
    for(int i=0; i<total_pass; i++)
    {
        // calling the function to fill seat one by one
        String res[] = fillSeat(pass_no[i], columns);
        
        String type = res[0];
        int col_num = Integer.parseInt(res[1])+1;
        int row_num = Integer.parseInt(res[2])+1;

        // printing allotted seat one by one
        System.out.println(pass_no[i]+" "+type+" "+col_num+" "+row_num);
    } 
    
    System.out.println("Seating in grid");
    
    for(int i=0; i<columns.length; i++)
    {   int z=i+1;
        System.out.print("Column "+z+" -> "+columns[i].type+" "+"and passengers are -> ");
        for(int j=0; j<columns[i].list.size(); j++)
            //cout<<columns[i].list[j]<<" ";
            System.out.print(columns[i].list.get(j)+" ");
        
        System.out.println();
    }    
	}

    // this function allots the seat to the passenger and update the list of particular column
    // where the seat is available
    public static int[] allotSeat(int val, Column columns[], Queue<Integer>q){
      int index = q.peek();
      int limit = columns[index].limit;
      int c[]=new int[2];
      q.poll();

      // allot the passenger the seat that comes in peek of queue;
      columns[index].list.add(val);
    
      int seat = columns[index].list.size()-1;

      // if there are still seats available in this column,
        // add it to the back of the queue (in order to maintain left -> right order)
	  if(columns[index].list.size() < limit)
           q.add(index);
           
	   
	   c[0]= index;
	   c[1]=seat;

        // return the allotted seat
	   return c;
	}


    // a function that finds out in which type of column a seat is available and
    // further allots a seat
    // returns [type of seat, index_of_column, index_of_row];
	public static String[] fillSeat(int val, Column columns[]){
    
     String b[]=new String[3];
     // allot seat based on rule  " aisle -> window -> middle "
    if(aisle.size()>0)
    {
        // allot seat if it is available in given type->aisle
        int p[] = allotSeat(val, columns, aisle);

        // store the type of seat and its index_of_column and its index_of_row in an array
        b[0]="aisle";
        b[1]=Integer.toString(p[0]);
        b[2]=Integer.toString(p[1]);
    }
    
    else if(window.size()>0)
    {
        int p[] = allotSeat(val, columns, window);
        
        b[0]="window";
        b[1]=Integer.toString(p[0]);
        b[2]=Integer.toString(p[1]);

    }
    
    else if(center.size()>0)
    {
        int p[] = allotSeat(val, columns, center);
        
        b[0]="center";
        b[1]=Integer.toString(p[0]);
        b[2]=Integer.toString(p[1]);
    
    }
    
    // case when there isn't any seat left
    else {
        b[0]="no_seat";
        b[1]="-1";
        b[2]="-1";
    }
    
    return b;
}

}





// TESTCASES

// 4
// 3 2
// 4 3
// 2 3
// 3 4
// 30

// 4
// 3 4
// 4 5
// 2 3
// 3 4
// 30

// 1
// 2 15
// 30

// 1
// 1 30
// 30

// 2
// 1 15
// 1 15
// 30

// 2
// 1 15
// 2 10
// 35

// 2
// 2 15
// 2 10
// 35
