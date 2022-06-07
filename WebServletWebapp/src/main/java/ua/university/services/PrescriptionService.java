package ua.university.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ua.university.DAO.PrescriptionDAO;
import ua.university.models.*;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class PrescriptionService {
    private PrescriptionDAO prescriptionDAO;

    public PrescriptionService() throws SQLException, ClassNotFoundException {
        this.prescriptionDAO = new PrescriptionDAO();
    }

    private static String objectToJson(Prescription data) {
        try {
            return new JSONObject(data).toString();
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    private static String objectsToJson(List<Prescription> data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //Set pretty printing of json
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            JSONArray array = new JSONArray();
            for (Prescription datum : data) {
                array.put(new JSONObject(datum));
            }
            return array.toString();
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String indexPrescription() throws SQLException {
        try {
            return objectsToJson(this.prescriptionDAO.indexPrescription());
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String indexPrescriptionForPatient(String patientName) throws SQLException {
        try {
            return objectsToJson(this.prescriptionDAO.getPrescriptionsForPatient(patientName));
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String indexPrescriptionForDoctor(String doctorName) throws SQLException {
        try {
            return objectsToJson(this.prescriptionDAO.getPrescriptionForDoctor(doctorName));
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String getPrescription(int id) throws SQLException {
        try {
            return objectToJson(this.prescriptionDAO.getPrescription(id));
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String addPrescription(Prescription prescription) throws SQLException {
        this.prescriptionDAO.savePrescription(prescription);
        try {
            prescription.setId(this.prescriptionDAO.getMaxGlobalId());
            return objectToJson(prescription);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String updatePrescription(int id, Prescription prescription) throws SQLException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.prescriptionDAO.updatePrescription(id, prescription);
            return mapper.writeValueAsString(prescription);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }


    public void deletePrescription(int id) throws SQLException {
        try {
            this.prescriptionDAO.deletePrescription(id);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        }
    }
}
