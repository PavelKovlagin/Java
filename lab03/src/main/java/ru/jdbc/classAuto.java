package ru.jdbc;

import com.sun.istack.internal.NotNull;
import ru.model.classAutoModel;

import java.sql.*;

public class classAuto implements DAO<classAutoModel> {

    @NotNull
    private final Connection connection;

    public classAuto(Connection connection)
    {
        this.connection = connection;
    }

    @Override
    public int INSERT(@NotNull classAutoModel classAuto) {

        int id = 0;
        try (PreparedStatement statement = connection.prepareStatement(SQLclassAuto.INSERT.QUERY, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, classAuto.getClassName());
            statement.setString(2, classAuto.getPrim());
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public classAutoModel SELECT() {
        final classAutoModel result = new classAutoModel();

        try (PreparedStatement statement = connection.prepareStatement(SQLclassAuto.SELECT.QUERY))
        {
            final ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                result.setId_class(rs.getInt(1));
                result.setClassName(rs.getString(2));
                result.setPrim(rs.getString(3));
                System.out.println(result.toString());
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean UPDATE(classAutoModel model) {
        return false;
    }

    @Override
    public boolean DELETE(int i) {
        boolean result = false;

        try (PreparedStatement statement = connection.prepareStatement(SQLclassAuto.DELETE.QUERY))
        {
            statement.setInt(1, i);
            result = statement.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    enum SQLclassAuto {
        SELECT("SELECT id_class, className, prim FROM classAuto"),
        INSERT("INSERT INTO classAuto (className, prim) VALUES(?, ?)"),
        DELETE("DELETE FROM classAuto WHERE id_class = ?");

        String  QUERY;

        SQLclassAuto(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
