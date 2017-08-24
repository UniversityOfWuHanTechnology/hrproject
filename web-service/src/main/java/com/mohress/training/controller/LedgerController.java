package com.mohress.training.controller;

import com.mohress.training.dto.Response;
import com.mohress.training.dto.ledger.LedgerApplyDto;
import com.mohress.training.enums.ResultCode;
import com.mohress.training.service.ledger.LedgerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 台账API接口
 *
 */
@Controller
@RequestMapping("api/ledger/")
public class LedgerController {

    @Resource
    private LedgerService ledgerService;

    @ResponseBody
    @RequestMapping("apply")
    public Response apply(@RequestBody LedgerApplyDto ledgerApplyDto){

        String userId = "";
        ledgerApplyDto.setApplicant(userId);

        ledgerService.apply(ledgerApplyDto);

        return new Response(ResultCode.SUCCESS.getCode(), "台账申请成功");
    }
}
