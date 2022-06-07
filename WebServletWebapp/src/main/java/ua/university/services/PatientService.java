package ua.university.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import ua.university.DAO.PatientDAO;
import ua.university.models.Patient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import com.fasterxml.jackson.databind.SerializationFeature;

import java.sql.SQLException;

@Slf4j
public class PatientService {
    private PatientDAO patientDAO;

    public PatientService() throws SQLException, ClassNotFoundException {
        this.patientDAO = new PatientDAO();
    }

    private static String objectToJson(Patient data) {
        try {
            return new JSONObject(data).toString();
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    private static String objectsToJson(List<Patient> data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //Set pretty printing of json
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            JSONArray array = new JSONArray();
            for (Patient datum : data) {
                array.put(new JSONObject(datum));
            }
            return array.toString();
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String indexPatient() throws SQLException {
        try {
            return objectsToJson(this.patientDAO.indexPatient());
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String getPatient(int id) throws SQLException {
        try {
            return objectToJson(this.patientDAO.getPatient(id));
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String addPatient(Patient patient) throws SQLException {
        try {
            this.patientDAO.savePatient(patient);
            patient.setId(this.patientDAO.getMaxGlobalId());
            return objectToJson(patient);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String updatePatient(int id, Patient patient) throws SQLException {
        try {
            this.patientDAO.updatePatient(id, patient);
            return objectToJson(patient);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }


    public void deletePatient(int id) throws SQLException {
        try {
            this.patientDAO.deletePatient(id);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        }
    }

    public String getPatientByName(String name) throws SQLException {
        try {
            return objectToJson(this.patientDAO.getPatient(name).get(0));
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }
}
