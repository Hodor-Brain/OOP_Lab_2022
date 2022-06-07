package ua.university.eventListeners;

import org.jboss.resteasy.spi.HttpRequest;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.*;
import ua.university.DAO.PatientDAO;
import ua.university.DAO.DoctorDAO;
import ua.university.models.Patient;
import ua.university.models.Doctor;

import javax.ws.rs.core.MultivaluedMap;

import java.sql.SQLException;
import java.util.*;

public class RegistrationEventListenerProvider implements EventListenerProvider {
    private final KeycloakSession session;
    private final RealmProvider model;

    public RegistrationEventListenerProvider(KeycloakSession session) {
        this.session = session;
        this.model = session.realms();
    }

    @Override
    public void onEvent(Event event) throws RuntimeException {

        if (EventType.REGISTER.equals(event.getType())) {
            RealmModel realm = this.model.getRealm(event.getRealmId());
            UserModel newRegisteredUser = this.session.users().getUserById(event.getUserId(), realm);

            org.jboss.resteasy.spi.HttpRequest req = session.getContext().getContextObject(HttpRequest.class);
            MultivaluedMap<String, String> formParameters = req.getFormParameters();

            String ourRole = formParameters.get("role").toString();
            PatientDAO patientDAO = null;
            DoctorDAO doctorDAO = null;

            try {
                patientDAO = new PatientDAO();
                doctorDAO = new DoctorDAO();
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

            if (Objects.equals(ourRole, "[patient]")) {
                RoleModel roleModel = realm.getClientById(realm.getClientByClientId("backend-client").getId()).getRole("ROLE_PATIENT");
                System.out.println("Our Role model: " + roleModel.getName());
                newRegisteredUser.grantRole(roleModel);

                try {
                    patientDAO.savePatient(new Patient(-1, newRegisteredUser.getFirstName()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (Objects.equals(ourRole, "[doctor]")) {
                RoleModel roleModel = realm.getClientById(realm.getClientByClientId("backend-client").getId()).getRole("ROLE_DOCTOR");
                System.out.println("Our Role model: " + roleModel.getName());
                newRegisteredUser.grantRole(roleModel);

                try {
                    doctorDAO.saveDoctor(new Doctor(-1, -1, newRegisteredUser.getFirstName()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Hello, am I alive? Am I? (•_•) ( •_•)>⌐■-■ (⌐■_■) -> " + newRegisteredUser.getUsername());
        }

    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

    }

    @Override
    public void close() {

    }
}
