package com.hmp.jwt.controller;

import com.hmp.jwt.configuration.manager.GlobalConfigurationManager;
import com.hmp.jwt.dao.SettingDAO;
import com.hmp.jwt.entity.Device;
import com.hmp.jwt.entity.Setting;
import com.hmp.jwt.entity.Worker;
import com.hmp.jwt.dao.DeviceDAO;
import com.hmp.jwt.dao.WorkerDAO;
import com.hmp.jwt.service.AuthenticationManager;


import io.vertx.mutiny.core.eventbus.EventBus;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
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

    @Inject
    DeviceDAO deviceDAO;

    @Inject
    WorkerDAO workerDAO;

    @Inject
    AuthenticationManager authenticationManager;

    @Context HttpHeaders httpHeaders;

    @Inject
    GlobalConfigurationManager globalConfigurationManager;

    @Inject
    EventBus eventBus;

    @POST
    @Path("/handshake")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorize(Map<String, String> bodyMap){
        LOG.info("Start handshake");
        if (!bodyMap.containsKey("id") || !bodyMap.containsKey("secret")) {
            LOG.info("end handshake");
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        }
        try {
            Device device = deviceDAO.findById(Long.parseLong(bodyMap.get("id")));
            if(!device.getSecret().equals(bodyMap.get("secret"))){
                LOG.error("end handshake: bad secret");
                return Response.status(HttpStatus.SC_FORBIDDEN).build();
            }
            Worker worker = device.getWorker();
            if(authenticationManager.istWorkerExpired(worker)){
                LOG.error("end handshake");
                return Response.status(HttpStatus.SC_BAD_REQUEST).build();
            }

            String secret = authenticationManager.generateSecret();
            device.setSecret(secret);

            updateDevice(device, httpHeaders);

            String token = authenticationManager.generateToken(device.getId().toString(), secret);

            return Response.ok(Map.of("cache", true, "id", device.getId(), "token", token)).build();

        } catch (Exception e){
            LOG.error("error handshake", e);
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        }
    }

    @RolesAllowed("USER")
    @GET()@Path("/authorize")
    @Produces(MediaType.TEXT_PLAIN)
    public Response authorize(){
        return Response.status(HttpStatus.SC_OK).build();
    }

    @POST
    @Path("/handshake/mdm")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorizeMDM(Map<String, String> bodyMap) {
        LOG.info("Start handshake MDM");
        if (!bodyMap.containsKey("identifier") || !bodyMap.containsKey("secret")) {
            LOG.info("end handshake");
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        }
        try {
            if (!globalConfigurationManager.getSecretMDM().equals(bodyMap.get("secret"))) {
                LOG.error("end handshake: bad secret");
                return Response.status(HttpStatus.SC_FORBIDDEN).build();
            }
            Worker worker = workerDAO.findByEmail(bodyMap.get("identifier"));
            if(authenticationManager.istWorkerExpired(worker)){
                LOG.error("end handshake");
                return Response.status(HttpStatus.SC_BAD_REQUEST).build();
            }
            Device device = new Device();
            device.setWorker(worker);
            String secret = authenticationManager.generateSecret();
            device.setSecret(secret);
            deviceDAO.persist(device);

            updateDevice(device, httpHeaders);

            String token = authenticationManager.generateToken(device.getId().toString(), secret);

            return Response.ok(Map.of("cache", true, "id", device.getId(), "token", token)).build();

        } catch (Exception e){
            LOG.error("error handshake", e);
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        }
    }

    private  void updateDevice(Device device, HttpHeaders httpHeaders){

        device.setModel(manageNullPointer(() -> {return httpHeaders.getRequestHeader("x-device-name").get(0);}));
        device.setManufacturer(manageNullPointer(() -> {return httpHeaders.getRequestHeader("x-device-manufacturer").get(0);}));
        device.setOsName(manageNullPointer(() -> {return httpHeaders.getRequestHeader("x-client-os").get(0);}));
        device.setOsVersion(manageNullPointer(() -> {return httpHeaders.getRequestHeader("x-client-os").get(0);}));
        device.setLanguage(manageNullPointer(() -> {return  httpHeaders.getRequestHeader("Accept-Language").get(0);}));

        eventBus.sendAndForget("updateDevice", device);

    }

    private String manageNullPointer(Supplier<String> s) {
        try {
            return s.get();
        } catch (Exception e) {
            return null;
        }
    }

}
