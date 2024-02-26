import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Install PostgreSQL and configure it on your PC (https://www.postgresql.org/)");
        System.out.println("Press Enter to continue");
        scanner.nextLine();

        System.out.println("Put CSV file to the root directory of the program");
        System.out.println("Press Enter to continue");
        scanner.nextLine();

        System.out.println("Enter URL of your PostgreSQL database (jdbc:postgresql://localhost/postgres by default):");
        String url = scanner.nextLine();

        System.out.println("Enter your PostgreSQL database login:");
        String user = scanner.nextLine();

        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        scanner.close();

        //Reading CSV
        CSVReader reader = new CSVReader();
        String path = "PREFERENCE_DEFINITION.csv";
        reader.readCSV(path);
        List<String[]> rows = reader.getRows();
        String tableName = reader.getTableName();
        List<String> columnNames = reader.getColumns();

        //Database operations
        DatabaseManager dbManager = new DatabaseManager(url, user, password);
        dbManager.createTable(tableName, columnNames);
        for (String[] row : rows) {
            dbManager.insertData(tableName, columnNames, rows);
        }

        dbManager.closeConnection();
        System.out.println("Parsing of CSV file completed. Data was successfully copied to database");

    }
}
