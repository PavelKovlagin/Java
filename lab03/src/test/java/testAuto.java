import ru.jdbc.Auto;
import ru.jdbc.connection;
import ru.model.AutoModel;

public class testAuto {

    connection conn = new connection();
    Auto auto = new Auto(conn.getConnection());

    @org.junit.Test
    public void testAutoSELECT() {
        System.out.println("\nТестирование SELECT из таблицы Auto");
        auto.SELECT();
    }

    @org.junit.Test
    public void testAutoINSERT(){
        System.out.println("\nТестирование INSERT в Auto");
        auto.INSERT(new AutoModel(0,"Test","Test"));
    }

    @org.junit.Test
    public void testAutoDELETE(){
        System.out.println("\nТестироване DELETE из Auto");
        auto.DELETE(0);
    }
}
