package ua.university.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ua.university.models.Doctor;
import ua.university.services.DoctorService;
import ua.university.utils.ServletUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/api/doctors/*")
@Slf4j
public class DoctorController extends HttpServlet {
    private DoctorService service;

    @Override
    public void init() {
        try {
            this.service = new DoctorService();
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

            String doctorsJsonString = "";
            if (idValue == -1) {
                doctorsJsonString = this.service.indexDoctor();
            } else {
                doctorsJsonString = this.service.getDoctor(idValue);
            }

            out.print(doctorsJsonString);
        } catch (SQLException exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                response.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Doctor get error");
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

            Doctor doctor = new ObjectMapper().readValue(requestBody.toString(), Doctor.class);
            String doctorsJsonString = this.service.addDoctor(doctor);

            out.print(doctorsJsonString);

        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                resp.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Doctor post error");
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
            this.service.deleteDoctor(id);
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                resp.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Doctor delete error");
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
            Doctor doctor = new ObjectMapper().readValue(requestBody.toString(), Doctor.class);
            String doctorJsonString = this.service.updateDoctor(idValue, doctor);

            out.print(doctorJsonString);
            resp.setStatus(200);
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                resp.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Doctor update error");
            }
        } catch (Exception exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
        }
    }
}
