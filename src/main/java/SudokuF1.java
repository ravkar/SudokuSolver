
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *  Sudoku
 *  @author KARAV
 *  Created: Oct 18, 2006 9:58:19 PM
 */
public class SudokuF1 implements Cloneable{

    static final int SIZE = 9;

    static volatile long m_solutionCounter;

    Pair m_emptyRowSize = new CellID(0,0);

    static final Set m_fullSet = new HashSet(Arrays.asList(new String[]{"1","2","3","4","5","6","7","8","9"}));

    List[] m_rows = new List[SIZE];
    List[] m_columns = new List[SIZE];
    List[] m_boxes = new List[SIZE];
    SortedSet[] m_emptyInfos = new SortedSet[SIZE];



    static volatile long m_startTime;

    public SudokuF1(String[][] anInput){
        for (int i = 0; i < anInput.length; i++) {
              m_rows[i] = Arrays.asList(anInput[i]);
              m_emptyInfos[i] = new TreeSet();
              String[] ints = anInput[i];
                    for (int j = 0; j < ints.length; j++) {
                        String anInt = ints[j];
                        if (m_columns[j] == null) m_columns[j] = new ArrayList();
                        m_columns[j].add(anInt);

                        int bi = (i/3)*3 + j/3;
                        if (m_boxes[bi] == null) m_boxes[bi] = new ArrayList();
                        m_boxes[bi].add(anInt);
                        if (anInt.equals("0")){
                            m_emptyInfos[i].add(Integer.toString(j));
                        }
                    }
          }   
      System.out.print("input rows->" + Arrays.asList(m_rows));
      System.out.println("input cols->" + Arrays.asList(m_columns));
      System.out.println("input boxes->" + Arrays.asList(m_boxes));
      System.out.println("infos->" + Arrays.asList(m_emptyInfos));        
        
    }

    SudokuF1(List[] aRows, List[] aCols, List[] aBoxes, SortedSet[] anEmptyInfos){
        List[] clonedRows = new List[SIZE];
        List[] clonedColumns = new List[SIZE];
        List[] clonedBoxes = new List[SIZE];

        SortedSet[] clonedEmptyInfos = new SortedSet[SIZE];
        for (int i = 0; i < aRows.length; i++) {
            List row = aRows[i];
            clonedRows[i] = new ArrayList(row);
        }
        for (int i = 0; i < aCols.length; i++) {
            List col = aCols[i];
            clonedColumns[i] = new ArrayList(col);
        }
        for (int i = 0; i < aBoxes.length; i++) {
            List box = aBoxes[i];
            clonedBoxes[i] = new ArrayList(box);
        }
        for (int i = 0; i < anEmptyInfos.length; i++) {
            SortedSet emptyInfo = anEmptyInfos[i];
            clonedEmptyInfos[i] = new TreeSet(emptyInfo);
        }

        m_rows = clonedRows;
        m_columns = clonedColumns;
        m_boxes = clonedBoxes;
        m_emptyInfos = clonedEmptyInfos;
    }

    void startStoper(){
      m_startTime = System.currentTimeMillis();
    }

    void cachIntermediateTime(){
        System.out.println("1st solution time: " + (System.currentTimeMillis() - m_startTime));
    }

    void stopStoper(){
        System.out.println("Total calculation time: " + (System.currentTimeMillis() - m_startTime));
    }

    public Object clone(){
        return new SudokuF1(m_rows, m_columns, m_boxes, m_emptyInfos);
    }

    private Pair getBestEmptyRowID(){
        SortedMap <Integer,Integer> map  = new TreeMap<Integer,Integer>();
        for (int i = 0; i < m_emptyInfos.length; i++) {
           SortedSet sortedSet = m_emptyInfos[i];
           if (sortedSet.size() > 0){
             map.put(sortedSet.size(), i);
           }
       }
        if (map.isEmpty()){
            return null;
        }       
        return new CellID(map.get(map.firstKey()), map.firstKey()); 
    }

    public int getBoxID(CellID aCellID){
        return (aCellID.getX()/3)*3 + aCellID.getY()/3;
    }

    public int getBoxElement(CellID aCellID){
        return (aCellID.getX()%3)*3 + aCellID.getY()%3;
    }

    public Set getMachingSet(CellID aCell){
        if (aCell == null){
            return null;
        }
        Set rowSet = new HashSet(m_rows[aCell.getX()]);
        Set columnSet = new HashSet(m_columns[aCell.getY()]);
        Set boxSet =  new HashSet(m_boxes[getBoxID(aCell)]);
        Set machingSet = new HashSet(m_fullSet);
        machingSet.removeAll(rowSet);
        machingSet.removeAll(columnSet);
        machingSet.removeAll(boxSet);
        return machingSet;
    }

    public void putValueToEmptyCell(CellID aCellID, String aValue){
        m_rows[aCellID.getX()].set(aCellID.getY(), aValue);
        m_columns[aCellID.getY()].set(aCellID.getX(), aValue);
        m_boxes[getBoxID(aCellID)].set(getBoxElement(aCellID), aValue);
        m_emptyInfos[aCellID.getX()].remove( m_emptyInfos[aCellID.getX()].first() );
        startSearching();
    }

    public CellID getEmptyCellID(){
    	 Pair x = null;
    	 if (m_emptyRowSize == null || m_emptyRowSize.getY() == 0 ){
    	   x = getBestEmptyRowID();           
    	 }
    	
        if (x == null){
        	m_emptyRowSize = null;
            return null;
        }
        m_emptyRowSize = x;
        m_emptyRowSize.setY(m_emptyRowSize.getY() - 1);
        int y = Integer.parseInt(((String)m_emptyInfos[x.getX()].first()));
        return new CellID(x.getX(),y);
    }

    public void startSearching(){
        final CellID emptyCell = getEmptyCellID();
        Set set = getMachingSet(emptyCell);
        if (emptyCell == null ){
            if (m_solutionCounter++ == 0){
                System.out.println("1st solution:");               
                System.out.println(Arrays.asList(m_rows));
                cachIntermediateTime();                 
            }
            return;
        }
        else if (set == null && emptyCell != null){
            //bad solution
            return;
        }

        for (Iterator iterator = set.iterator(); iterator.hasNext();) {
           final String goodElement = (String) iterator.next();
            //new thread here 
           final  SudokuF1 clonedSudok = (SudokuF1)this.clone();
           clonedSudok.putValueToEmptyCell(emptyCell, goodElement);            
        }
    }
    
    public long getSolutionCounter(){
    	return m_solutionCounter;
    }

    class CellID implements Pair{
            int x;
            int y;
        public CellID(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
        
        
        public void setX(int x) {
			this.x = x;
		}

		public void setY(int y) {
			this.y = y;
		}

		public String toString(){
           return "x->" + x + ";y->"+ y;
        }
    };
    
    interface Pair{
    	 public int getX();          
         public int getY();
         
 		 public void setX(int x);
         public void setY(int y);         
    };   
}
