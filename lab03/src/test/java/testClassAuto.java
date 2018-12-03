import ru.jdbc.classAuto;
import ru.jdbc.connection;
import ru.model.classAutoModel;

public class testClassAuto {

    connection conn = new connection();
    classAuto classAuto = new classAuto(conn.getConnection());

    @org.junit.Test
    public void testClassAutoSELECT()
    {
        System.out.println("\nТестирование SELECT из таблицы classAuto");
        classAuto.SELECT();
    }

    @org.junit.Test
    public void testClassAutoDELETE()
    {
        System.out.println("\nТестирование DLEETE из classAuto");
        classAuto.DELETE(0);
    }

    @org.junit.Test
    public void testClassAutoINSERT()
    {
        System.out.println("\nТестирование INSERT в classAuto");
        classAuto.DELETE(classAuto.INSERT(new classAutoModel("Test","Test")));
    }
}
