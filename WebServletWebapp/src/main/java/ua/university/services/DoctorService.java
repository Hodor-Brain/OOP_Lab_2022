package ua.university.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import ua.university.DAO.DoctorDAO;
import ua.university.models.Doctor;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.sql.SQLException;

@Slf4j
public class DoctorService {
    private DoctorDAO doctorDAO;

    public DoctorService() throws SQLException, ClassNotFoundException {
        this.doctorDAO = new DoctorDAO();
    }

    private static String objectToJson(Doctor data) {
        try {
            return new JSONObject(data).toString();
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    private static String objectsToJson(List<Doctor> data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //Set pretty printing of json
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            JSONArray array = new JSONArray();
            for (Doctor datum : data) {
                array.put(new JSONObject(datum));
            }
            return array.toString();
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String indexDoctor() {
        try {
            return objectsToJson(this.doctorDAO.indexDoctor());
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String getDoctor(int id) throws SQLException {
        try {
            return objectToJson(this.doctorDAO.getDoctor(id));
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String addDoctor(Doctor doctor) throws SQLException {
        this.doctorDAO.saveDoctor(doctor);
        try {
            doctor.setId(this.doctorDAO.getMaxGlobalId());
            return objectToJson(doctor);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String updateDoctor(int id, Doctor doctor) throws SQLException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.doctorDAO.updateDoctor(id, doctor);
            return mapper.writeValueAsString(doctor);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }


    public void deleteDoctor(int id) throws SQLException {
        try {
            this.doctorDAO.deleteDoctor(id);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        }
    }
}
