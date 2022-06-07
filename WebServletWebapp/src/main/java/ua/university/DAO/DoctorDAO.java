package ua.university.DAO;

import lombok.extern.slf4j.Slf4j;
import ua.university.config.DataSourceConfig;
import ua.university.models.Doctor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DoctorDAO {
    private final Connection connection;

    public DoctorDAO() throws ClassNotFoundException, SQLException {
        connection = new DataSourceConfig().getConnection();
    }

    public void stop() throws SQLException {
        connection.close();
    }

    public List<Doctor> indexDoctor() throws SQLException {
        List<Doctor> doctorsList = new ArrayList<>();

        String sql = "SELECT * FROM doctors";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int type = resultSet.getInt("type");
                String name = resultSet.getString("name");

                doctorsList.add(new Doctor(id, type, name));
            }
            resultSet.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }

        return doctorsList;
    }

    public Doctor getDoctor(int id) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM doctors " +
                    "WHERE id=?");
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Doctor(resultSet.getInt("id"),
                        resultSet.getInt("type"),
                        resultSet.getString("name"));
            }

            return null;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public List<Doctor> getDoctor(String name) throws SQLException {
        List<Doctor> resultList = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM doctors " +
                    "WHERE name=?");
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                resultList.add(new Doctor(resultSet.getInt("id"),
                        resultSet.getInt("type"),
                        resultSet.getString("name")));
            }

            return resultList;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public int getMaxGlobalId() throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT max(id) FROM doctors");

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("max");
            }

            return -1;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public void saveDoctor(Doctor doctor) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO doctors " +
                    "(name, type) " +
                    "VALUES(?, ?)");
            connection.setAutoCommit(false);
            statement.setInt(2, doctor.getType());
            statement.setString(1, doctor.getName());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public void updateDoctor(int id, Doctor updatedDoctor) throws SQLException {
        String sql1 = "SELECT id FROM doctors WHERE id=?";
        String sql2 = "UPDATE doctors SET name=?, type=? WHERE id=?";

        try(PreparedStatement ps1 = connection.prepareStatement(sql1);
            PreparedStatement ps2 = connection.prepareStatement(sql2)) {
            connection.setAutoCommit(false);

            ps1.setInt(1, id);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps2.setInt(3, id);
            } else {
                String error = String.format("Could not find a doctor with id: %d to update", id);
                log.error(error);
                throw new SQLException(error);
            }

            ps2.setInt(2, updatedDoctor.getType());
            ps2.setString(1, updatedDoctor.getName());
            ps2.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public void deleteDoctor(int id) throws SQLException {
        String sql1 = "SELECT id FROM doctors WHERE id=?";
        String sql2 = "DELETE FROM doctors WHERE id=?";

        try(PreparedStatement ps1 = connection.prepareStatement(sql1);
            PreparedStatement ps2 = connection.prepareStatement(sql2)) {
            connection.setAutoCommit(false);

            ps1.setInt(1, id);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps2.setInt(1, id);
            } else {
                String error = String.format("Could not find a doctor with id: %d to delete", id);
                log.error(error);
                throw new SQLException(error);
            }

            ps2.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }
}
