package com.chixing.controller;


import com.chixing.common.JsonResult;
import com.chixing.entity.Customer;
import com.chixing.service.CustomerService;
import com.chixing.util.GetMessageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    public CustomerController(){
        System.out.println("CustomerController>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }


    //获得手机验证码
    @RequestMapping("getCode")
    @ResponseBody
    public String sendSMS(String phone){
        //手机验证码
      //  String code = GetMessageCode.getCode(phone);
        String code="123456";
        System.out.println("code:"+code);
        Map<String,Object> data = new HashMap<>();
        data.put("code",code);
        return code;
    }


    //手机登录
    @RequestMapping("loginByPhone")
    @ResponseBody
    public  JsonResult loginByPhone(long telno, HttpServletRequest request){
        //1. 查看该用户是否存在过，若存在过，查询出来绑定在session
       Customer customer =  customerService.getByTelno(telno);
        if(customer!=null){
            request.getSession().setAttribute("customerTelno",customer.getCustTelno());
            request.getSession().setAttribute("customerId",customer.getCustId());


            return JsonResult.createSuccessJsonResult(null);


        }else{ //2. 若用户没有存在过，注册一个（添加一个新账户），绑定在session
            Customer newCustomer =    new Customer();
            newCustomer.setCustTelno(telno);
            boolean flag = customerService.save(newCustomer);
            if(flag){
                //添加成功，登录成功,查询出来绑定在session
                customer =  customerService.getByTelno(telno);
                request.getSession().setAttribute("customerTelno",customer.getCustTelno());
                request.getSession().setAttribute("customerId",customer.getCustId());
                return JsonResult.createSuccessJsonResult(null);

            }else{
                //添加失败
                return JsonResult.createFailJsonResult(null);

            }

        }

    }

    @RequestMapping("loginByUsername")
    public  JsonResult loginByUsername(){
        return null;
    }




    @RequestMapping("goToLogin")
    public String goToLogin(){
        return "/customer/login";
    }

    public String goToRegist(){
        return "customer/regist";
    }
    //用户退出
    @RequestMapping("logout")
    @ResponseBody
    public JsonResult logout( HttpServletRequest request){
        request.getSession().invalidate();
        return JsonResult.createSuccessJsonResult(null);
    }
}
