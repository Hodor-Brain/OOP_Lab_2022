package ua.university.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ua.university.models.Patient;
import ua.university.services.PatientService;
import ua.university.utils.ServletUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(value = "/api/patients/*")
@Slf4j
public class PatientController extends HttpServlet {
    private PatientService service;

    @Override
    public void init() {
        try {
            this.service = new PatientService();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            int idValue = ServletUtils.getURIId(request.getRequestURI());

            String patientsJsonString = "";

            String name = request.getParameter("name");
            if (name != null) {
                patientsJsonString = this.service.getPatientByName(name);
            } else if (idValue == -1) {
                patientsJsonString = this.service.indexPatient();
            } else {
                patientsJsonString = this.service.getPatient(idValue);
            }

            out.print(patientsJsonString);
        } catch (SQLException exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                response.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Patient get error");
            }
        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            StringBuilder requestBody = new StringBuilder();
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }


            Patient patient = new ObjectMapper().readValue(requestBody.toString(), Patient.class);
            String patientsJsonString = this.service.addPatient(patient);

            out.print(patientsJsonString);

        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                resp.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Patient post error");
            }
        } catch (Exception exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int id = ServletUtils.getURIId(req.getRequestURI());
            this.service.deletePatient(id);
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                resp.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Patient delete error");
            }
        } catch (Exception exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            StringBuilder requestBody = new StringBuilder();
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }

            int idValue = ServletUtils.getURIId(req.getRequestURI());
            Patient patient = new ObjectMapper().readValue(requestBody.toString(), Patient.class);
            String patientJsonString = this.service.updatePatient(idValue, patient);

            out.print(patientJsonString);
            resp.setStatus(200);
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                resp.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Patient update error");
            }
        } catch (Exception exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
        }
    }
}
