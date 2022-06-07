package ua.university.DAO;

import lombok.extern.slf4j.Slf4j;
import ua.university.config.DataSourceConfig;
import ua.university.models.Prescription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PrescriptionDAO {
    private final Connection connection;
    private final PatientDAO patientDAO;
    private final DoctorDAO doctorDAO;

    public PrescriptionDAO() throws ClassNotFoundException, SQLException {
        connection = new DataSourceConfig().getConnection();
        patientDAO = new PatientDAO();
        doctorDAO = new DoctorDAO();
    }

    public void stop() throws SQLException {
        connection.close();
    }

    public List<Prescription> indexPrescription() throws SQLException {
        List<Prescription> prescriptionsList = new ArrayList<>();

        String sql = "SELECT * FROM prescriptions";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int patientId = resultSet.getInt("patient_id");
                int doctorId = resultSet.getInt("doctor_id");
                String diagnosis = resultSet.getString("diagnosis");
                int treatment = resultSet.getInt("treatment");

                prescriptionsList.add(new Prescription(id,
                        this.patientDAO.getPatient(patientId),
                        this.doctorDAO.getDoctor(doctorId),
                        diagnosis,
                        treatment));
            }
            resultSet.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }

        return prescriptionsList;
    }

    public Prescription getPrescription(int id) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM prescriptions " +
                    "WHERE id=?");
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Prescription(resultSet.getInt("id"),
                        this.patientDAO.getPatient(resultSet.getInt("patient_id")),
                        this.doctorDAO.getDoctor(resultSet.getInt("doctor_id")),
                        resultSet.getString("diagnosis"),
                        resultSet.getInt("treatment"));
            }

            return null;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public int getMaxGlobalId() throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT max(id) FROM prescriptions");

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

    public void savePrescription(Prescription prescription) throws SQLException {
        String sql1 = "SELECT id from patients WHERE id=?";
        String sql2 = "SELECT id from doctors WHERE id=?";
        String sql3 = "INSERT INTO prescriptions (patient_id, doctor_id, diagnosis, treatment) VALUES(?, ?, ?, ?)";

        try (PreparedStatement ps1 = connection.prepareStatement(sql1);
             PreparedStatement ps2 = connection.prepareStatement(sql2);
             PreparedStatement ps3 = connection.prepareStatement(sql3)) {
            connection.setAutoCommit(false);

            int patientId = prescription.getPatient().getId();
            ps1.setInt(1, patientId);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps3.setInt(1, rs1.getInt("id"));
            } else {
                String error = String.format("Could not find a patient with id: %d", patientId);
                log.error(error);
                throw new SQLException(error);
            }

            int doctorId = prescription.getDoctor().getId();
            ps2.setInt(1, doctorId);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                ps3.setInt(2, rs2.getInt("id"));
            } else {
                String error = String.format("Could not find a doctor with id: %d", doctorId);
                log.error(error);
                throw new SQLException(error);
            }

            ps3.setString(3, prescription.getDiagnosis());
            ps3.setInt(4, prescription.getTreatment());
            ps3.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public void updatePrescription(int id, Prescription updatedPrescription) throws SQLException {
        String sql1 = "SELECT id from patients WHERE id=?";
        String sql2 = "SELECT id from doctors WHERE id=?";
        String sql3 = "SELECT id from prescriptions WHERE id=?";
        String sql4 = "UPDATE prescriptions SET patient_id=?, doctor_id=?, diagnosis=?, treatment=? WHERE id=?";


        try (PreparedStatement ps1 = connection.prepareStatement(sql1);
             PreparedStatement ps2 = connection.prepareStatement(sql2);
             PreparedStatement ps3 = connection.prepareStatement(sql3);
             PreparedStatement ps4 = connection.prepareStatement(sql4)) {
            connection.setAutoCommit(false);

            int patientId = updatedPrescription.getPatient().getId();
            ps1.setInt(1, patientId);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps4.setInt(1, rs1.getInt("id"));
            } else {
                String error = String.format("Could not find a patient with id: %d", patientId);
                log.error(error);
                throw new SQLException(error);
            }

            int doctorId = updatedPrescription.getDoctor().getId();
            ps2.setInt(1, doctorId);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                ps4.setInt(2, rs2.getInt("id"));
            } else {
                String error = String.format("Could not find a doctor with id: %d", doctorId);
                log.error(error);
                throw new SQLException(error);
            }

            ps3.setInt(1, id);
            ResultSet rs3 = ps3.executeQuery();
            if (rs3.next()) {
                ps4.setInt(5, id);
            } else {
                String error = String.format("Could not find a prescription with id: %d to update", id);
                log.error(error);
                throw new SQLException(error);
            }

            ps4.setString(3, updatedPrescription.getDiagnosis());
            ps4.setInt(4, updatedPrescription.getTreatment());

            ps4.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public void deletePrescription(int id) throws SQLException {
        String sql1 = "SELECT id from prescriptions WHERE id=?";
        String sql2 = "DELETE FROM prescriptions WHERE id=?";

        try(PreparedStatement ps1 = connection.prepareStatement(sql1);
            PreparedStatement ps2 = connection.prepareStatement(sql2)) {
            connection.setAutoCommit(false);

            ps1.setInt(1, id);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps2.setInt(1, id);
            } else {
                String error = String.format("Could not find a prescription with id: %d to delete", id);
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

    public List<Prescription> getPrescriptionsForPatient(String patientName) throws SQLException {
        List<Prescription> resultList = new ArrayList<>();

        String sql1 = "SELECT id FROM patients WHERE name=?";
        String sql2 = "SELECT * FROM prescriptions WHERE patient_id=?";

        try(PreparedStatement ps1 = connection.prepareStatement(sql1);
            PreparedStatement ps2 = connection.prepareStatement(sql2)) {
            ps1.setString(1, patientName);

            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps2.setInt(1, rs1.getInt("id"));
            } else {
                String error = String.format("Could not find a patient with name: %s", patientName);
                log.error(error);
                throw new SQLException(error);
            }

            ResultSet resultSet = ps2.executeQuery();

            while (resultSet.next()) {
                resultList.add(new Prescription(resultSet.getInt("id"),
                        this.patientDAO.getPatient(resultSet.getInt("patient_id")),
                        this.doctorDAO.getDoctor(resultSet.getInt("doctor_id")),
                        resultSet.getString("diagnosis"),
                        resultSet.getInt("treatment")));
            }

            return resultList;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public List<Prescription> getPrescriptionForDoctor(String doctorName) throws SQLException {
        List<Prescription> resultList = new ArrayList<>();

        String sql1 = "SELECT id FROM doctors WHERE name=?";
        String sql2 = "SELECT * FROM prescriptions WHERE doctor_id=?";
        try(PreparedStatement ps1 = connection.prepareStatement(sql1);
            PreparedStatement ps2 = connection.prepareStatement(sql2)) {
            ps1.setString(1, doctorName);

            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps2.setInt(1, rs1.getInt("id"));
            } else {
                String error = String.format("Could not find a doctor with name: %s", doctorName);
                log.error(error);
                throw new SQLException(error);
            }

            ResultSet resultSet = ps2.executeQuery();

            while (resultSet.next()) {
                resultList.add(new Prescription(resultSet.getInt("id"),
                        this.patientDAO.getPatient(resultSet.getInt("patient_id")),
                        this.doctorDAO.getDoctor(resultSet.getInt("doctor_id")),
                        resultSet.getString("diagnosis"),
                        resultSet.getInt("treatment")));
            }

            return resultList;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }
}
