package ua.university.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessToken;
import ua.university.models.Prescription;
import ua.university.services.PrescriptionService;
import ua.university.utils.KeycloakTokenUtil;
import ua.university.utils.ServletUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/api/prescriptions/*")
@Slf4j
public class PrescriptionController extends HttpServlet {
    private PrescriptionService service;

    @Override
    public void init() {
        try {
            this.service = new PrescriptionService();
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

            /*AccessToken accessToken = KeycloakTokenUtil.getToken(request, request.getHeader("Authorization"));
            assert accessToken != null;
            String userName = "";
            String prescriptionsJsonString = "";

            if (idValue == -1) {
                if (KeycloakTokenUtil.getRoles(accessToken).contains("ROLE_ADMIN")) {
                    prescriptionsJsonString = this.service.indexPrescription();
                } else if (KeycloakTokenUtil.getRoles(accessToken).contains("ROLE_PATIENT")) {
                    userName = KeycloakTokenUtil.getName(accessToken);
                    prescriptionsJsonString = this.service.indexPrescriptionForPatient(userName);
                } else if (KeycloakTokenUtil.getRoles(accessToken).contains("ROLE_DOCTOR")) {
                    userName = KeycloakTokenUtil.getName(accessToken);
                    prescriptionsJsonString = this.service.indexPrescriptionForDoctor(userName);
                }

            } else {
                prescriptionsJsonString = this.service.getPrescription(idValue);
            }*/

            String prescriptionsJsonString = "";
            if (idValue == -1) {
                prescriptionsJsonString = this.service.indexPrescription();
            } else {
                prescriptionsJsonString = this.service.getPrescription(idValue);
            }

            out.print(prescriptionsJsonString);
        } catch (SQLException exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                response.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Prescription get error");
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

            Prescription prescription = new ObjectMapper().readValue(requestBody.toString(), Prescription.class);

            String prescriptionsJsonString = this.service.addPrescription(prescription);

            out.print(prescriptionsJsonString);

        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                resp.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Prescription post error");
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
            this.service.deletePrescription(id);
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                resp.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Prescription delete error");
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

            Prescription prescription = new ObjectMapper().readValue(requestBody.toString(), Prescription.class);

            int idValue = ServletUtils.getURIId(req.getRequestURI());
            String prescriptionJsonString = this.service.updatePrescription(idValue, prescription);

            out.print(prescriptionJsonString);
            resp.setStatus(200);
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                resp.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Prescription update error");
            }
        } catch (Exception exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
        }
    }
}
