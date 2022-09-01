package com.zny.iot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WBS
 * Date:2022/8/31
 */

@RestController
@RequestMapping("/iot")
@Tag(name = "iot",description = "物联网模块")
public class IotController {

    @Value("${iot_username}")
    private String name;

    @Value("${iot_password}")
    private String password;

    @Operation(summary = "iot 测试接口", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true)))})
    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String Test() {
        return "iot";
    }
}
