package TIMS.TIMS;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;




public class excel {
	public static void createexcel()
	{
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sathish");
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        data.put("1", new Object[] {"TIMSID", "descriptopm", "status"});
        data.put("2", new Object[] {"Ttv1111c", "sat", "pass"});
        data.put("3", new Object[] {"Ttv1112c", "sat1", "fail"});
        data.put("4", new Object[] {"Ttv1113c", "sat2", "blocked"});
        data.put("5", new Object[] {"Ttv1114c", "sat3", "skipped"});
          
        //Iterate over data and write to sheet
        Set<String> keyset = data.keySet();
        int rownum = 0;
        for (String key : keyset)
        {
            Row row = sheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr)
            {
               Cell cell = row.createCell(cellnum++);
               if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Integer)
                    cell.setCellValue((Integer)obj);
            }
        }
        try
        {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("write.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("write.xlsx file created");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}

	
	public static  ArrayList<String> readexcel()
	{ ArrayList<String> re1 = new ArrayList<String>(); 
		
	 try
    {
		
		 String tcid = "";
        FileInputStream file = new FileInputStream(new File("write.xlsx"));

        //Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        //Get first/desired sheet from the workbook
        XSSFSheet sheet = workbook.getSheetAt(0);

        //Iterate through each rows one by one
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext())
        {
            Row row = rowIterator.next();
            //For each row, iterate through all the columns
            Iterator<Cell> cellIterator = row.cellIterator();
             
            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();
                //Check the cell type and format accordingly
                switch (cell.getCellType())
                {
                    case Cell.CELL_TYPE_NUMERIC:
                        System.out.print(cell.getNumericCellValue() + "t");
                        break;
                    case Cell.CELL_TYPE_STRING:
                    	tcid=tcid+cell.getStringCellValue()+"~";
                        //System.out.print(tcid + "t");
                        break;
                }
            }
           // System.out.println("");
            if (!tcid.isEmpty())
            {
            re1.add(tcid);
            tcid="";
            }
        }
        file.close();
        System.out.println("Excel read value:");
        for (String k:re1)
        {
        	System.out.println(k);
        }
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
	return re1;

	}
}
