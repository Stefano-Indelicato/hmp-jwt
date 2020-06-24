package com.hmp.jwt.controller;

import com.hmp.jwt.entity.Device;
import com.hmp.jwt.entity.Worker;
import com.hmp.jwt.dao.DeviceDAO;
import com.hmp.jwt.dao.WorkerDAO;
import com.hmp.jwt.service.AuthenticationManager;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.Map;

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

    @Context HttpHeaders headers;

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
            deviceDAO.merge(device);
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


}
