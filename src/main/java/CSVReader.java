import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    private List<String[]> records = new ArrayList<>();
    private String tableName;
    private List<String> columns = new ArrayList<>();

    public void readCSV(String filePath) {
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip the first line
            br.readLine();

            // Reading TableName
            String tableNameLine = br.readLine().trim();
            if (tableNameLine.startsWith("\"")) {
                tableNameLine = tableNameLine.substring(1);
            }
            if (tableNameLine != null && tableNameLine.startsWith("Table:")) {
                tableName = tableNameLine.split(";")[1].replace("\"", "").trim();
            } else {
                tableName = "default";
            }

            // Reading Columns names
            String columnsLine = br.readLine().trim().replace("\"", "");
            if (columnsLine.startsWith("Columns:")) {
                String[] columnNames = columnsLine.substring("Columns:".length() + 1).split(";");
                for (String columnName : columnNames) {
                    columns.add(columnName.replace('.', '_').trim());
                }
            }

            //Reading the parameters
            while ((line = br.readLine()) != null) {
                line = line.replace("; ", ", ").replace(" ;", " ,");
                line = line.replace("\"", "");
                if (line.startsWith(";")) {
                    line = line.substring(1);
                }
                String[] values = line.split(";", -1); //
                records.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> getRows() {
        return records;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getColumns() {
        return columns;
    }
}