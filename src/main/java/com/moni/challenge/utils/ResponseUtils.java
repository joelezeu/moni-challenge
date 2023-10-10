package com.moni.challenge.utils;

import com.moni.challenge.domain.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseUtils {
    public ResponseEntity<Response> getResponse(boolean status, String message){
        Response response = new Response();
        response.setStatus(status);
        response.setMessage(message);
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<Response> getResponse(boolean status, String message, Object data){
        Response response = new Response();
        response.setStatus(status);
        response.setMessage(message);
        response.setData(data);
        return ResponseEntity.ok(response);
    }

    public Response getCustomerResponse(boolean status, String message){
        Response response = new Response();
        response.setStatus(status);
        response.setMessage(message);
        return response;
    }
}
