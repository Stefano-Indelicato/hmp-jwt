package com.hmp.jwt.controller;

import com.hmp.jwt.entity.Device;
import com.hmp.jwt.entity.Worker;
import com.hmp.jwt.dao.DeviceDAO;
import com.hmp.jwt.dao.WorkerDAO;
import com.hmp.jwt.event.UpdateDevice;
import com.hmp.jwt.event.UpdateDeviceEvent;
import com.hmp.jwt.service.AuthenticationManager;


import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Supplier;

@Path("/auth")
@RequestScoped
public class Authentication {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Context
    SecurityContext securityContext;

    @Inject
    DeviceDAO deviceDAO;

    @Inject
    WorkerDAO workerDAO;

    @Inject
    AuthenticationManager authenticationManager;

    @Context HttpHeaders httpHeaders;



    @Inject
    @UpdateDevice
    Event<UpdateDeviceEvent> updateDeviceEventEvent;


    @POST
    @Path("/authorize")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorize(Map<String, String> bodyMap){
        LOG.info("Start handshake");
        if (!bodyMap.containsKey("id") || !bodyMap.containsKey("secret")) {
            LOG.info("end handshake");
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        }
        try {
            Device device = deviceDAO.findById(Integer.parseInt(bodyMap.get("id")));
            if(!device.getSecret().equals(bodyMap.get("secret"))){
                LOG.error("end handshake: bad secret");
                return Response.status(HttpStatus.SC_FORBIDDEN).build();
            }
            Worker worker = workerDAO.findById(device.getWorkerId());
            LocalDateTime expirein = worker.getExpirein();
            if (expirein != null && expirein.compareTo(LocalDateTime.now()) < 0) {
                worker.setEnabled(false);
                workerDAO.merge(worker);
                LOG.error("end handshake");
                return Response.status(HttpStatus.SC_BAD_REQUEST).build();
            }

            String secret = authenticationManager.generateSecret();

            device.setSecret(secret);
            device.setModel(manageNullPointer(() -> {return httpHeaders.getRequestHeader("x-device-name").get(0);}));
            device.setManufacturer(manageNullPointer(() -> {return httpHeaders.getRequestHeader("x-device-manufacturer").get(0);}));
            device.setOsName(manageNullPointer(() -> {return httpHeaders.getRequestHeader("x-client-os").get(0);}));
            device.setOsVersion(manageNullPointer(() -> {return httpHeaders.getRequestHeader("x-client-os").get(0);}));
            device.setLanguage(manageNullPointer(() -> {return  httpHeaders.getRequestHeader("Accept-Language").get(0);}));

            updateDeviceEventEvent.fire(new UpdateDeviceEvent(device));

            String token = authenticationManager.generateToken(device.getId().toString(), secret);

            return Response.ok(Map.of("cache", true, "id", bodyMap.get("id"), "token", token)).build();

        } catch (Exception e){
            LOG.error("error handshake", e);
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        }
    }

    @GET()@Path("/validate")
    @RolesAllowed("USER")
    @Produces(MediaType.TEXT_PLAIN)
    public Response validateToken(){
        return Response.status(HttpStatus.SC_OK).build();
    }

    private String manageNullPointer(Supplier<String> s) {
        try {
            return s.get();
        } catch (Exception e) {
            return null;
        }
    }

}
