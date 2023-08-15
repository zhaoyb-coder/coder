package com.rpc.controller;

import com.rpc.api.ServerService;
import com.rpc.client.annotation.RpcAutowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * @author zhaoyubo
 * @title ClientController
 * @description 客户端Controller
 * @create 2023/8/14 16:18
 **/
@Controller
public class ClientController {

    @RpcAutowired(version = "1.0")
    private ServerService serverService;

    @GetMapping("/hello/rpc")
    public ResponseEntity<String> pullServiceInfo(@RequestParam("name") String name){
        return ResponseEntity.ok(serverService.helloRPC(name));
    }


}
