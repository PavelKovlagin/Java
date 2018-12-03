package ru.jdbc;

import com.sun.istack.internal.NotNull;

import java.sql.*;

import ru.model.AutoModel;

public class Auto implements DAO<AutoModel>  {

    @NotNull
    private final Connection connection;

    public Auto(final Connection connection)
    {
        this.connection = connection;
    }

    @Override
    public int INSERT(@NotNull AutoModel autoModel)
    {
        int id = 0;
        try (PreparedStatement statement = connection.prepareStatement(SQLAuto.INSERT.QUERY, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setInt(1, autoModel.getId_classAuto());
            statement.setString(2, autoModel.getMarka());
            statement.setString(3, autoModel.getModel());
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
    public boolean DELETE(int i)
    {
        boolean result = false;

        try (PreparedStatement statement = connection.prepareStatement(SQLAuto.DELETE.QUERY))
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

    @Override
    public AutoModel SELECT()
    {
        final AutoModel result = new AutoModel();

        try (PreparedStatement statement = connection.prepareStatement(SQLAuto.SELECT.QUERY))
        {
            final ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                result.setId_auto(rs.getInt("id_auto"));
                result.setId_classAuto(rs.getInt("id_class"));
                result.setClassAuto(rs.getString("className"));
                result.setMarka(rs.getString("marka"));
                result.setModel(rs.getString("model"));
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
    public boolean UPDATE(AutoModel model) {
        return false;
    }


    enum SQLAuto {
        SELECT("SELECT id_auto, auto.id_class, className, marka, model FROM classAuto, auto WHERE classAuto.id_class = auto.id_class"),
        INSERT("INSERT INTO auto (id_class, marka, model) VALUES(?, ?, ?)"),
        DELETE("DELETE FROM auto WHERE id_auto = ?");

        String  QUERY;

        SQLAuto(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
